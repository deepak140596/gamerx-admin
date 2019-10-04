package com.example.gamerxadmin.database

import androidx.lifecycle.MutableLiveData
import com.example.gamerxadmin.analytics.E
import com.example.gamerxadmin.analytics.log
import com.example.gamerxadmin.models.Match
import com.example.gamerxadmin.utils.toastSuccess
import com.google.firebase.firestore.FirebaseFirestore

fun createNewMatchToDB(match: Match){
    val document = FirebaseFirestore.getInstance().collection(match.status).document(match.matchId)

    document.set(match).addOnSuccessListener {
        toastSuccess("Match Created")
    }
}

fun updateMatch(match: Match){
    val document = FirebaseFirestore.getInstance().collection(match.status).document(match.matchId)

    document.update("startTime", match.startTime,
        "status",match.status,
        "teamType",match.teamType,
        "photoUrl",match.photoUrl,
        "gameId",match.gameId,
        "minPlayers",match.minPlayers,
        "maxPlayers",match.maxPlayers,
        "perspectiveMode",match.perspectiveMode,
        "entryFee",match.entryFee,
        "pricePerKill",match.pricePerKill,
        "winAmount",match.winAmount,
        "map",match.map).addOnSuccessListener {
        toastSuccess("Match Updated")
    }
}

fun getMatches(status: String): MutableLiveData<List<Match>> {
    val query = FirebaseFirestore.getInstance().collection(status)
    val matches : MutableLiveData<List<Match>> = MutableLiveData()

    query.addSnapshotListener { value, e ->
        if(e != null){
            log(E,"Failed to get")
            return@addSnapshotListener
        }

        val matchList = mutableListOf<Match>()
        for(doc in value!!){
            matchList.add(doc.toObject(Match::class.java))
        }
        matches.value = matchList

    }
    return matches
}