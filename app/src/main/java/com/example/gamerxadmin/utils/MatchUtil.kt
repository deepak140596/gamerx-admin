package com.example.gamerxadmin.utils

import android.content.Context
import com.example.gamerxadmin.models.Match


fun getSpotsLeft(match: Match) : Int{
    return  match.maxPlayers - match.playersJoined
}

fun getPositionInArray(text: String,array: Array<String>): Int{
    for(i in array.indices){
        if(array[i] == text)
            return i
    }
    return -1
}

