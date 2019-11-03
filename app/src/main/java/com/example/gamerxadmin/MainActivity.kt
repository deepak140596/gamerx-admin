package com.example.gamerxadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gamerxadmin.analytics.E
import com.example.gamerxadmin.analytics.log
import com.example.gamerxadmin.login.GoogleSignIn
import com.example.gamerxadmin.ui.match.CreateMatch
import com.example.gamerxadmin.ui.match.UpcomingMatches
import com.example.gamerxadmin.ui.useroperation.transactions.UserTransactions
import com.example.gamerxadmin.utils.SMSUtils
import com.example.gamerxadmin.utils.firebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setOnClick()
    }

    private fun setOnClick(){
        mainCreateMatchBtn.setOnClickListener {
            startActivity(Intent(this,CreateMatch::class.java))
        }

        mainUpcomingMatchesBtn.setOnClickListener {
            startActivity(Intent(this,UpcomingMatches::class.java))
        }

        mainTransactionsBtn.setOnClickListener {
            startActivity(Intent(this,UserTransactions::class.java))
        }

        sendSms.setOnClickListener {
            SMSUtils.sendSMS("New Msg","+918100509479")
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
