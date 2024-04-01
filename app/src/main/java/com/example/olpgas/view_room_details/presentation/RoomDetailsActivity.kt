package com.example.olpgas.view_room_details.presentation

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgas.databinding.ActivityRoomDetailsBinding


class RoomDetailsActivity : AppCompatActivity() {

    private val binding: ActivityRoomDetailsBinding by lazy {
        ActivityRoomDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}