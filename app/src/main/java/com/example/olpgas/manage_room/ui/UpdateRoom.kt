package com.example.olpgas.manage_room.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.olpgas.R
import com.example.olpgas.databinding.ActivityUpdateRoomBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.platform.MaterialContainerTransform

class UpdateRoom : AppCompatActivity() {

    private val binding: ActivityUpdateRoomBinding by lazy {
        ActivityUpdateRoomBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        window.sharedElementEnterTransition = buildContainerTransform(true)
        window.sharedElementReturnTransition = buildContainerTransform(false)


        val imageButtonIds = intArrayOf(
            binding.updateRoomNameBtn.id,
            binding.updateRoomAddressBtn.id,
            binding.updateRoomRentBtn.id,
            binding.updateRoomDepositBtn.id,
            binding.updateRoomSharableByBtn.id,
            binding.updateRoomTypeBtn.id,
            binding.updateRoomAreaBtn.id,
            binding.updateRoomAboutBtn.id
        )

        for (imageButtonId in imageButtonIds) {
            findViewById<ImageButton>(imageButtonId).setOnClickListener { view ->

                val singleTextFiled = View.inflate(this, R.layout.raw_change_user_profile, null)
                val updateRoomDetails =
                    singleTextFiled.findViewById<TextInputLayout>(R.id.changeUserProfileData)

                when (view.id) {

                    binding.updateRoomTypeBtn.id -> {

                        val updateRoomTypeView =
                            View.inflate(this, R.layout.raw_update_room_type, null)

                        val updateRoomType =
                            updateRoomTypeView.findViewById<Spinner>(R.id.update_spinner_room_type)

                        val roomTypeAdapter = ArrayAdapter(
                            this,
                            android.R.layout.simple_spinner_dropdown_item,
                            resources.getStringArray(R.array.room_type)
                        )
                        updateRoomType.adapter = roomTypeAdapter

                        val dialog = MaterialAlertDialogBuilder(this).setView(updateRoomTypeView)
                            .setTitle("Change Room Type").setPositiveButton("Save") { _, _ ->

                                val roomName = updateRoomDetails.editText?.text.toString()

                            }.setNegativeButton("Cancel") { _, _ ->

                            }.show()
                    }


                    binding.updateRoomNameBtn.id -> {

                        updateRoomDetails.hint = "Room Name"

                        val dialog = MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                            .setTitle("Change Room Name").setPositiveButton("Save") { _, _ ->

                                val roomName = updateRoomDetails.editText?.text.toString()

                            }.setNegativeButton("Cancel") { _, _ ->

                            }.show()

                    }

                    binding.updateRoomRentBtn.id -> {
                        updateRoomDetails.hint = "Room Rent"

                        val dialog = MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                            .setTitle("Change Room Rent").setPositiveButton("Save") { _, _ ->

                                val roomName = updateRoomDetails.editText?.text.toString()

                            }.setNegativeButton("Cancel") { _, _ ->

                            }.show()
                    }

                    binding.updateRoomDepositBtn.id -> {
                        updateRoomDetails.hint = "Room Deposit"

                        val dialog = MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                            .setTitle("Change Room Deposit").setPositiveButton("Save") { _, _ ->

                                val roomName = updateRoomDetails.editText?.text.toString()

                            }.setNegativeButton("Cancel") { _, _ ->

                            }.show()
                    }

                    binding.updateRoomSharableByBtn.id -> {
                        updateRoomDetails.hint = "Room Sharable By"

                        val dialog = MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                            .setTitle("Change Room Sharable By").setPositiveButton("Save") { _, _ ->

                                val roomName = updateRoomDetails.editText?.text.toString()

                            }.setNegativeButton("Cancel") { _, _ ->

                            }.show()
                    }

                    binding.updateRoomTypeBtn.id -> {

                    }

                    binding.updateRoomAreaBtn.id -> {
                        updateRoomDetails.hint = "Room Area"

                        val dialog = MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                            .setTitle("Change Room Area").setPositiveButton("Save") { _, _ ->

                                val roomName = updateRoomDetails.editText?.text.toString()

                            }.setNegativeButton("Cancel") { _, _ ->

                            }.show()
                    }

                    binding.updateRoomAboutBtn.id -> {
                        updateRoomDetails.hint = "Room About"

                        val dialog = MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                            .setTitle("Change Room About").setPositiveButton("Save") { _, _ ->

                                val roomName = updateRoomDetails.editText?.text.toString()

                            }.setNegativeButton("Cancel") { _, _ ->

                            }.show()
                    }

                    binding.updateRoomAddressBtn.id -> {
                        val updateRoomAddressView =
                            View.inflate(this, R.layout.raw_update_room_address, null)
                        val updateRoomLandMark =
                            updateRoomAddressView.findViewById<TextInputLayout>(R.id.update_input_landmark)

                        val updateRoomStreet =
                            updateRoomAddressView.findViewById<TextInputLayout>(R.id.update_input_street_number)

                        val updateRoomState =
                            updateRoomAddressView.findViewById<Spinner>(R.id.update_spinner_state)

                        val updateRoomCity =
                            updateRoomAddressView.findViewById<Spinner>(R.id.update_spinner_city)


                        val stateAdapter = ArrayAdapter.createFromResource(
                            this, R.array.indian_states, android.R.layout.simple_spinner_item
                        )
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        updateRoomState.adapter = stateAdapter

                        // Create array adapter for city spinner (initially empty)
                        val cityAdapter = ArrayAdapter<String>(
                            this, android.R.layout.simple_spinner_item
                        )
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        updateRoomCity.adapter = cityAdapter

                        // Set up on item selected listener for state spinner
                        updateRoomState.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: android.view.View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    // Clear previous city selection
                                    cityAdapter.clear()

                                    // Get selected state
                                    val selectedState =
                                        parent?.getItemAtPosition(position).toString()

                                    // Populate cities based on the selected state
                                    val citiesArrayId = resources.getIdentifier(
                                        selectedState.lowercase().replace(" ", "_") + "_cities",
                                        "array",
                                        packageName
                                    )
                                    val citiesArray = resources.getStringArray(citiesArrayId)
                                    cityAdapter.addAll(citiesArray.toList())
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                }
                            }

                        val dialog = MaterialAlertDialogBuilder(this).setView(updateRoomAddressView)
                            .setTitle("Change Room Address").setPositiveButton("Save") { _, _ ->

                                val roomName = updateRoomDetails.editText?.text.toString()

                            }.setNegativeButton("Cancel") { _, _ ->

                            }.show()


                    }


                }
            }
        }


    }



    private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
        val transform = MaterialContainerTransform(this, entering)
        transform.addTarget(android.R.id.content)
        return transform
    }
}