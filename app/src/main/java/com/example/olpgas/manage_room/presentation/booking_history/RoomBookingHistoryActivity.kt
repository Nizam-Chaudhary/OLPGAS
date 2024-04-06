package com.example.olpgas.manage_room.presentation.booking_history

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgas.bookings_history.presentation.BookingsRecyclerViewAdapter
import com.example.olpgas.databinding.ActivityRoomBookingHistoryBinding
import com.google.android.material.transition.platform.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomBookingHistoryActivity : AppCompatActivity() {

    private val binding:ActivityRoomBookingHistoryBinding  by lazy {
        ActivityRoomBookingHistoryBinding.inflate(layoutInflater)
    }

    private val viewModel: RoomBookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.sharedElementEnterTransition = buildContainerTransform(true)
        window.sharedElementReturnTransition = buildContainerTransform(false)

        setAdapter()
    }

    private fun setAdapter() {
        viewModel.onEvent(RoomBookingEvent.OnCreate)

        val adapter = BookingsRecyclerViewAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allBookings.observe(this) {
            adapter.roomsData = it
            adapter.notifyDataSetChanged()


            if(it.isEmpty()) {
                binding.noBookings.visibility = View.VISIBLE
            } else {
                binding.noBookings.visibility = View.GONE
            }
        }
    }

    private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
        val transform = MaterialContainerTransform(this, entering)
        transform.addTarget(android.R.id.content)
        return transform
    }
}