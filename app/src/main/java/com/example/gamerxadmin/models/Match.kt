package com.example.gamerxadmin.models

import java.io.Serializable

/** status **/
const val UPCOMING = "upcoming"
const val ONGOING = "ongoing"
const val ENDED = "ended"
const val CANCELLED = "cancelled"

/** game mode **/
const val FPP = "fpp"
const val TPP = "tpp"

/** team types **/
const val SOLO = "solo"
const val DUO = "duo"
const val SQUADS = "squads"

/**
 * @param matchId = UID of the match
 * @param startTime = start time of the match
 * @param status = status of the match
 * @param teamType = no of player per team
 * @param gameId = game name
 * @param perspectiveMode = FPP/TPP
 */

class Match (): Serializable {
    var matchId: String = ""
    var startTime: Long = 0L
    var status: String = ""
    var teamType: String = ""
    var photoUrl: String = ""
    var gameId: String = ""
    var minPlayers : Int = 0
    var maxPlayers: Int = 0
    var perspectiveMode: String = ""
    var entryFee: Double =0.0
    var pricePerKill: Double = 0.0
    var winAmount: Double = 0.0
    var map: String = ""
    var winner: User? =null
    var mostKills: User? =null
    var playersJoined : Int = 0
    var playersJoinedList: ArrayList<String> = arrayListOf()
    var roomCredentialsAvailable : Boolean = false
}