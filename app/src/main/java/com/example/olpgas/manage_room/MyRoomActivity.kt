package com.example.olpgas.manage_room

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgas.databinding.ActivityMyRoomBinding


class MyRoomActivity : AppCompatActivity() {
    private val binding: ActivityMyRoomBinding by lazy {
        ActivityMyRoomBinding.inflate(layoutInflater)

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.fabAddRoom.setOnClickListener {
            startActivity(Intent(this@MyRoomActivity, AddRoomActivity::class.java))
        }
    }
}
