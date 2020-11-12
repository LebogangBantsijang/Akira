package com.lebogang.akira

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lebogang.akira.PhonePackage.PhoneSignInActivity
import com.lebogang.akira.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupButtons()
    }

    private fun setupButtons(){
        binding.phoneButton.setOnClickListener {
            startActivity(Intent(this, PhoneSignInActivity::class.java))
        }
        binding.emailButton.setOnClickListener {
            startActivity(Intent(this, EmailSignInActivity::class.java))
        }

    }
}