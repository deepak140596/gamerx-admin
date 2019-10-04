package com.example.gamerxadmin.utils

import android.widget.Toast
import es.dmoral.toasty.Toasty

fun toastError(message: String,length: Int = Toast.LENGTH_SHORT){
    Toasty.error(appContext,message,length).show()
}

fun toastInfo(message: String,length: Int = Toast.LENGTH_SHORT){
    Toasty.info(appContext,message,length).show()
}

fun toastSuccess(message: String,length: Int = Toast.LENGTH_SHORT){
    Toasty.success(appContext,message,length).show()
}

fun toastNormal(message: String,length: Int = Toast.LENGTH_SHORT){
    Toasty.normal(appContext,message,length).show()
}