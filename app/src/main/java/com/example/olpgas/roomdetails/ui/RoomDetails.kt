package com.example.olpgas.roomdetails.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.olpgas.auth.data.network.SupabaseClient
import com.example.olpgas.databinding.ActivityRoomDetailsBinding
import com.example.olpgas.roomdetails.adapter.RoomsImageRecyclerPagerAdapter
import com.example.olpgas.roomdetails.viewmodel.RoomsViewModel
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

        val roomId = intent.getIntExtra("roomId",1)
        val ownerId = intent.getStringExtra("ownerId") ?: ""

        setData(roomId)
        setImagesAdapter(ownerId)
    }

    private fun setData(roomId: Int) {
        roomsViewModel.getFullRoomDetails(roomId)

        roomsViewModel.fullRoomDetails.observe(this) {roomDetails ->
            binding.roomDetailsName.text = roomDetails.roomName
            binding.roomDetailsLocation.text = "${roomDetails.streetNumber}, ${roomDetails.landMark}, ${roomDetails.city}, ${roomDetails.state}"
            binding.roomTypeTv.text = roomDetails.roomType
            binding.shareableTv.text = roomDetails.shareable.toString()
            //binding.sutableForTv.text = ""
            binding.roomAreaTv.text = roomDetails.roomArea.toString()
            binding.roomDetailsAbout.text = roomDetails.description
            binding.roomPrice.text = String.format(Locale.UK, "%,d", roomDetails.rentAmount) + "/-"
            binding.roomDepositTv.text = String.format(Locale.UK, "%,d", roomDetails.deposit) + "/-"
        }
    }

    private fun setImagesAdapter(ownerId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bucket = SupabaseClient.client.storage.from("RoomPics")
                val files = bucket.list(ownerId)

                val adapter = RoomsImageRecyclerPagerAdapter(
                    ownerId,
                    files,
                    this@RoomDetails
                )
                withContext(Dispatchers.Main) {
                    binding.displayImageViewpager.adapter = adapter
                }
            }catch (e: Exception) {
                Log.d("Room","Error: ${e.message}")
            }
        }
    }
}