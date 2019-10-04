package com.example.gamerxadmin.models

import java.io.Serializable

class Transaction():Serializable{
    var amount: Double = 0.0
    var transactionId: String = ""
    var details: String = ""
    var transactedFor :String = ""
    var date : Long = System.currentTimeMillis()
    var type: String = TYPE_NONE


}

val TYPE_CREDIT = "Credit"
val TYPE_DEBIT = "Debit"
val TYPE_REFUND = "Refund"
val TYPE_NONE = "None"