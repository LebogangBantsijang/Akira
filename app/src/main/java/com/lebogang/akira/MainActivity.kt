package com.lebogang.akira

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lebogang.akira.EmailPackage.EmailSignInActivity
import com.lebogang.akira.GooglePackage.GoogleSignInObject
import com.lebogang.akira.PhonePackage.PhoneSignInActivity
import com.lebogang.akira.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var googleSignInObject: GoogleSignInObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupButtons()
        googleSignInObject = GoogleSignInObject(this)
    }

    private fun setupButtons(){
        binding.phoneButton.setOnClickListener {
            startActivity(Intent(this, PhoneSignInActivity::class.java))
        }
        binding.emailButton.setOnClickListener {
            startActivity(Intent(this, EmailSignInActivity::class.java))
        }
        binding.googleButton.setOnClickListener {
            googleSignInObject.signIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleSignInObject.onActivityResult(requestCode,data)
    }
}