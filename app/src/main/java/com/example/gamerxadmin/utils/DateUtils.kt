package com.example.gamerxadmin.utils

import java.text.SimpleDateFormat
import java.util.*

fun getThirdDayName(): String {
    val dayInMilliseconds = 1000 * 60 * 60 * 24
    val now = Date()
    val currentTimeInMs = now.time
    val thirdDay = currentTimeInMs + dayInMilliseconds*2
    now.time = thirdDay

    val sdf = SimpleDateFormat("EEEE")
    return sdf.format(now)

}

fun getHHMMDDDMMM(time: Long):String{
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm")
    return sdf.format(time)
}