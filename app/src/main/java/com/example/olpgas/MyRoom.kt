package com.example.olpgas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgas.databinding.ActivityMyRoomBinding

class MyRoom : AppCompatActivity() {

    private val binding: ActivityMyRoomBinding by lazy {
        ActivityMyRoomBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}