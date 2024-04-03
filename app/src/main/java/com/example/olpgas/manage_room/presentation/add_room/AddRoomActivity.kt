package com.example.olpgas.manage_room.presentation.add_room

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
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
import com.example.olpgas.auth.presentation.login_activity.LoginEvent
import com.example.olpgas.databinding.ActivityAddRoomBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class AddRoomActivity : AppCompatActivity() {
    private val binding: ActivityAddRoomBinding by lazy {
        ActivityAddRoomBinding.inflate(layoutInflater)
    }

    private val viewModel: AddRoomViewModel by viewModels()

    private val images = mutableListOf<ByteArray>()

    private lateinit var stateAdapter: ArrayAdapter<CharSequence>
    private lateinit var cityAdapter: ArrayAdapter<String>
    private lateinit var roomTypeAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.coordinatorLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpSpinner()

        setState()

        onRoomNameTextChange()
        onRentAmountTextChange()
        onDepositAmountTextChange()
        onRoomAreaTextChange()
        onRoomAboutTextChange()
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
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
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
        roomTypeAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,resources.getStringArray(R.array.room_type))
        roomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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

    private fun setState() {
        val roomNameState = viewModel.roomNameState.value
        val roomNumberState = viewModel.roomNumberState.value
        val streetNumberState = viewModel.streetNumberState.value
        val landMarkState = viewModel.landMarkState.value
        val cityState = viewModel.cityState.value
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

        setUpSpinner()
        binding.spinnerState.setSelection(stateAdapter.getPosition(stateState))
        binding.spinnerCity.setSelection(cityAdapter.getPosition(cityState))
        binding.spinnerRoomType.setSelection(roomTypeAdapter.getPosition(roomTypeState))
    }

    private fun onRoomNameTextChange() {
        binding.inputRoomName.editText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputRoomName.error = null
                viewModel.onEvent(AddRoomEvent.EnteredRoomName(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }


    private fun onRentAmountTextChange() {
        binding.inputRentAmount.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputRentAmount.error = null
                viewModel.onEvent(AddRoomEvent.EnteredRentAmount(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onDepositAmountTextChange() {
        binding.inputDepositAmount.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputDepositAmount.error = null
                viewModel.onEvent(AddRoomEvent.EnteredDeposit(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onRoomAreaTextChange() {
        binding.inputRoomArea.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputRoomArea.error = null
                viewModel.onEvent(AddRoomEvent.EnteredRoomArea(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }


    private fun onRoomAboutTextChange() {
        binding.inputAbout.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.inputAbout.error = null
                viewModel.onEvent(AddRoomEvent.EnteredDescription(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }




    companion object {
        private const val DEFAULT_BUFFER_SIZE = 4096 // Adjust as needed
        private const val GALLERY_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_EXTERNAL_STORAGE = 2
        private const val PERMISSION_REQUEST_MEDIA_IMAGES = 3
    }
}