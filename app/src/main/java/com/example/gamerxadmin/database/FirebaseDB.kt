package com.example.gamerxadmin.database

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.gamerxadmin.analytics.D
import com.example.gamerxadmin.analytics.E
import com.example.gamerxadmin.analytics.log
import com.example.gamerxadmin.models.*
import com.example.gamerxadmin.utils.appContext
import com.example.gamerxadmin.utils.toastError
import com.example.gamerxadmin.utils.toastSuccess
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

fun createNewMatchToDB(match: Match) {
    val document = FirebaseFirestore.getInstance().collection("matches").document(match.matchId)

    document.set(match).addOnSuccessListener {
        toastSuccess("Match Created")
    }
}

fun updateMatch(match: Match) {
    val document = FirebaseFirestore.getInstance().collection("matches").document(match.matchId)

    document.update(
        "startTime", match.startTime,
        "status", match.status,
        "teamType", match.teamType,
        "photoUrl", match.photoUrl,
        "gameId", match.gameId,
        "minPlayers", match.minPlayers,
        "maxPlayers", match.maxPlayers,
        "perspectiveMode", match.perspectiveMode,
        "entryFee", match.entryFee,
        "pricePerKill", match.pricePerKill,
        "winAmount", match.winAmount,
        "map", match.map
    ).addOnSuccessListener {
        toastSuccess("Match Updated")
    }
}

fun getMatches(status: String): MutableLiveData<List<Match>> {
    val query = FirebaseFirestore.getInstance().collection("matches")
        .whereEqualTo("status",status)
    val matches: MutableLiveData<List<Match>> = MutableLiveData()

    query.addSnapshotListener { value, e ->
        if (e != null) {
            log(E, "Failed to get")
            return@addSnapshotListener
        }

        val matchList = mutableListOf<Match>()
        for (doc in value!!) {
            matchList.add(doc.toObject(Match::class.java))
        }
        matches.value = matchList

    }
    return matches
}

fun getJoinedPlayers(match: Match): MutableLiveData<List<User>> {
    val query = FirebaseFirestore.getInstance().collection("matches").document(match.matchId)
        .collection("joined_players")
    val players: MutableLiveData<List<User>> = MutableLiveData()

    query.addSnapshotListener { value, e ->
        if (e != null) {
            log(E, "Failed to get joined players")
            return@addSnapshotListener
        }

        val playersList = mutableListOf<User>()
        for (doc in value!!) {
            playersList.add(doc.toObject(User::class.java))
        }

        players.value = playersList

    }
    return players
}

fun updateMatchStatistics(playerList: List<User>, match: Match) {
    val collectionRef = FirebaseFirestore.getInstance().collection("matches")
        .document(match.matchId).collection("joined_players")
    val userNodeRef = FirebaseFirestore.getInstance().collection("users")


    for (player in playerList) {
        FirebaseFirestore.getInstance().runTransaction { transaction ->
            val playerMatchDocRef = collectionRef.document("${player.uid}")
            val userDocRef = userNodeRef.document("${player.uid}")

            val userSnapshot = transaction.get(userDocRef)
            val moneyEarned = userSnapshot.getDouble("moneyEarned")
            val totalKills = userSnapshot.getLong("kills")
            val walletBalance = userSnapshot.getDouble("wallet")

            val updateWalletTransaction = Transaction()
            updateWalletTransaction.amount = player.moneyEarned ?: 0.0
            updateWalletTransaction.transactionId = System.currentTimeMillis().toString()
            updateWalletTransaction.transactedFor = "Earned from match ${match.matchId}"
            updateWalletTransaction.type = TYPE_CREDIT
            val newTransactionRef = userDocRef.collection("transactions")
                .document(updateWalletTransaction.transactionId)


            transaction.update(
                playerMatchDocRef, "kills", player.kills,
                "moneyEarned", player.moneyEarned,
                "winner", player.isWinner,
                "tournamentRank", player.tournamentRank
            )

            transaction.update(
                userDocRef, "kills", (totalKills?.plus(player.kills ?: 0)),
                "moneyEarned", (moneyEarned?.plus(player.moneyEarned ?: 0.0)),
                "wallet", (walletBalance?.plus(player.moneyEarned ?: 0.0))
            )

            if (updateWalletTransaction.amount > 0) {
                transaction.set(newTransactionRef, updateWalletTransaction)
            }
        }
            .addOnSuccessListener {
                log(D, "Transaction Success")
                toastSuccess("Player Update Success")
            }.addOnFailureListener { e ->
                log(E, "Transaction failure: $e")
                toastError("Player Update Failed")
            }
    }
}

