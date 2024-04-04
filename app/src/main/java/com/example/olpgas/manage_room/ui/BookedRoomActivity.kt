package com.example.olpgas.manage_room.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgas.databinding.ActivityBookedRoomBinding

class BookedRoomActivity : AppCompatActivity() {

    private val binding: ActivityBookedRoomBinding by lazy {
        ActivityBookedRoomBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

//        binding.visibilityBtn.setOnClickListener {
//            if (binding.hideView.visibility == View.VISIBLE) {
//                TransitionManager.beginDelayedTransition(binding.card, AutoTransition())
//                binding.hideView.visibility = View.GONE
//                binding.materialDivider.visibility = View.GONE
//                binding.visibilityBtn.setImageResource(R.drawable.ic_arrow_up)
//            } else {
//                TransitionManager.beginDelayedTransition(binding.card, AutoTransition())
//                binding.hideView.visibility = View.VISIBLE
//                binding.materialDivider.visibility = View.VISIBLE
//                binding.visibilityBtn.setImageResource(R.drawable.ic_arrow_down)
//            }
//        }
    }
}