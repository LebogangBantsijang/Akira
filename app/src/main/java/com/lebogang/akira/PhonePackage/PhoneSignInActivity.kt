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
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.UserActivity
import com.lebogang.akira.databinding.ActivityPhoneSign2Binding
import java.util.concurrent.TimeUnit

class PhoneSignInActivity : AppCompatActivity(), PhoneCustomInterface {
    private lateinit var binding:ActivityPhoneSign2Binding
    private val verificationCallbacks = VerificationCallbacks()
    private val auth = Firebase.auth

    private val counter = object : CountDownTimer(59000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            //display counter
            binding.counterTextView.text = "00:" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        }
        override fun onFinish() {
            //Reset count view
            binding.counterTextView.text = "00:00"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneSign2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupEditText()
        setupVerifyButton()
        setupSendButton()
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * setup ccp with phone number edit text.
     * */
    private fun setupEditText(){
        binding.ccp.registerCarrierNumberEditText(binding.phoneNumberEditText)
    }

    /**
     * when user click verify button, get the full number from the ccp view and send the verification
     * code via sms. if the number is null then notify user
     * */
    private fun setupSendButton(){
        binding.sendButton.setOnClickListener {
            if (binding.ccp.isValidFullNumber){
                val fullNumber = binding.ccp.fullNumberWithPlus
                showViews()
                sendCode(fullNumber)
            }else{
                binding.errorMsgTextView.text = "Invalid Number"
                binding.phoneNumberEditText.requestFocus()
            }
        }
    }

    /**
     * when user clicks verify, get the verification code from the edit text and attempt to sign in
     * to firebase. If the verification code is incorrect then notify user.
     * */
    private fun setupVerifyButton(){
        binding.verifyButton.setOnClickListener {
            if (binding.codeEditText.text != null){
                val credential : PhoneAuthCredential? = verificationCallbacks.getCredential(binding.codeEditText.text.toString())
                if (credential != null){
                    showViews()
                    signIn(credential)
                }
            }else
                binding.errorMsgTextView.text = "Invalid Code"
        }
    }

    /**
     * used to indicate to user that there is something happening to in the background
     * */
    private fun showViews(){
        binding.progressBar.visibility = View.VISIBLE
    }

    /**
     * Self-explanatory
     * */
    private fun hideViews(){
        binding.progressBar.visibility = View.GONE
    }

    private fun sendCode(number:String){
        verificationCallbacks.phoneCustomInterface = this
        val option = PhoneAuthOptions.newBuilder(auth)
                .setActivity(this)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(verificationCallbacks)
                .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    /**
     * callbacks
     * */
    override fun onPhoneCallback(result: VerificationCallbacks.Feedback) {
        when (result){
            VerificationCallbacks.Feedback.CODE_SENT->{
                binding.errorMsgTextView.text = ""
                counter.start()
                binding.progressBar.visibility = View.GONE
            }

            VerificationCallbacks.Feedback.VERIFICATION_COMPLETED->{
                // cancel counter and sign in
                counter.cancel()
                signIn(verificationCallbacks.credential)
            }

            VerificationCallbacks.Feedback.VERIFICATION_FAILED->{
                //notify user and reset everything
                binding.errorMsgTextView.text = "Verification Failed"
                counter.cancel()
                counter.onFinish()
                hideViews()
            }

            VerificationCallbacks.Feedback.TIMEOUT->{
                //user failed to enter code in time, reset time
                binding.errorMsgTextView.text = "Timed Out"
                counter.cancel()
                counter.onFinish()
                showViews()
            }
        }
    }

    /*
    * sign in user, if successful then start user activity else notify user
    * */
    private fun signIn(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener(this) {
            hideViews()
            if (it.isSuccessful){
                //next activity
                val user = auth.currentUser
                val intent = Intent(this, UserActivity :: class.java).apply {
                    putExtra("User", user)
                }
                startActivity(intent)
            }else
                binding.errorMsgTextView.text = "Authentication Failed"
        }
    }

}