package com.lebogang.akira.PhonePackage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.lebogang.akira.databinding.ActivityPhoneSignInBinding

class PhoneSignInActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPhoneSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupEditText()
        setupNextButton()
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupEditText(){
        binding.ccp.registerCarrierNumberEditText(binding.phoneNumberEditText)
        binding.phoneNumberEditText.doOnTextChanged { text, start, before, count ->
            if (binding.errorMsgTextView.text.toString().equals("invalid number"))
                binding.errorMsgTextView.text = ""
        }
    }

    private fun setupNextButton(){
        binding.nextButton.setOnClickListener {
            if (binding.ccp.isValidFullNumber)
                startActivity(Intent(this, PhoneAuthenticateActivity::class.java).apply {
                    putExtra("FormattedNumber", binding.ccp.formattedFullNumber)
                    putExtra("FullNumber", binding.ccp.fullNumberWithPlus)
                })
            else{
                binding.phoneNumberEditText.requestFocus()
                binding.errorMsgTextView.text = "invalid number"
            }
        }
    }
}