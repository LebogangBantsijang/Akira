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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.EmailPackage.EmailSignUpActivity
import com.lebogang.akira.FacebookPackage.FacebookSignInObject
import com.lebogang.akira.GooglePackage.GoogleSignInObject
import com.lebogang.akira.PhonePackage.PhoneActivity
import com.lebogang.akira.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding :ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val googleSignInObject:GoogleSignInObject by lazy {
        GoogleSignInObject(this)
    }
    private val facebookSignInObject: FacebookSignInObject by lazy {
        FacebookSignInObject(this)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkIfUserIsSignedIn()
        setupButtons()
    }

    private fun setupButtons(){
        binding.phoneButton.setOnClickListener {
            startActivity(Intent(this, PhoneActivity::class.java))
        }
        binding.emailButton.setOnClickListener {
            startActivity(Intent(this, EmailSignUpActivity::class.java))
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
        if (requestCode == googleSignInObject.requestCode){
            googleSignInObject.onActivityResult(data)
        } else facebookSignInObject.onActivityResult(requestCode,resultCode,data)
    }
}