package com.example.gamerxadmin.utils

import android.net.Uri
import java.lang.Exception

interface FirebaseListener{
    fun onSuccess()
    fun onError(exception: Exception)
}

interface StorageListener{
    fun onUploadSuccess(result: Uri?)
    fun onError(exception: Exception)
}