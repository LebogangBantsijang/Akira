package com.lebogang.akira.PhonePackage

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lebogang.akira.databinding.ActivityPhoneAuthenticateBinding
import java.util.concurrent.TimeUnit

class PhoneAuthenticateActivity : AppCompatActivity(), PhoneCustomInterface{
    private lateinit var binding: ActivityPhoneAuthenticateBinding
    private val verificationCallbacks = VerificationCallbacks()
    private val auth = Firebase.auth

    private val counter = object :CountDownTimer(59000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            //display counter
            binding.counterTextView.text = "00:" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        }

        override fun onFinish() {
            //Reset count view
            binding.counterTextView.text = "00:00"
            //Enable resend button
            binding.resendButton.isEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneAuthenticateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupView()
        getNumber()
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupView(){
        binding.resendButton.setOnClickListener { getNumber() }
        //this is use if instant verification failed or auto retrieval failed
        binding.verifyButton.setOnClickListener {
            if(binding.codeEditText.text == null)
                binding.errorMsgTextView.text = "Invalid Code"
            else{
                val code = binding.codeEditText.text.toString()
                val credential = verificationCallbacks.getCredential(code)
                signIn(credential)
            }
        }
    }

    private fun getNumber(){
        val formattedNumber = intent.extras!!.getString("FormattedNumber")
        val fullNumber = intent.extras!!.getString("FullNumber")
        binding.phoneTextView.text = formattedNumber
        sendCode(fullNumber!!)
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


    override fun onPhoneCallback(result: VerificationCallbacks.Feedback) {
        when (result){
            VerificationCallbacks.Feedback.CODE_SENT->{
                //remove error message and start counter
                binding.errorMsgTextView.text = ""
                counter.start()
            }

            VerificationCallbacks.Feedback.VERIFICATION_COMPLETED->{
                // cancel counter and sign in
                counter.cancel()
                val credential = verificationCallbacks.getCredential("")
                signIn(credential)
            }

            VerificationCallbacks.Feedback.VERIFICATION_FAILED->{
                //notify user and reset everything
                binding.errorMsgTextView.text = "Verification Failed"
                counter.cancel()
                counter.onFinish()
            }

            VerificationCallbacks.Feedback.TIMEOUT->{
                //user failed to enter code in time, reset time
                binding.errorMsgTextView.text = "Timed Out"
                counter.cancel()
                counter.onFinish()
            }
        }
    }


    private fun signIn(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener(this) {
            if (it.isSuccessful){
                //next activity
                val user = auth.currentUser
                val intent = Intent().apply {
                    putExtra("User", user)
                }
            }else
                binding.errorMsgTextView.text = "Failed to Sign In"
        }
    }

}