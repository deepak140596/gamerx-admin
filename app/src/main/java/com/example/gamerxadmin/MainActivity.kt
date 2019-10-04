package com.example.gamerxadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gamerxadmin.login.GoogleSignIn
import com.example.gamerxadmin.match.CreateMatch
import com.example.gamerxadmin.utils.firebaseUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setOnClick()
    }

    private fun setOnClick(){
        main_create_match_btn.setOnClickListener {
            startActivity(Intent(this,CreateMatch::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if(firebaseUser == null){

            startActivity(Intent(this,GoogleSignIn::class.java))
            finish()
        }
    }
}
