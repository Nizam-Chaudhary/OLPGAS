package com.example.olpgas.manage_room.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.example.olpgas.R
import com.example.olpgas.databinding.ActivityMyRoomBinding
import com.example.olpgas.manage_room.viewmodel.ManageRoomViewModel
import com.example.olpgas.roomdetails.adapter.RoomRecyclerAdapter


class MyRoomActivity : AppCompatActivity() {
    private val binding: ActivityMyRoomBinding by lazy {
        ActivityMyRoomBinding.inflate(layoutInflater)
    }

    private val manageRoomViewModel: ManageRoomViewModel by lazy {
        ViewModelProvider(this)[ManageRoomViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fabAddRoom.setOnClickListener {

            val transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.container_transform)

            TransitionManager.beginDelayedTransition(window.decorView as ViewGroup, transition)

            val intent = Intent(this, AddRoomActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this, binding.fabAddRoom, "shared_element_end_root")
            startActivity(intent, options.toBundle())
        }

        setMyRoomDataAdapter()

        refreshLayout()
    }

    private fun setMyRoomDataAdapter(){
        binding.rvMyRooms.layoutManager = LinearLayoutManager(this)
        val adapter = RoomRecyclerAdapter(emptyList(), this, true)
        binding.rvMyRooms.adapter = adapter

        manageRoomViewModel.fetchMyRooms()

        manageRoomViewModel.myRoomDetails.observe(this) {
            adapter.roomsData = it
            adapter.notifyDataSetChanged()
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun refreshLayout() {
        binding.refreshLayout.setOnRefreshListener {
            setMyRoomDataAdapter()
        }
    }
}
