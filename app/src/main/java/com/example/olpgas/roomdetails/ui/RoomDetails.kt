package com.example.olpgas.roomdetails.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgas.R
import com.example.olpgas.databinding.ActivityRoomDetailsBinding


class RoomDetails : AppCompatActivity() {

    private val binding: ActivityRoomDetailsBinding by lazy {
        ActivityRoomDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_details)

    }
}