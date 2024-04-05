package com.example.olpgas.manage_room.presentation.update_room

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import com.example.olpgas.core.util.NetworkUnavailableDialog
import com.example.olpgas.databinding.ActivityUpdateRoomBinding
import com.example.olpgas.databinding.RawUpdateRoomInputFieldBinding
import com.example.olpgas.manage_room.presentation.AddRemoveImageViewPagerAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.platform.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class UpdateRoomActivity : AppCompatActivity(), AddRemoveImageViewPagerAdapter.OnItemClickListener {

    private val binding: ActivityUpdateRoomBinding by lazy {
        ActivityUpdateRoomBinding.inflate(layoutInflater)
    }

    private val id: Int by lazy {
        intent.getIntExtra("id",0)
    }

    private val viewModel: UpdateRoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.sharedElementEnterTransition = buildContainerTransform(true)
        window.sharedElementReturnTransition = buildContainerTransform(false)

        setData()

        observeNetworkConnection()

        onRemoveRoomBtnClick()

        val imageButtons = mutableListOf(
            binding.updateRoomNameBtn,
            binding.updateRoomAddressBtn,
            binding.updateRoomRentBtn,
            binding.updateRoomDepositBtn,
            binding.updateRoomSharableByBtn,
            binding.updateRoomTypeBtn,
            binding.updateRoomAreaBtn,
            binding.updateRoomAboutBtn
        )

        for (imageButton in imageButtons) {
            imageButton.setOnClickListener { view ->

                val singleTextFiled = View.inflate(this, R.layout.raw_update_room_input_field, null)
                val updateRoomDetailsBinding = RawUpdateRoomInputFieldBinding.bind(singleTextFiled)
                val updateRoomDetails = updateRoomDetailsBinding.updateRoomDataTxtField

                when (view.id) {
                    binding.updateRoomTypeBtn.id -> {
                        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
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

                            MaterialAlertDialogBuilder(this).setView(updateRoomTypeView)
                                .setTitle("Update Room Type").setPositiveButton("Update") { _, _ ->

                                    val roomType = updateRoomType.selectedItem.toString()
                                    viewModel.onEvent(UpdateRoomEvent.OnUpdateRoomType(roomType))

                                }.setNegativeButton("Cancel") { _, _ ->

                                }.show()
                        } else {
                            NetworkUnavailableDialog(this).networkUnavailable
                        }
                    }

                    binding.updateRoomNameBtn.id -> {

                        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                            updateRoomDetails.hint = "Room Name"
                            updateRoomDetails.editText?.inputType = InputType.TYPE_CLASS_TEXT

                            MaterialAlertDialogBuilder(this)
                                .setView(singleTextFiled)
                                .setTitle("Update Room Name")
                                .setPositiveButton("Update") { _, _ ->
                                    val roomName = updateRoomDetails.editText?.text.toString()
                                    if(roomName.isEmpty()) {
                                        Toast.makeText(this, "Room Name can't be empty", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.onEvent(UpdateRoomEvent.OnUpdateRoomName(roomName))
                                    }
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                        } else {
                            NetworkUnavailableDialog(this).networkUnavailable
                        }

                    }

                    binding.updateRoomRentBtn.id -> {
                        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                            updateRoomDetails.hint = "Room Rent"
                            updateRoomDetails.editText?.inputType = InputType.TYPE_CLASS_NUMBER

                            MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                                .setTitle("Update Room Rent")
                                .setPositiveButton("Update") { _, _ ->

                                    val roomRent = updateRoomDetails.editText?.text.toString()
                                    if(roomRent.isEmpty()) {
                                        Toast.makeText(this, "Rent can't be empty", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.onEvent(UpdateRoomEvent.OnUpdateRent(roomRent.toInt()))
                                    }
                                }.setNegativeButton("Cancel") { _, _ ->

                                }.show()
                        } else {
                            NetworkUnavailableDialog(this).networkUnavailable
                        }
                    }

                    binding.updateRoomDepositBtn.id -> {
                        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                            updateRoomDetails.hint = "Room Deposit"
                            updateRoomDetails.editText?.inputType = InputType.TYPE_CLASS_NUMBER

                            val dialog = MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                                .setTitle("Update Room Deposit").setPositiveButton("Update") { _, _ ->
                                    val deposit = updateRoomDetails.editText?.text.toString()
                                    if(deposit.isEmpty()) {
                                        Toast.makeText(this, "Deposit can't be empty", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.onEvent(UpdateRoomEvent.OnUpdateDeposit(deposit.toInt()))
                                    }
                                }.setNegativeButton("Cancel") { _, _ ->

                                }.show()
                        } else {
                            NetworkUnavailableDialog(this).networkUnavailable
                        }
                    }

                    binding.updateRoomSharableByBtn.id -> {
                        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                            updateRoomDetails.hint = "Room Sharable By"
                            updateRoomDetails.editText?.inputType = InputType.TYPE_CLASS_NUMBER

                            MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                                .setTitle("Update Room Sharable By").setPositiveButton("Update") { _, _ ->
                                    val shareableBy = updateRoomDetails.editText?.text.toString()
                                    if(shareableBy.isEmpty()) {
                                        Toast.makeText(this, "Shareable by can't be empty", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.onEvent(UpdateRoomEvent.OnUpdateShareableBy(shareableBy.toInt()))
                                    }
                                }.setNegativeButton("Cancel") { _, _ ->

                                }.show()
                        } else {
                            NetworkUnavailableDialog(this).networkUnavailable
                        }
                    }

                    binding.updateRoomAreaBtn.id -> {
                        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                            updateRoomDetails.hint = "Room Area"
                            updateRoomDetails.editText?.inputType = InputType.TYPE_CLASS_NUMBER

                            val dialog = MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                                .setTitle("Update Room Area").setPositiveButton("Update") { _, _ ->
                                    val roomArea = updateRoomDetails.editText?.text.toString()
                                    if(roomArea.isEmpty()) {
                                        Toast.makeText(this, "Room Area can't be empty", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.onEvent(UpdateRoomEvent.OnUpdateRoomArea(roomArea.toInt()))
                                    }
                                }.setNegativeButton("Cancel") { _, _ ->

                                }.show()
                        } else {
                            NetworkUnavailableDialog(this).networkUnavailable
                        }
                    }

                    binding.updateRoomAboutBtn.id -> {
                        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                            updateRoomDetails.hint = "Room About"
                            updateRoomDetails.editText?.inputType = InputType.TYPE_CLASS_TEXT

                            val dialog = MaterialAlertDialogBuilder(this).setView(singleTextFiled)
                                .setTitle("Update Room About").setPositiveButton("Update") { _, _ ->
                                    val about = updateRoomDetails.editText?.text.toString()
                                    if(about.isEmpty()) {
                                        Toast.makeText(this, "About can't be empty", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.onEvent(UpdateRoomEvent.OnUpdateDescription(about))
                                    }
                                }.setNegativeButton("Cancel") { _, _ ->

                                }.show()
                        } else {
                            NetworkUnavailableDialog(this).networkUnavailable
                        }
                    }

                    binding.updateRoomAddressBtn.id -> {
                        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
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
                                    val streetNumber = updateRoomStreet.editText?.text.toString()
                                    val landMark = updateRoomLandMark.editText?.text.toString()
                                    val state = updateRoomState.selectedItem.toString()
                                    val city = updateRoomCity.selectedItem.toString()

                                    if(streetNumber.isEmpty()) {
                                        Toast.makeText(this, "Street Number can't be empty", Toast.LENGTH_SHORT).show()
                                    } else if(landMark.isEmpty()) {
                                        Toast.makeText(this, "Landmark Number can't be empty", Toast.LENGTH_SHORT).show()
                                    } else if(state.isEmpty()) {
                                        Toast.makeText(this, "Please select state", Toast.LENGTH_SHORT).show()
                                    } else if(city.isEmpty() || city == "Select Your District") {
                                        Toast.makeText(this, "Please select city", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.onEvent(UpdateRoomEvent.OnUpdateAddress(
                                            streetNumber, landMark, state, city
                                        ))
                                    }

                                }.setNegativeButton("Cancel") { _, _ ->

                                }.show()
                        } else {
                            NetworkUnavailableDialog(this).networkUnavailable
                        }
                    }
                }
            }
        }
    }

    private fun setData() {
        viewModel.onEvent(UpdateRoomEvent.OnCreate(id))

        viewModel.allRoomDetailsState.observe(this) {
            binding.updateRoomNameTv.text = it.roomName
            binding.updateRoomAddressTV.text = "${it.streetNumber}, ${it.landMark}, ${it.city},${it.state}"

            binding.updateRoomRent.text = "${it.rentAmount}/-"
            binding.updateDepositRent.text = "${it.deposit}/-"
            binding.updateRoomSharableBy.text = "${it.shareable} People"
            binding.updateRoomType.text = it.roomType
            binding.updateRoomArea.text = "${it.roomArea} Sq. Ft."
            binding.updateRoomAboutTv.text = it.description

            binding.updateChipGroupAmenities.removeAllViews()
            binding.updateChipGroupSuitableFor.removeAllViews()
            for(amenity in it.features) {
                val chip = Chip(this)
                chip.text = amenity
                chip.tag = amenity
                binding.updateChipGroupSuitableFor.addView(chip)
            }
            for(suitableFor in it.suitableFor) {
                val chip = Chip(this)
                chip.text = suitableFor
                chip.tag = suitableFor
                binding.updateChipGroupAmenities.addView(chip)
            }
            binding.updateChipGroupAmenities.addView(binding.updateAddAmenities)
            binding.updateChipGroupSuitableFor.addView(binding.updateAddSuitableFor)

            val adapter = AddRemoveImageViewPagerAdapter(this, it.urls)
            adapter.onItemClickListener = this
            binding.updateRoomImageViewpager.adapter = adapter
        }
    }

    private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
        val transform = MaterialContainerTransform(this, entering)
        transform.addTarget(android.R.id.content)
        return transform
    }

    override fun onAddImageItemClick() {
        Toast.makeText(this, "Add Image", Toast.LENGTH_SHORT).show()
    }

    override fun onRemoveImageItemClick(position: Int) {
        Toast.makeText(this, "Remove Image", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "Image Selected Successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun onRemoveRoomBtnClick() {
        binding.removeRoomBtn.setOnClickListener {
            if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                val singleTextFiled = View.inflate(this, R.layout.raw_update_room_input_field, null)
                val updateRoomDetailsBinding = RawUpdateRoomInputFieldBinding.bind(singleTextFiled)
                val updateRoomDetails = updateRoomDetailsBinding.updateRoomDataTxtField

                updateRoomDetails.hint = "Type CONFIRM"
                MaterialAlertDialogBuilder(this)
                    .setTitle("Remove Room")
                    .setMessage("Type CONFIRM to remove room")
                    .setCancelable(false)
                    .setView(singleTextFiled)
                    .setPositiveButton("Remove") {_,_ ->
                        val input = updateRoomDetails.editText?.text.toString()
                        if(input == "CONFIRM") {
                            viewModel.onEvent(UpdateRoomEvent.OnRemoveRoom)
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                NetworkUnavailableDialog(this).networkUnavailable
            }
        }
    }

    private fun observeNetworkConnection() {
        viewModel.connectionStatus.observe(this) {
            Log.d("Network Connection", it.toString())
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 4096 // Adjust as needed
        private const val GALLERY_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_EXTERNAL_STORAGE = 2
        private const val PERMISSION_REQUEST_MEDIA_IMAGES = 3
    }
}