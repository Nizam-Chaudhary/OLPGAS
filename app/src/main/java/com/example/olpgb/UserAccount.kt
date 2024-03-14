package com.example.olpgb

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgb.databinding.ActivityUserAccountBinding

class UserAccount : AppCompatActivity() {


    private val binding: ActivityUserAccountBinding by lazy {
        ActivityUserAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.uEmail.text = "Hello"
        binding.uPassword.text = "Hello"
        binding.uName.text = "Hello"
        binding.uAge.text = "Hello"
        binding.uGender.text = "Hello"
        binding.uPhoneNumber.text = "Hello"
        binding.uAddress.text = "Hello"


        //Edit profile btn
        binding.uProfileEditBtn.setOnClickListener {
            startActivity(Intent(this@UserAccount,UserProfileEdit::class.java))
        }

    }
}