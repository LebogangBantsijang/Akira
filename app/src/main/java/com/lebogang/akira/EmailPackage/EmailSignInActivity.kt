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

package com.lebogang.akira.EmailPackage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.R
import com.lebogang.akira.databinding.ActivityEmailSignInBinding

class EmailSignInActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEmailSignInBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView(){
        binding.nextButton.setOnClickListener {
            binding.errorMsgTextView.text = ""
            val email = binding.emailEditText.text!!.toString()
            val password = binding.passwordEditText.text!!.toString()
            if (email == null || password == null)
                binding.errorMsgTextView.text = "invalid values"
            else{
                if (signInUser(email, password) || createUser(email, password)){
                    val user = auth.currentUser
                    val intent = Intent().apply {
                        putExtra("User", user)
                    }
                }else{
                    binding.errorMsgTextView.text = "all has Failed"
                }
            }
        }
    }

    private fun createUser(email:String, password:String) : Boolean{
        var result = false
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
            if (it.isSuccessful){
                result = true
            }
        }
        return result
    }

    private fun signInUser(email:String, password:String) : Boolean{
        var result = false
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
            if (it.isSuccessful){
                result = true
            }
        }
        return result
    }
}