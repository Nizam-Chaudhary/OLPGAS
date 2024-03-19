package com.example.olpgas.roomdetails.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgas.R
import com.example.olpgas.databinding.ActivityMyRoomBinding


class MyRoom : AppCompatActivity() {


    private val binding: ActivityMyRoomBinding by lazy {
        ActivityMyRoomBinding.inflate(layoutInflater)
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setUpSpinner()
    }




    private fun setUpSpinner() {

        val stateAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.indian_states,
            android.R.layout.simple_spinner_item
        )
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.myRoomAddressState.adapter = stateAdapter

        // Create array adapter for city spinner (initially empty)
        val cityAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item
        )
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.myRoomAddressCity.adapter = cityAdapter

        // Set up on item selected listener for state spinner
        binding.myRoomAddressState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                // Clear previous city selection
                cityAdapter.clear()

                // Get selected state
                val selectedState = parent?.getItemAtPosition(position).toString()

                // Populate cities based on the selected state
                val citiesArrayId = resources.getIdentifier(
                    selectedState.toLowerCase().replace(" ", "_") + "_cities",
                    "array",
                    packageName
                )
                val citiesArray = resources.getStringArray(citiesArrayId)
                cityAdapter.addAll(citiesArray.toList())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }



        val roomTypeAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,resources.getStringArray(R.array.room_type))
        binding.myRoomType.adapter=roomTypeAdapter

        val roomSuitableAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,resources.getStringArray(R.array.room_suitableFor))
        binding.myRoomSuitableFor.adapter=roomSuitableAdapter

    }
}