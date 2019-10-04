package com.example.gamerxadmin.utils

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

const val APP_SHARED_PREF = "app_shared_prefs"
const val USER_DETAILS_SP = "user_details_sp"

var appContext= FirebaseApp.getInstance().applicationContext
var firebaseUser = FirebaseAuth.getInstance().currentUser