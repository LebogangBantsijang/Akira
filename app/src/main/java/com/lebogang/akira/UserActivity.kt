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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private val binding : ActivityUserBinding by lazy{
        ActivityUserBinding.inflate(layoutInflater)
    }
    private val firebaseAuth:FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initSignOutViews()
        val user = intent.extras!!.getParcelable<FirebaseUser>("User")
        if (user != null ) setUserDetails(user)
    }

    private fun setUserDetails(user:FirebaseUser){
        val name = if (user.displayName != null) "Username: " + user.displayName else "Username:"
        val email = if (user.email != null) "Email: " + user.email else "Email:"
        val phone = if (user.phoneNumber != null) "Phone: " + user.phoneNumber else "Phone:"
        binding.usernameTextView.text = name
        binding.emailTextView.text = email
        binding.phoneTextView.text = phone
    }

    private fun initSignOutViews(){
        binding.signOutButton.setOnClickListener {
            firebaseAuth.signOut()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}