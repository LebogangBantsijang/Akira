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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.TextUtil
import com.lebogang.akira.UserActivity
import com.lebogang.akira.databinding.ActivityEmailSignUpBinding
import java.lang.Exception

class EmailSignUpActivity : AppCompatActivity(), OnFailureListener {

    private val binding:ActivityEmailSignUpBinding by lazy{
        ActivityEmailSignUpBinding.inflate(layoutInflater)
    }
    private val firebaseAuth:FirebaseAuth by lazy{
        Firebase.auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        initSignUpViews()
        initOtherViews()
    }

    private fun initToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initSignUpViews(){
        binding.signUpButton.setOnClickListener {
            if (!TextUtil.isValuesNull(binding.emailEditText.text
                    , binding.passwordEditText.text, binding.confirmPasswordEditText.text)){
                if (binding.confirmPasswordEditText.text.toString() == binding.passwordEditText.text.toString()){
                    //Sign in the user
                    val email = binding.emailEditText.text.toString()
                    val password = binding.confirmPasswordEditText.text.toString()
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnFailureListener(this, this)
                        .addOnSuccessListener {
                            startActivity(Intent(this, UserActivity :: class.java).apply {
                                putExtra("User", it.user)
                            })
                        }
                }else
                    binding.errorTextView.text = TextUtil.getMessage(TextUtil.ErrorTypes.PASSWORDS_NO_MATCH)
            }else
                binding.errorTextView.text = TextUtil.getMessage(TextUtil.ErrorTypes.NULL_VALUES)
        }
    }

    private fun initOtherViews(){
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, EmailLoginActivity:: class.java))
        }
    }

    override fun onFailure(p0: Exception) {
        binding.errorTextView.text = p0.toString()
    }
}