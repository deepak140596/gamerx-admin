package com.example.gamerxadmin.database

import com.example.gamerxadmin.models.Match
import com.example.gamerxadmin.utils.toastSuccess
import com.google.firebase.firestore.FirebaseFirestore

fun createNewMatchToDB(match: Match){
    val document = FirebaseFirestore.getInstance().collection(match.status).document(match.matchId)

    document.set(match).addOnSuccessListener {
        toastSuccess("Match Created")
    }
}