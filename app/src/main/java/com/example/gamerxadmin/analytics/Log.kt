package com.example.gamerxadmin.analytics

import android.util.Log

val E = 1
val D = 2
val V = 3
val I = 4
val WTF = 5
val W = 6
val S = 7
val TAG = "ANALYTICS"

fun log(type: Int, message: String){
    when(type){
        E -> {
            Log.e(TAG, message)
        }
        D -> {
            Log.d(TAG, message)
        }
        V -> {
            Log.v(TAG, message)
        }
        I -> {
            Log.i(TAG, message)
        }
        WTF -> {
            Log.wtf(TAG, message)
        }
        W -> {
            Log.w(TAG, message)
        }
        S -> {
            Log.i(TAG,message)
        }

    }
}