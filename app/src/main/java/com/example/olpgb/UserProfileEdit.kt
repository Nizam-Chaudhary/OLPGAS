package com.example.olpgb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgb.databinding.ActivityUserProfileEditBinding

class UserProfileEdit : AppCompatActivity() {

    private val binding: ActivityUserProfileEditBinding by lazy {
        ActivityUserProfileEditBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}