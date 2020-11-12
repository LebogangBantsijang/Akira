package com.lebogang.akira.GooglePackage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GoogleSignInObject(private val activity: AppCompatActivity) {

    private val requestCode:Int = 987

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("")
        .requestEmail()
        .build()

    private val googleSignInClient = GoogleSignIn.getClient(activity, gso)

    private val auth = Firebase.auth

    fun signIn(){
        val intent = googleSignInClient.signInIntent
        activity.startActivityForResult(intent, requestCode)
    }

    fun onActivityResult(requestCode: Int, data: Intent?){
        if (this.requestCode == requestCode){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                //Sign In successful, authenticate with firebase
                firebaseAuthenticate(account!!.idToken!!)
            }catch (e : ApiException){
                //Sign in failed
            }
        }
    }

    private fun firebaseAuthenticate(tokenId:String){
        val credential = GoogleAuthProvider.getCredential(tokenId, null)
        auth.signInWithCredential(credential).addOnCompleteListener(activity){
            if (it.isSuccessful){
                //move to the next activity
                val user = auth.currentUser
                val intent = Intent().apply {
                    putExtra("User", user)
                }
            }else{
                Snackbar.make(activity.window.peekDecorView()
                    ,"Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}