package com.example.olpgas.manage_room.presentation.post_room

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.olpgas.R
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.core.util.Error
import com.example.olpgas.databinding.ActivityPostRoomBinding
import com.example.olpgas.databinding.RawAddChipBinding
import com.example.olpgas.databinding.RawChipBinding
import com.example.olpgas.view_room_details.presentation.RoomImageRecyclerPagerAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class PostRoomActivity : AppCompatActivity() {
    private val binding: ActivityPostRoomBinding by lazy {
        ActivityPostRoomBinding.inflate(layoutInflater)
    }

    private val viewModel: PostRoomViewModel by viewModels()

    private val images = mutableListOf<ByteArray>()

    private val selectedImages = mutableListOf<String>()

    private lateinit var stateAdapter: ArrayAdapter<CharSequence>
    private lateinit var cityAdapter: ArrayAdapter<String>
    private lateinit var roomTypeAdapter: ArrayAdapter<String>
    private var city: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.coordinatorLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        viewModel.onEvent(PostRoomEvent.OnCreate)

        city = viewModel.cityState.value
        setUpSpinner()

        setState()

        onRoomNameTextChange()
        onRoomNumberTextChange()
        onStreetNumberTextChange()
        onLandMarkTextChange()
        onCityItemChange()
        onRentAmountTextChange()
        onShareableByTextChange()
        onDepositAmountTextChange()
        onRoomAreaTextChange()
        onRoomAboutTextChange()
        observePostRoomState()
        onPostRoomButtonClick()
        observeNetworkStatus()
        onAddAmenityClick()
        onAddSuitableForClick()
        setOnChipCheckChange()

        onAddImageButtonClick()
    }

    private fun picImages() {
        val intentGallery = Intent(Intent.ACTION_PICK)
        intentGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intentGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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
            // Handle single image selection
            if (data?.clipData == null) {
                val selectedImageUri = data?.data
                handleSelectedImage(selectedImageUri)
            } else {
                // Handle multiple image selection
                for (i in 0 until data.clipData!!.itemCount) {
                    val selectedImageUri = data.clipData!!.getItemAt(i).uri
                    handleSelectedImage(selectedImageUri)
                }
            }
        }
    }

    // Function to handle selected image
    private fun handleSelectedImage(selectedImageUri: Uri?) {
        try {
            val imageByteArray = selectedImageUri?.let { getByteArrayFromImageUri(it) }

            if (imageByteArray != null) {
                // Add selected image URI to the list
                selectedImages.add(selectedImageUri.toString())
                viewModel.onEvent(PostRoomEvent.AddedImage(imageByteArray))

                // Update ViewPager2 adapter
                val adapter = RoomImageRecyclerPagerAdapter(selectedImages, this)
                binding.postRoomViewPager.adapter = adapter

                // Show ViewPager2 and remove image button
                binding.postRoomViewPager.visibility = View.VISIBLE
                binding.postRemoveImage.visibility = View.VISIBLE

                // Disable add main image button if the maximum number of images is reached
                if (selectedImages.size >= 5) {
                    binding.btnAddMainImage.isEnabled = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpSpinner() {

        stateAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.indian_states,
            android.R.layout.simple_spinner_item
        )
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerState.adapter = stateAdapter
        viewModel.onEvent(PostRoomEvent.SelectedState(binding.spinnerState.selectedItem.toString()))

        // Create array adapter for city spinner (initially empty)
        cityAdapter = ArrayAdapter<String>(
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

                    viewModel.onEvent(PostRoomEvent.SelectedState(selectedState))

                    // Populate cities based on the selected state
                    val citiesArrayId = resources.getIdentifier(
            selectedState.lowercase().replace(" ", "_") + "_cities",
            "array",
            packageName)
                    val citiesArray = resources.getStringArray(citiesArrayId)
                    cityAdapter.addAll(citiesArray.toList())

                    binding.spinnerCity.setSelection(cityAdapter.getPosition(city))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        roomTypeAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,resources.getStringArray(R.array.room_type))
        roomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRoomType.adapter=roomTypeAdapter

        viewModel.onEvent(PostRoomEvent.SelectedRoomType(binding.spinnerRoomType.selectedItem.toString()))
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

    private fun setState() {
        val roomNameState = viewModel.roomNameState.value
        val roomNumberState = viewModel.roomNumberState.value
        val streetNumberState = viewModel.streetNumberState.value
        val landMarkState = viewModel.landMarkState.value
        val stateState = viewModel.stateState.value
        val roomTypeState = viewModel.roomTypeState.value
        val shareableByState = viewModel.shareableState.value
        val rentAmountState = viewModel.rentAmountState.value
        val depositState = viewModel.depositState.value
        val roomAreaState = viewModel.roomAreaState.value
        val featuresState = viewModel.featuresState.value
        val suitableForState = viewModel.suitableForState.value
        val descriptionState = viewModel.descriptionState.value

        binding.inputRoomName.editText?.setText(roomNameState?.text)
        binding.inputRoomNumber.editText?.setText(roomNumberState?.text)
        binding.inputStreetNumber.editText?.setText(streetNumberState?.text)
        binding.inputLandmark.editText?.setText(landMarkState?.text)
        binding.inputShareableBy.editText?.setText(shareableByState?.text)
        binding.inputRentAmount.editText?.setText(rentAmountState?.text)
        binding.inputDepositAmount.editText?.setText(depositState?.text)
        binding.inputRoomArea.editText?.setText(roomAreaState?.text)
        binding.inputAbout.editText?.setText(descriptionState?.text)

        binding.spinnerState.setSelection(stateAdapter.getPosition(stateState))
        binding.spinnerRoomType.setSelection(roomTypeAdapter.getPosition(roomTypeState))

        if (featuresState != null) {
            for(feature in featuresState) {
                if(feature.text != "AC" && feature.text != "Washing Machine" && feature.text != "Wi-Fi" &&
                    feature.text != "Fans" && feature.text != "Geyser" && feature.text != "Furnished") {
                    val rawChip = View.inflate(this, R.layout.raw_chip, null)
                    val rawChipBinding = RawChipBinding.bind(rawChip)
                    rawChipBinding.rawChip.text = feature.text
                    rawChipBinding.rawChip.tag = feature.text
                    rawChipBinding.rawChip.isChecked = feature.isChecked
                    binding.chipGroupAmenities.removeView(binding.addAmenitiesChip)
                    binding.chipGroupAmenities.addView(rawChipBinding.rawChip)
                    binding.chipGroupAmenities.addView(binding.addAmenitiesChip)
                }
            }
        }

        if (suitableForState != null) {
            for(item in suitableForState) {
                if(item.text != "Students" && item.text != "Professionals" &&
                    item.text != "Couples" && item.text != "Solo") {
                    val rawChip = View.inflate(this, R.layout.raw_chip, null)
                    val rawChipBinding = RawChipBinding.bind(rawChip)
                    rawChipBinding.rawChip.text = item.text
                    rawChipBinding.rawChip.tag = item.text
                    rawChipBinding.rawChip.isChecked = item.isChecked
                    binding.chipGroupSuitableFor.removeView(binding.addSuitableChip)
                    binding.chipGroupSuitableFor.addView(rawChipBinding.rawChip)
                    binding.chipGroupSuitableFor.addView(binding.addSuitableChip)
                }
            }
        }
    }

    private fun onRoomNameTextChange() {
        binding.inputRoomName.editText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputRoomName.error = null
                viewModel.onEvent(PostRoomEvent.EnteredRoomName(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onRoomNumberTextChange() {
        binding.inputRoomNumber.editText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputRoomNumber.error = null
                viewModel.onEvent(PostRoomEvent.EnteredRoomNumber(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onStreetNumberTextChange() {
        binding.inputStreetNumber.editText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputStreetNumber.error = null
                viewModel.onEvent(PostRoomEvent.EnteredStreetNumber(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onLandMarkTextChange() {
        binding.inputLandmark.editText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputLandmark.error = null
                viewModel.onEvent(PostRoomEvent.EnteredLandMark(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onCityItemChange() {
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                // Get selected city
                val selectedCity = parent?.getItemAtPosition(position).toString()
                viewModel.onEvent(PostRoomEvent.SelectedCity(selectedCity))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun onShareableByTextChange() {
        binding.inputShareableBy.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputShareableBy.error = null
                viewModel.onEvent(PostRoomEvent.EnteredShareableBy(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onRentAmountTextChange() {
        binding.inputRentAmount.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputRentAmount.error = null
                viewModel.onEvent(PostRoomEvent.EnteredRentAmount(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onDepositAmountTextChange() {
        binding.inputDepositAmount.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputDepositAmount.error = null
                viewModel.onEvent(PostRoomEvent.EnteredDeposit(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onRoomAreaTextChange() {
        binding.inputRoomArea.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputRoomArea.error = null
                viewModel.onEvent(PostRoomEvent.EnteredRoomArea(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }


    private fun onRoomAboutTextChange() {
        binding.inputAbout.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputAbout.error = null
                viewModel.onEvent(PostRoomEvent.EnteredDescription(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onPostRoomButtonClick() {
        binding.btnPostRoom.setOnClickListener {
            if (viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                viewModel.onEvent(PostRoomEvent.OnSubmit)

                checkValidationError()

            } else {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Connection error")
                    .setMessage("Please check your network connection")
                    .setPositiveButton("dismiss") { _, _ -> }
                    .show()
            }
        }

    }

    private fun observePostRoomState() {
        viewModel.postRoomState.observe(this) {
            val view = View.inflate(this, R.layout.raw_room_upload_progress, null)

            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle("Posting Room")
                .setCancelable(false)
                .setView(view)

            when(it) {
                is PostRoomState.Success -> {
                    onBackPressedDispatcher.onBackPressed()
                    finish()
                    overridePendingTransition(0, R.anim.slide_down)
                }
                is PostRoomState.Error -> {
                    Toast.makeText(this, "Error Uploading", Toast.LENGTH_SHORT).show()
                }
                is PostRoomState.IsLoading -> {
                    if(it.isLoading) {
                       dialog.show()
                    }
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_down)
    }

    private fun checkValidationError() {
        when(viewModel.roomNameState.value?.error) {
            Error.FieldEmpty -> binding.inputRoomName.error = "Room name can't be empty"
            else -> binding.inputRoomName.error = null
        }

        when(viewModel.roomNumberState.value?.error) {
            Error.FieldEmpty -> binding.inputRoomNumber.error = "Room number can't be empty"
            else -> binding.inputRoomNumber.error = null
        }

        when(viewModel.streetNumberState.value?.error) {
            Error.FieldEmpty -> binding.inputStreetNumber.error = "Street number can't be empty"
            else -> binding.inputStreetNumber.error = null
        }

        when(viewModel.landMarkState.value?.error) {
            Error.FieldEmpty -> binding.inputLandmark.error = "Landmark can't be empty"
            else -> binding.inputLandmark.error = null
        }

        when(viewModel.stateErrorState.value) {
            Error.NoItemSelected -> Toast.makeText(this, "Please select state", Toast.LENGTH_SHORT).show()
            else -> Unit
        }

        when(viewModel.cityErrorState.value) {
            Error.NoItemSelected -> Toast.makeText(this, "Please select city", Toast.LENGTH_SHORT).show()
            else -> Unit
        }

        when(viewModel.shareableState.value?.error) {
            Error.FieldEmpty -> binding.inputShareableBy.error = "Shareable can't be empty"
            else -> binding.inputShareableBy.error = null
        }

        when(viewModel.rentAmountState.value?.error) {
            Error.FieldEmpty -> binding.inputRentAmount.error = "Rent Amount can't be empty"
            else -> binding.inputRentAmount.error = null
        }

        when(viewModel.depositState.value?.error) {
            Error.FieldEmpty -> binding.inputDepositAmount.error = "Deposit can't be empty"
            else -> binding.inputDepositAmount.error = null
        }

        when(viewModel.roomAreaState.value?.error) {
            Error.FieldEmpty -> binding.inputRoomArea.error = "Room area can't be empty"
            else -> binding.inputRoomArea.error = null
        }

        when(viewModel.featuresErrorState.value) {
            Error.NoItemSelected -> Toast.makeText(this, "Please add amenity", Toast.LENGTH_SHORT).show()
            else -> Unit
        }

        when(viewModel.suitableForErrorState.value) {
            Error.NoItemSelected -> Toast.makeText(this, "Please add suitable for", Toast.LENGTH_SHORT).show()
            else -> Unit
        }

        when(viewModel.descriptionState.value?.error) {
            Error.FieldEmpty -> binding.inputAbout.error = "Description can't be empty"
            else -> binding.inputAbout.error = null
        }

        when(viewModel.imagesErrorState.value) {
            Error.NoItemSelected -> Toast.makeText(this, "Please add images", Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    private fun observeNetworkStatus() {
        viewModel.connectionStatus.observe(this) {
            Log.d("Network Status", it.toString())
        }
    }

    private fun onAddAmenityClick() {
        binding.addAmenitiesChip.setOnClickListener {
            val addChipView = View.inflate(this, R.layout.raw_add_chip, null)
            val addChipBinding = RawAddChipBinding.bind(addChipView)
            addChipBinding.txtFieldAddChip.hint = "Amenity"
            MaterialAlertDialogBuilder(this)
                .setTitle("Add Amenity")
                .setView(addChipBinding.root)
                .setPositiveButton("Add") {_,_ ->
                    val amenity = addChipBinding.txtFieldAddChip.editText?.text.toString()

                    val contains = viewModel.featuresState.value?.contains(ChipField(amenity,true))!! || viewModel.featuresState.value?.contains(ChipField(amenity,false))!!
                    if(contains) {
                        Toast.makeText(this, "Already Present", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    if(amenity.isNotEmpty()){
                        val rawChip = View.inflate(this, R.layout.raw_chip, null)
                        val rawChipBinding = RawChipBinding.bind(rawChip)
                        rawChipBinding.rawChip.text = amenity
                        rawChipBinding.rawChip.isChecked = true
                        rawChipBinding.rawChip.tag = amenity
                        binding.chipGroupAmenities.removeView(binding.addAmenitiesChip)
                        binding.chipGroupAmenities.addView(rawChipBinding.rawChip)
                        binding.chipGroupAmenities.addView(binding.addAmenitiesChip)
                        viewModel.onEvent(PostRoomEvent.AddedAmenity(ChipField(amenity, true)))

                        setOnChipCheckChange()
                    } else {
                        Toast.makeText(this, "Please enter amenity", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun onAddSuitableForClick() {
        binding.addSuitableChip.setOnClickListener {
            val addChipView = View.inflate(this, R.layout.raw_add_chip, null)
            val addChipBinding = RawAddChipBinding.bind(addChipView)
            addChipBinding.txtFieldAddChip.hint = "Suitable for"
            MaterialAlertDialogBuilder(this)
                .setTitle("Add Suitable For")
                .setView(addChipBinding.root)
                .setPositiveButton("Add") {_,_ ->
                    val suitableFor = addChipBinding.txtFieldAddChip.editText?.text.toString()
                    val contains = viewModel.featuresState.value?.contains(ChipField(suitableFor,true))!! || viewModel.featuresState.value?.contains(ChipField(suitableFor,false))!!
                    if(contains) {
                        Toast.makeText(this, "Already Present", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    if(suitableFor.isNotEmpty()){
                        val rawChip = View.inflate(this, R.layout.raw_chip, null)
                        val rawChipBinding = RawChipBinding.bind(rawChip)
                        rawChipBinding.rawChip.text = suitableFor
                        rawChipBinding.rawChip.isChecked = true
                        rawChipBinding.rawChip.tag = suitableFor
                        binding.chipGroupSuitableFor.removeView(binding.addSuitableChip)
                        binding.chipGroupSuitableFor.addView(rawChipBinding.rawChip)
                        binding.chipGroupSuitableFor.addView(binding.addSuitableChip)
                        viewModel.onEvent(PostRoomEvent.AddedSuitableFor(ChipField(suitableFor, true)))

                        setOnChipCheckChange()
                    } else {
                        Toast.makeText(this, "Please enter suitable for", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setOnChipCheckChange() {
        val featureState = viewModel.featuresState.value
        if (featureState != null) {
            for(i in featureState.indices){
                binding.chipGroupAmenities.findViewWithTag<Chip>(featureState[i].text).setOnCheckedChangeListener { _, isChecked ->
                    viewModel.onEvent(PostRoomEvent.FeatureCheckChange(ChipField(featureState[i].text, isChecked), i))
                }
            }
        }

        val suitableForState  = viewModel.suitableForState.value
        if (suitableForState != null) {
            for(i in suitableForState.indices){
                binding.chipGroupSuitableFor.findViewWithTag<Chip>(suitableForState[i].text).setOnCheckedChangeListener { _, isChecked ->
                    viewModel.onEvent(PostRoomEvent.SuitableForCheckChange(ChipField(suitableForState[i].text, isChecked), i))
                }
            }
        }
    }

    private fun onAddImageButtonClick() {
        binding.btnAddMainImage.setOnClickListener {
            getImagesPermission()
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 4096 // Adjust as needed
        private const val GALLERY_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_EXTERNAL_STORAGE = 2
        private const val PERMISSION_REQUEST_MEDIA_IMAGES = 3
    }
}