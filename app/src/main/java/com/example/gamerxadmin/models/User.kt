package com.example.gamerxadmin.models

import java.io.Serializable

class User() : Serializable{
    var uid: String? =""
    var phoneNumber: String? =""
    var name: String?=""
    var photoUrl: String? =""
    var tournamentRank : Int? = 0
    var inGameName : String? = ""
    var inGameId : String? = ""
    var kills : Int? = 0
    var moneyEarned : Double? = 0.0
    var isWinner: Boolean = false
    var tournamentsParticipated : List<Match> = emptyList()
    var wallet: Double = 0.0
}

