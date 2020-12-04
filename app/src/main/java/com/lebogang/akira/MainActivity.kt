/*
 * Copyright (c) 2020. - Lebogang Bantsijang
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * compliance License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License on an "IS BASIS", WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the for specific language governing permissions and limitations
 * under the License.
 */

package com.lebogang.akira

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.EmailPackage.EmailSignInActivity
import com.lebogang.akira.FacebookPackage.FacebookSignInObject
import com.lebogang.akira.GooglePackage.GoogleSignInObject
import com.lebogang.akira.PhonePackage.PhoneSignInActivity
import com.lebogang.akira.databinding.ActivityMain2Binding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMain2Binding
    private lateinit var googleSignInObject: GoogleSignInObject
    private lateinit var facebookSignInObject: FacebookSignInObject
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        checkIfUserIsSignedIn()
        googleSignInObject = GoogleSignInObject(this)
        facebookSignInObject = FacebookSignInObject(this)
        setupButtons()
    }

    //initialise sign in buttons
    private fun setupButtons(){
        binding.phoneButton.setOnClickListener {
            startActivity(Intent(this, PhoneSignInActivity::class.java))
        }
        binding.emailButton.setOnClickListener {
            startActivity(Intent(this, EmailSignInActivity::class.java))
        }
        binding.googleButton.setOnClickListener {
            googleSignInObject.signIn()
        }
        binding.facebookButton.setPermissions("email")
        binding.facebookButton.registerCallback(facebookSignInObject.callbackManager, facebookSignInObject.getFacebookCallback())
    }

    private fun checkIfUserIsSignedIn(){
        val auth = Firebase.auth
        val user = auth.currentUser
        if (user != null) startActivity(Intent(this, UserActivity :: class.java).apply {
            putExtra("User", user)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //googleSignInObject!!.onActivityResult(data)
        if (requestCode == googleSignInObject.requestCode){
            googleSignInObject.onActivityResult(data)
        } else facebookSignInObject.onActivityResult(requestCode,resultCode,data)
    }
}