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

    private lateinit var binding : ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = intent.extras!!.getParcelable<FirebaseUser>("User")
        if (user != null ) setUserDetails(user)
    }

    private fun setUserDetails(user:FirebaseUser){
        val name = if (user.displayName != null) user.displayName else "Unknown Name"
        val email = if (user.email != null) "Email:" + user.email else "Email:"
        binding.nameTextView.text = name
        binding.emailTextView.text = email
    }

    //avoid going back to sign in
    override fun onBackPressed() {
        //super.onBackPressed()
        moveTaskToBack(true)
    }
}