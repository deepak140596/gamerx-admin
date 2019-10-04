package com.example.gamerxadmin.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gamerxadmin.MainActivity
import com.example.gamerxadmin.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.example.gamerxadmin.analytics.E
import com.example.gamerxadmin.analytics.I
import com.example.gamerxadmin.analytics.log
import kotlinx.android.synthetic.main.activity_google_sign_in.*

class GoogleSignIn : AppCompatActivity() {

    private val RC_SIGN_IN = 100
    private lateinit var gso : GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        mAuth = FirebaseAuth.getInstance()

        googleSignInBtn.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        // update ui
        updateUI(user)
    }

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val gAccount = task.getResult(ApiException::class.java)
                gAccount?.let {
                    firebaseAuthWithGoogle(gAccount)
                }

            } catch (e : ApiException) {
                log(E,"Google sign in failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(gAccount : GoogleSignInAccount){

        log(I,"firebase with google: ${gAccount.id}")
        val credential = GoogleAuthProvider.getCredential(gAccount.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    log(I,"Sign in with credentials successful")
                    val user = mAuth.currentUser
                    // update ui
                    updateUI(user)
                } else {
                    log(E,"sign in with credential failed. Exception: ${task.exception}")
                    // update ui
                    updateUI(null)
                }

            }

    }

    private fun updateUI(user: FirebaseUser?){
        user?.let {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}

