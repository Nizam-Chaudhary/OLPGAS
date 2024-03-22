package com.example.olpgas.roomdetails.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.olpgas.auth.data.network.SupabaseClient
import com.example.olpgas.databinding.ActivityRoomDetailsBinding
import com.example.olpgas.roomdetails.adapter.RoomsImageRecyclerPagerAdapter
import com.example.olpgas.roomdetails.viewmodel.RoomsViewModel
import com.google.android.material.chip.Chip
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val roomId = intent.getIntExtra("roomId",1)
        val ownerId = intent.getStringExtra("ownerId") ?: ""
        val roomName = intent.getStringExtra("roomName") ?: ""

        setData(roomId)
        setImagesAdapter(ownerId, roomName)
    }

    private fun setData(roomId: Int) {
        roomsViewModel.getFullRoomDetails(roomId)

        roomsViewModel.fullRoomDetails.observe(this) {roomDetails ->
            binding.detailedRoomName.text = roomDetails.roomName
            binding.detailedRoomLocation.text = "${roomDetails.streetNumber}, ${roomDetails.landMark}, ${roomDetails.city}, ${roomDetails.state}"
            binding.roomTypeTv.text = roomDetails.roomType
            binding.shareableTv.text = roomDetails.shareable.toString()
            binding.roomAreaTv.text = roomDetails.roomArea.toString() + " Sq. Ft."
            binding.detailedRoomAbout.text = roomDetails.description
            binding.roomPrice.text = String.format(Locale.UK, "%,d", roomDetails.rentAmount) + "/-"
            binding.roomDepositTv.text = String.format(Locale.UK, "%,d", roomDetails.deposit) + "/-"
            for(i in roomDetails.suitableFor.indices) {
                val chip = Chip(this)
                val suitableFor = roomDetails.suitableFor[i].toString()
                chip.text = suitableFor.substring(1..<suitableFor.length-1)
                chip.isChecked = true
                chip.isEnabled = true
                binding.detailsRoomSuitableForGroup.addView(chip)

            }
            for(i in roomDetails.features.indices) {
                val chip = Chip(this)
                val feature = roomDetails.features[i].toString()
                chip.text = feature.substring(1..<feature.length-1)
                chip.isChecked = true
                chip.isEnabled = true
                binding.detailedRoomAmenitiesChipGroup.addView(chip)

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
}