package com.example.olpgas.manage_room.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.example.olpgas.R
import com.example.olpgas.databinding.ActivityMyRoomBinding
import com.example.olpgas.manage_room.presentation.add_room.AddRoomActivity


class MyRoomActivity : AppCompatActivity() {
    private val binding: ActivityMyRoomBinding by lazy {
        ActivityMyRoomBinding.inflate(layoutInflater)
    }

    /*private val manageRoomViewModel: ManageRoomViewModel by lazy {
        ViewModelProvider(this)[ManageRoomViewModel::class.java]
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        //setMyRoomDataAdapter()

        refreshLayout()
    }

   /* private fun setMyRoomDataAdapter(){
        binding.rvMyRooms.layoutManager = LinearLayoutManager(this)
        val adapter = RoomsRecyclerViewAdapter(emptyList(), this, true)
        binding.rvMyRooms.adapter = adapter

        manageRoomViewModel.fetchMyRooms()

        manageRoomViewModel.myRoomDetails.observe(this) {
            adapter.roomsData = it
            adapter.notifyDataSetChanged()
            binding.refreshLayout.isRefreshing = false
        }
    }*/

    private fun refreshLayout() {
        binding.refreshLayout.setOnRefreshListener {
            //setMyRoomDataAdapter()
        }
    }
}
