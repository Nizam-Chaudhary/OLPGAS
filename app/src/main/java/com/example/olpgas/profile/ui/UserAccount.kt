package com.example.olpgas.profile.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgas.databinding.ActivityUserAccountBinding

class UserAccount : AppCompatActivity() {


    private val binding: ActivityUserAccountBinding by lazy {
        ActivityUserAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.uEmail.text = "VP.patel.personal@gmail.com"
        binding.uName.text = "VP"
        binding.uAge.text = "19"
        binding.uGender.text = "male"
        binding.uPhoneNumber.text = "1234567890"
        binding.uAddressStreet.text = "12" //Street Name or Number
        binding.uAddressCity.text="Valsad"
        binding.uAddressState.text="Gujarat"


        //Edit profile btn
        binding.uEdit.setOnClickListener {
            startActivity(Intent(this@UserAccount, UserProfileEdit::class.java))
        }

    }
}