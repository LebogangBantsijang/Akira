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

package com.lebogang.akira.PhonePackage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.UserActivity
import com.lebogang.akira.databinding.ActivityPhoneBinding
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PhoneActivity : AppCompatActivity(), OnFailureListener {
    private val binding:ActivityPhoneBinding by lazy{
        ActivityPhoneBinding.inflate(layoutInflater)
    }
    private val firebaseAuth:FirebaseAuth by lazy {
        Firebase.auth
    }
    private var id:String = ""
    private val counter = object : CountDownTimer(60000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            binding.counterTextView.text = "00:" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        }
        override fun onFinish() {
            binding.counterTextView.text = "00:00"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        initSendCodeViews()
        initVerifyViews()
        binding.ccp.registerCarrierNumberEditText(binding.phoneNumberEditText)
    }

    private fun initToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initSendCodeViews(){
        binding.nextButton.setOnClickListener {
            if (binding.ccp.isValidFullNumber){
                toggleViews()
                val number = binding.ccp.fullNumberWithPlus
                val option = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setActivity(this)
                    .setPhoneNumber(number)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setCallbacks(getCallbacks())
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(option)
            }else
                binding.errorTextView.text = "Invalid Number"
        }
    }

    private fun initVerifyViews(){
        binding.verifyButton.setOnClickListener {
            if(binding.codeEditText.text.isNullOrEmpty()){
                val credential = PhoneAuthProvider.getCredential(id, binding.codeEditText.text.toString())
                signIn(credential)
            }else{
                binding.errorTextView.text = "Enter verification code"
            }
        }
    }

    private fun toggleViews(){
        if (binding.seekBar.visibility ==  View.VISIBLE){
            binding.seekBar.visibility = View.GONE
        }
        else{
            binding.seekBar.visibility = View.VISIBLE
        }
    }

    private fun signIn(credential: PhoneAuthCredential){
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                startActivity(Intent(this, UserActivity::class.java).apply {
                    putExtra("User", it.user)
                })
            }
            .addOnFailureListener(this,this)
    }

    override fun onFailure(p0: Exception) {
        binding.errorTextView.text = p0.toString()
    }

    private fun getCallbacks():PhoneAuthProvider.OnVerificationStateChangedCallbacks{
        return object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                counter.cancel()
                binding.nextButton.isEnabled = true
                binding.verifyButton.isEnabled = false
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                binding.nextButton.isEnabled = true
                binding.verifyButton.isEnabled = false
                counter.cancel()
                counter.onFinish()
                binding.errorTextView.text = p0.toString()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                id = p0
                super.onCodeSent(p0, p1)
                counter.start()
                binding.nextButton.isEnabled = false
                binding.verifyButton.isEnabled = true
                toggleViews()
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                binding.nextButton.isEnabled = false
                binding.verifyButton.isEnabled = true
                counter.cancel()
                counter.onFinish()
            }
        }
    }
}