fun getTransactions(userId: String): MutableLiveData<List<Transaction>> {
    val transactionRef =  FirebaseFirestore.getInstance().collection("users")
        .document(userId).collection("transactions").orderBy("date",Query.Direction.DESCENDING)
    val transactionLiveData: MutableLiveData<List<Transaction>> = MutableLiveData()

    transactionRef.get().addOnSuccessListener {
        val list = mutableListOf<Transaction>()
        for(doc in it){
            list.add(doc.toObject(Transaction::class.java))
        }
        transactionLiveData.value = list
    }

    return transactionLiveData
}

fun getTransactionByPhoneNumber(activity: AppCompatActivity,phoneNumber: String): MutableLiveData<List<Transaction>?> {
    val mutableLiveData = MutableLiveData<List<Transaction>?>()
    getUserByPhoneNumber(phoneNumber).addOnSuccessListener {
        val user = it.documents[0].toObject(User::class.java)

        user?.uid?.let {uid->
            getTransactions(uid).observe(activity, Observer {
                mutableLiveData.value = it
            })

        }

    }

    return mutableLiveData
}

fun createNewTransactionByPhoneNumber(phoneNumber: String,transaction: Transaction){
    getUserByPhoneNumber(phoneNumber).addOnSuccessListener {
        val user = it.documents[0].toObject(User::class.java)
        user?.uid?.let { uid ->
            createNewTransactionByUid(uid,transaction)
        }

    }
}

fun createNewTransactionByUid(userId: String,transaction: Transaction){
    val transactionRef = FirebaseFirestore.getInstance().collection("users")
        .document(userId).collection("transactions")
        .document(transaction.transactionId)

    val userRef = FirebaseFirestore.getInstance().collection("users")
        .document(userId)

    FirebaseFirestore.getInstance().runTransaction { ft ->
        val userSnapshot = ft.get(userRef)
        var walletBalance = userSnapshot.getDouble("wallet")

        when {
            transaction.type == TYPE_DEBIT -> walletBalance = walletBalance?.minus(transaction.amount)
            transaction.type == TYPE_CREDIT -> walletBalance = walletBalance?.plus(transaction.amount)
            transaction.type == TYPE_REFUND -> walletBalance = walletBalance?.plus(transaction.amount)
            else -> {}
        }
        log(E,"$walletBalance")

        ft.set(transactionRef,transaction)
        ft.update(userRef,"wallet",walletBalance)
    }.addOnSuccessListener {
        toastSuccess("Added Transaction")
    }.addOnFailureListener {
        log(E,"${it.message}")
        toastError("Failed to add transaction")
    }
}

fun getUserByPhoneNumber(phoneNumber: String) : Task<QuerySnapshot> {
    val userRef = FirebaseFirestore.getInstance().collection("users")
        .whereEqualTo("phoneNumber",phoneNumber)

    return userRef.get()
}

fun setRoomIdPw(match: Match, roomCredentials: RoomCredentials){
    val roomRef = FirebaseFirestore.getInstance().collection("matches").document(match.matchId)
        .collection("room").document("credentials")

    val matchRef = FirebaseFirestore.getInstance().collection("matches").document(match.matchId)
    FirebaseFirestore.getInstance().runTransaction { transaction ->

        transaction.set(roomRef,roomCredentials)
        transaction.update(matchRef,"roomCredentialsAvailable",true)
    }.addOnSuccessListener {
        toastSuccess("Room Credentials Set")
    }.addOnFailureListener {
        toastError("Failed to set Credentials")
        log(E,"#${it.message}")
    }
}
