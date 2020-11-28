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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.R
import com.lebogang.akira.UserActivity
import com.lebogang.akira.databinding.ActivityEmailSignInBinding

class EmailSignInActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEmailSignInBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupView(){
        binding.nextButton.setOnClickListener {
            //reset error message if present
            binding.errorMsgTextView.text = ""
            //get information
            val email = binding.emailEditText.text
            val password = binding.passwordEditText.text
            //check if null
            if (email == null || password == null)
                binding.errorMsgTextView.text = "Invalid values"
            else{
                //sign in user
                if (password.length > 7) signInUser(email.toString(), password.toString())
                else binding.errorMsgTextView.text = "Invalid Password"
            }
        }
    }

    private fun createUser(email:String, password:String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
            if (it.isSuccessful){
                //Start new activity
                val user:FirebaseUser = auth.currentUser!!
                val intent = Intent(this, UserActivity :: class.java).apply {
                    putExtra("User", user)
                }
                startActivity(intent)
            }else{
                Snackbar.make(this.window.peekDecorView()
                        ,"Authentication Failed.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInUser(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
            if (it.isSuccessful){
                //Start new activity
                val user:FirebaseUser = auth.currentUser!!
                val intent = Intent(this, UserActivity :: class.java).apply {
                    putExtra("User", user)
                }
                startActivity(intent)
            }else{
                createUser(email, password)
            }
        }
    }
}