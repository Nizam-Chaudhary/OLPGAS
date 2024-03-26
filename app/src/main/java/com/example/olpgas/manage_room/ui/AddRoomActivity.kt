package com.example.olpgas.manage_room.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.olpgas.R
import com.example.olpgas.databinding.ActivityAddRoomBinding
import com.example.olpgas.manage_room.model.RoomDetails
import com.example.olpgas.manage_room.model.RoomMaster
import com.example.olpgas.manage_room.model.WorkState
import com.example.olpgas.manage_room.viewmodel.ManageRoomViewModel
import java.io.ByteArrayOutputStream

class AddRoomActivity : AppCompatActivity() {
    private val binding: ActivityAddRoomBinding by lazy {
        ActivityAddRoomBinding.inflate(layoutInflater)
    }

    private val manageRoomViewModel by lazy {
        ViewModelProvider(this)[ManageRoomViewModel::class.java]
    }

    private val images = mutableListOf<ByteArray>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.scrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpSpinner()

        binding.btnAddMainImage.setOnClickListener {
            getImages()
        }


        val id = if(intent.getIntExtra("id", 0) == 0) null else intent.getIntExtra("id", 0)
        val featureId = if(intent.getIntExtra("featureId", 0) == 0) null else intent.getIntExtra("featureId", 0)
        val oldRoomName = intent.getStringExtra("oldRoomName")
        addRoom(id, featureId, oldRoomName)
        if(id != null) {
            fetchAndSetData(id)
        }
    }

    private fun getImages() {
        getImagesPermission()
    }

    private fun picImages() {
        val intentGallery = Intent(Intent.ACTION_PICK)
        intentGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intentGallery, GALLERY_REQUEST_CODE)
    }

    private fun getImagesPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_EXTERNAL_STORAGE
                )
            } else {
                picImages()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    PERMISSION_REQUEST_MEDIA_IMAGES
                )
            } else {
                picImages()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_MEDIA_IMAGES && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            picImages()
        }
        if (requestCode == PERMISSION_REQUEST_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            picImages()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data ?: return // Handle cancelled selection
            try {
                val imageByteArray = getByteArrayFromImageUri(selectedImageUri)

                if(imageByteArray != null) {
                    images.add(imageByteArray)

                    Toast.makeText(this, "Image Selected Successfully", Toast.LENGTH_SHORT).show()
                    if(images.size >= 4) {
                        binding.btnAddMainImage.isEnabled = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exceptions (e.g., file not found, decoding error)
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setUpSpinner() {

        val stateAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.indian_states,
            android.R.layout.simple_spinner_item
        )
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerState.adapter = stateAdapter

        // Create array adapter for city spinner (initially empty)
        val cityAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item
        )
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCity.adapter = cityAdapter

        // Set up on item selected listener for state spinner
        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            selectedState.lowercase().replace(" ", "_") + "_cities",
            "array",
            packageName)
                val citiesArray = resources.getStringArray(citiesArrayId)
                cityAdapter.addAll(citiesArray.toList())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        val roomTypeAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,resources.getStringArray(R.array.room_type))
        binding.spinnerRoomType.adapter=roomTypeAdapter
    }

    private fun getByteArrayFromImageUri(imageUri: Uri): ByteArray? {
        val inputStream = contentResolver.openInputStream(imageUri) ?: return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        try {
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytesRead: Int

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }

            return byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                inputStream.close()
                byteArrayOutputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 4096 // Adjust as needed
        private const val GALLERY_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_EXTERNAL_STORAGE = 2
        private const val PERMISSION_REQUEST_MEDIA_IMAGES = 3
        private var allFieldValid = true
    }

    private fun addRoom(id: Int? = null, featureID: Int? = null, oldRoomName: String? = null) {
        binding.btnSubmit.setOnClickListener {
            val roomName = binding.inputRoomName.editText?.text.toString()
            val roomNumber = binding.inputRoomNumber.editText?.text.toString()
            val roomArea = binding.inputRoomArea.editText?.text.toString()
            val landmark = binding.inputLandmark.editText?.text.toString()
            val street = binding.inputStreetNumber.editText?.text.toString()
            val state = binding.spinnerState.selectedItem.toString()
            val city = binding.spinnerCity.selectedItem.toString()
            val roomType = binding.spinnerRoomType.selectedItem.toString()
            val shareableBy = binding.inputShareableBy.editText?.text.toString()
            val rentAmount = binding.inputRentAmount.editText?.text.toString()
            val deposit = binding.inputDepositAmount.editText?.text.toString()
            val about = binding.inputAbout.editText?.text.toString()

            val amenities = getAmenities()
            val suitableFor = getSuitableFor()

            if(roomName.isEmpty()) {
                allFieldValid = false
                binding.inputRoomName.error = " Please enter Room Name"
            } else {
                allFieldValid = true
                binding.inputRoomName.error = null
            }

            if(roomName.isEmpty()) {
                allFieldValid = false
                binding.inputRoomNumber.error = " Please enter Room Number"
            } else {
                allFieldValid = true
                binding.inputRoomNumber.error = null
            }

            if(roomArea.isEmpty()) {
                allFieldValid = false
                binding.inputRoomArea.error = " Please enter Room Area"
            } else {
                allFieldValid = true
                binding.inputRoomArea.error = null
            }

            if(landmark.isEmpty()) {
                allFieldValid = false
                binding.inputLandmark.error = " Please enter Landmark"
            } else {
                allFieldValid = true
                binding.inputLandmark.error = null
            }

            if(street.isEmpty()) {
                allFieldValid = false
                binding.inputStreetNumber.error = " Please enter Street"
            } else {
                allFieldValid = true
                binding.inputStreetNumber.error = null
            }

            if(city == "Select Your District") {
                allFieldValid = false
                Toast.makeText(this, "Please select district", Toast.LENGTH_SHORT).show()
            } else {
                allFieldValid = true
            }

            if(shareableBy.isEmpty()) {
                allFieldValid = false
                binding.inputShareableBy.error = " Please enter shareable by"
            } else {
                allFieldValid = true
                binding.inputShareableBy.error = null
            }

            if(rentAmount.isEmpty()) {
                allFieldValid = false
                binding.inputRentAmount.error = " Please enter Rent"
            } else {
                allFieldValid = true
                binding.inputRentAmount.error = null
            }

            if(deposit.isEmpty()) {
                allFieldValid = false
                binding.inputDepositAmount.error = " Please enter Deposit"
            } else {
                allFieldValid = true
                binding.inputDepositAmount.error = null
            }

            if(amenities.isEmpty()) {
                allFieldValid = false
                Toast.makeText(this, "Please select Amenities", Toast.LENGTH_SHORT).show()
            } else {
                allFieldValid = true
            }

            if(suitableFor.isEmpty()) {
                allFieldValid = false
                Toast.makeText(this, "Please select suitable for", Toast.LENGTH_SHORT).show()
            } else {
                allFieldValid = true
            }

            if(about.isEmpty()) {
                allFieldValid = false
                binding.inputAbout.error = " Please enter Description"
            } else {
                allFieldValid = true
                binding.inputAbout.error = null
            }

            if(images.isEmpty() && id == null && featureID == null) {
                allFieldValid = false
                Toast.makeText(this, "Please select images", Toast.LENGTH_SHORT).show()
            } else {
                allFieldValid = true
            }

            if(allFieldValid) {
                val roomDetails = RoomDetails(
                    id = featureID,
                    roomArea = roomArea.toInt(),
                    shareable = shareableBy.toInt(),
                    roomType = roomType,
                    deposit = deposit.toInt(),
                    rentAmount = rentAmount.toInt(),
                    description = about,
                    suitableFor = suitableFor,
                    features = amenities
                )

                val roomMaster = RoomMaster(
                    id = id,
                    roomName = roomName,
                    roomNumber = roomNumber,
                    streetNumber = street,
                    landMark = landmark,
                    city = city,
                    state = state,
                    roomFeatureId = featureID
                )

                if(allFieldValid && id != null && featureID != null && oldRoomName != null) {
                    manageRoomViewModel.updateRoomDetails(roomDetails, roomMaster, oldRoomName)
                } else if(allFieldValid){
                    manageRoomViewModel.uploadRoomDetails(roomDetails, roomMaster, images)
                }

                manageRoomViewModel.addRoomStatus.observe(this) {
                    when(it) {
                        WorkState.Loading -> {
                            binding.btnSubmit.text = "Uploading Room Details"
                        }
                        is WorkState.Success -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        is WorkState.Error -> {
                            binding.btnSubmit.text = "Submit"
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun getAmenities() : List<String> {
        val amenities = mutableListOf<String>()

        if(binding.chipAC.isChecked) {
            amenities.add("AC")
        }

        if(binding.chipWashingMachine.isChecked) {
            amenities.add("Washing Machine")
        }

        if(binding.chipWifi.isChecked) {
            amenities.add("Wi-Fi")
        }

        if(binding.chipFan.isChecked) {
            amenities.add("Fans")
        }

        if(binding.chipGeyser.isChecked) {
            amenities.add("Geyser")
        }

        if(binding.chipFurnished.isChecked) {
            amenities.add("Furnished")
        }

        return amenities
    }

    private fun getSuitableFor() : List<String> {
        val suitableFor = mutableListOf<String>()

        if(binding.chipStudents.isChecked) {
            suitableFor.add("Students")
        }

        if(binding.chipProfessionals.isChecked) {
            suitableFor.add("Professionals")
        }

        if(binding.chipCouples.isChecked) {
            suitableFor.add("Couples")
        }

        if(binding.chipSolo.isChecked) {
            suitableFor.add("Solo")
        }

        return suitableFor
    }

    private fun fetchAndSetData(id: Int) {
        manageRoomViewModel.fetchFullRoomDetails(id)
        manageRoomViewModel.fullRoomDetails.observe(this) {

            val stateAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.indian_states,
                android.R.layout.simple_spinner_item
            )
            val citiesArrayId = resources.getIdentifier(
                it.state.lowercase().replace(" ", "_") + "_cities",
                "array",
                packageName)
            val citiesArray = resources.getStringArray(citiesArrayId)
            val cityAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item
            )
            cityAdapter.addAll(citiesArray.toList())
            binding.spinnerCity.adapter = cityAdapter

            val roomTypeAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,resources.getStringArray(R.array.room_type))

            binding.inputRoomName.editText?.setText(it.roomName)
            binding.inputRoomNumber.editText?.setText(it.roomNumber)
            binding.inputLandmark.editText?.setText(it.landMark)
            binding.inputStreetNumber.editText?.setText(it.streetNumber)
            binding.spinnerState.setSelection(stateAdapter.getPosition(it.state))
            binding.spinnerCity.setSelection(cityAdapter.getPosition(it.city))
            binding.spinnerRoomType.setSelection(roomTypeAdapter.getPosition(it.roomType))
            binding.inputShareableBy.editText?.setText(it.shareable.toString())
            binding.inputRentAmount.editText?.setText(it.rentAmount.toString())
            binding.inputDepositAmount.editText?.setText(it.deposit.toString())
            binding.inputRoomArea.editText?.setText(it.roomArea.toString())
            binding.inputAbout.editText?.setText(it.description)

            if(it.features.contains("AC")) {
                binding.chipAC.isChecked = true
            }

            if(it.features.contains("Washing Machine")) {
                binding.chipWashingMachine.isChecked = true
            }

            if(it.features.contains("Wi-Fi")) {
                binding.chipWifi.isChecked = true
            }

            if(it.features.contains("Fans")) {
                binding.chipFan.isChecked = true
            }

            if(it.features.contains("Geyser")) {
                binding.chipGeyser.isChecked = true
            }

            if(it.features.contains("Furnished")) {
                binding.chipFurnished.isChecked = true
            }

            if(it.suitableFor.contains("Students")) {
                binding.chipStudents.isChecked = true
            }

            if(it.suitableFor.contains("Professionals")) {
                binding.chipProfessionals.isChecked = true
            }

            if(it.suitableFor.contains("Couples")) {
                binding.chipCouples.isChecked = true
            }

            if(it.suitableFor.contains("Solo")) {
                binding.chipSolo.isChecked = true
            }
        }
    }
}