package com.example.olpgas.roomdetails.ui

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.example.olpgas.R
import com.example.olpgas.auth.data.remote.SupabaseClient
import com.example.olpgas.databinding.ActivityRoomDetailsBinding
import com.example.olpgas.manage_room.ui.AddRoomActivity
import com.example.olpgas.manage_room.ui.UpdateRoom
import com.example.olpgas.roomdetails.adapter.RoomsImageRecyclerPagerAdapter
import com.example.olpgas.roomdetails.viewmodel.RoomsViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class RoomDetails : AppCompatActivity() {

    private val binding: ActivityRoomDetailsBinding by lazy {
        ActivityRoomDetailsBinding.inflate(layoutInflater)
    }

    private val roomsViewModel: RoomsViewModel by lazy {
        ViewModelProvider(this)[RoomsViewModel::class.java]
    }
    private var manageRoom = false

    private var roomId = 0
    private var featureID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        manageRoom = intent.getBooleanExtra("manageRoom", false)
        roomId = intent.getIntExtra("roomId",1)
        val ownerId = intent.getStringExtra("ownerId") ?: ""
        val roomName = intent.getStringExtra("roomName") ?: ""

        setData(roomId)
        setImagesAdapter(ownerId, roomName)

        if(manageRoom) {
            setFab()
        }
    }

    private fun setData(roomId: Int) {
        roomsViewModel.getFullRoomDetails(roomId)

        roomsViewModel.fullRoomDetails.observe(this) {roomDetails ->
            featureID = roomDetails.roomFeatureId
            binding.detailedRoomName.text = roomDetails.roomName
            binding.detailedRoomLocation.text = "${roomDetails.streetNumber}, ${roomDetails.landMark}, ${roomDetails.city}, ${roomDetails.state}"
            binding.roomTypeTv.text = roomDetails.roomType
            binding.shareableTv.text = roomDetails.shareable.toString() + " Person"
            binding.roomAreaTv.text = roomDetails.roomArea.toString() + " Sq. Ft."
            binding.detailedRoomAbout.text = roomDetails.description
            binding.roomPrice.text = String.format(Locale.UK, "%,d", roomDetails.rentAmount) + "/-"
            binding.roomDepositTv.text = String.format(Locale.UK, "%,d", roomDetails.deposit) + "/-"
            for(amenity in roomDetails.features) {
                val chip = Chip(this)
                chip.text = amenity
                chip.isChecked = true
                chip.isEnabled = true
                binding.detailedRoomAmenitiesChipGroup.addView(chip)
            }
            for(suitableFor in roomDetails.suitableFor) {
                val chip = Chip(this)
                chip.text = suitableFor
                chip.isChecked = true
                chip.isEnabled = true
                binding.detailsRoomSuitableForGroup.addView(chip)
            }

        }
    }

    private fun setImagesAdapter(ownerId: String,roomName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bucket = SupabaseClient.client.storage.from("RoomPics")
                val files = bucket.list("$ownerId/$roomName")

                val adapter = RoomsImageRecyclerPagerAdapter(
                    ownerId,
                    roomName,
                    files,
                    this@RoomDetails
                )
                withContext(Dispatchers.Main) {
                    binding.detailedRoomImageViewpager.adapter = adapter
                }
            }catch (e: Exception) {
                Log.d("Room","Error: ${e.message}")
            }
        }
    }

    private fun setFab() {
        var isFabVisible = false

        binding.fabExpand.visibility = View.VISIBLE

        binding.fabExpand.setOnClickListener {
            if(!isFabVisible) {
                val animEdit = ObjectAnimator.ofFloat(binding.fabEditRoom, "translationY", 200f, 0f)
                val animRemove = ObjectAnimator.ofFloat(binding.fabDeleteRoom,"translationY", 200f, 0f)
                animEdit.duration = 100
                animEdit.start()
                animRemove.duration = 100
                animRemove.start()
                binding.fabEditRoom.visibility = View.VISIBLE
                binding.fabDeleteRoom.visibility = View.VISIBLE
                isFabVisible = true
                binding.fabExpand.setIconResource(R.drawable.ic_expand_less)
            } else {
                val animEdit = ObjectAnimator.ofFloat(binding.fabEditRoom, "translationY", 0f, 200f)
                val animRemove = ObjectAnimator.ofFloat(binding.fabDeleteRoom,"translationY", 0f, 200f)
                animEdit.duration = 100
                animEdit.start()
                animRemove.duration = 100
                animRemove.start()
                isFabVisible = false
                binding.fabExpand.setIconResource(R.drawable.ic_expand_more)
                binding.fabEditRoom.visibility = View.GONE
                binding.fabDeleteRoom.visibility = View.GONE
            }
        }

        binding.fabDeleteRoom.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Remove Room")
                .setMessage("Are you sure you want to remove Room Details")
                .setPositiveButton("Yes") {_,_ ->
                    roomsViewModel.removeRoomDetails(roomsViewModel.fullRoomDetails.value!!.id,
                        roomsViewModel.fullRoomDetails.value!!.roomFeatureId,
                        roomsViewModel.fullRoomDetails.value!!.ownerId,
                        roomsViewModel.fullRoomDetails.value!!.roomName)
                    Toast.makeText(this, "Room Removed Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Cancel") {dialog,_ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.fabEditRoom.setOnClickListener {

            val transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.container_transform)

            TransitionManager.beginDelayedTransition(window.decorView as ViewGroup, transition)

            val intent = Intent(this, UpdateRoom::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this, binding.fabEditRoom, "shared_element_end_root")
            startActivity(intent, options.toBundle())
        }
    }
}