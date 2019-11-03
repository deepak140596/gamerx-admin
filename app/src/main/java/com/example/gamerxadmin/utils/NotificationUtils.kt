package com.example.gamerxadmin.utils

import com.example.gamerxadmin.models.RoomCredentials
import com.example.gamerxadmin.models.User

fun createRoomAlert( roomCredentials: RoomCredentials,players: List<User>){
    val message = "Hi Gamer,\nMatch starts in 10 mins. \nRoom ID: ${roomCredentials.roomId}\nPass: ${roomCredentials.password}"
    val numbers = phonesToString(players)

    SMSUtils.sendSMS(message,numbers)
}

fun phonesToString(players: List<User>): String {
    var phones = ""
    for(player in players) {
        phones += player.phoneNumber
    }

    return phones
}