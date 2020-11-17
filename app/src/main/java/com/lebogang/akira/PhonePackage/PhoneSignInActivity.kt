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