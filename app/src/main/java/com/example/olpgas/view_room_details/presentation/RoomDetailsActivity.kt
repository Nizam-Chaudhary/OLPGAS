package com.example.olpgas.view_room_details.presentation

import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgas.databinding.ActivityRoomDetailsBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs


@AndroidEntryPoint
class RoomDetailsActivity : AppCompatActivity() {

    private val binding: ActivityRoomDetailsBinding by lazy {
        ActivityRoomDetailsBinding.inflate(layoutInflater)
    }

    private val id: Int by lazy {
        intent.getIntExtra("id",0)
    }

    private val viewModel: RoomDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpData()

        setPageTransformerToViewPager()
    }

    private fun setUpData() {
        viewModel.onEvent(RoomDetailsEvent.OnCreate(id))

        viewModel.allRoomDetailsState.observe(this) {
            binding.detailedRoomName.text = it.roomName
            binding.detailedRoomLocation.text = "${it.streetNumber}, ${it.landMark}, ${it.city},${it.state}"

            binding.detailsRoomSuitableForGroup.removeAllViews()
            binding.detailedRoomAmenitiesChipGroup.removeAllViews()

            for(suitableFor in it.suitableFor) {
                val chip = Chip(this)
                chip.text = suitableFor
                binding.detailsRoomSuitableForGroup.addView(chip)
            }

            for(amenity in it.features) {
                val chip = Chip(this)
                chip.text = amenity
                binding.detailedRoomAmenitiesChipGroup.addView(chip)
            }

            binding.roomPrice.text = "${it.rentAmount}/-"
            binding.roomDepositTv.text = "${it.deposit}/-"
            binding.shareableTv.text = "${it.shareable} People"
            binding.roomAreaTv.text = it.roomType
            binding.roomAreaTv.text = "${it.roomArea} Sq. Ft."
            binding.roomDetailsAboutTV.text = it.description

            val adapter = RoomImageRecyclerPagerAdapter(it.urls, this)
            binding.detailedRoomImageViewpager.adapter = adapter
            binding.wormDotsIndicator.attachTo(binding.detailedRoomImageViewpager)
        }
    }

    private fun setPageTransformerToViewPager() {
        binding.detailedRoomImageViewpager.setPageTransformer { page, position ->
            val absPosition = abs(position)

            page.scaleY = 1 - absPosition / 2
        }
    }
}