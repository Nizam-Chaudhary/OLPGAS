package com.example.olpgb.roomdetails.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgb.R
import com.example.olpgb.databinding.ActivityRoomDetailsBinding


class RoomDetails : AppCompatActivity() {

    private val binding: ActivityRoomDetailsBinding by lazy {
        ActivityRoomDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_details)

//        val transitionName = intent.getStringExtra("transitionName")
//        val imageResourceId = intent.getIntExtra("imageResourceId", 0)
//
//        binding.im.setImageResource(imageResourceId)
//
//        binding.im.transitionName = transitionName
    }
}