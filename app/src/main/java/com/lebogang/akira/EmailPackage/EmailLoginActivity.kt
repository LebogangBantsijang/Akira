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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.TextUtil
import com.lebogang.akira.UserActivity
import com.lebogang.akira.databinding.ActivityEmailLoginBinding
import java.lang.Exception

class EmailLoginActivity : AppCompatActivity(), OnFailureListener {
    private val binding:ActivityEmailLoginBinding by lazy {
        ActivityEmailLoginBinding.inflate(layoutInflater)
    }

    private val firebaseAuth:FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        initLoginViews()
        initOtherViews()
    }

    private fun initToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initLoginViews(){
        binding.loginButton.setOnClickListener {
            if (!TextUtil.isValuesNull(binding.emailEditText.text, binding.passwordEditText.text)) {
                //Sign in the user
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnFailureListener(this, this)
                    .addOnSuccessListener {
                        startActivity(Intent(this, UserActivity :: class.java).apply {
                            putExtra("User", it.user)
                        })
                    }
            }else
                binding.errorTextView.text = TextUtil.getMessage(TextUtil.ErrorTypes.NULL_VALUES)
        }
    }

    private fun initOtherViews(){
        binding.resetButton.setOnClickListener {
            if (!TextUtil.isValuesNull(binding.emailEditText.text)){
                val email = binding.emailEditText.text.toString()
                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                    MaterialAlertDialogBuilder(this).apply {
                        setTitle("Reset Link Sent")
                        setMessage("A reset link has been sent to :$email")
                        setPositiveButton("Okay", null)
                    }.create().show()
                }.addOnFailureListener(this)
            }else
                binding.errorTextView.text = "Please enter your email"
        }
    }

    override fun onFailure(p0: Exception) {
        binding.errorTextView.text = p0.toString()
    }


}