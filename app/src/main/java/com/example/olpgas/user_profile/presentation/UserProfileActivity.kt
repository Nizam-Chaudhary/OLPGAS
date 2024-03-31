package com.example.olpgas.user_profile.presentation


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.olpgas.R
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.core.util.Error
import com.example.olpgas.databinding.ActivityUserProfileBinding
import com.example.olpgas.databinding.RawUpdateAddressBinding
import com.example.olpgas.databinding.RawUpdateAgeBinding
import com.example.olpgas.databinding.RawUpdateGenderBinding
import com.example.olpgas.databinding.RawUpdatePhoneNumberBinding
import com.example.olpgas.user_profile.domain.util.UserProfileValidationUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {

    private val binding: ActivityUserProfileBinding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    private val viewModel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUserProfile()

        onProfileImageClick()

        onGenderUpdateClick()

        onAgeUpdateClick()

        onPhoneNumberUpdateClick()

        onAddressUpdateClick()

        observeNetworkConnection()
    }

    private fun setUserProfile() {
        viewModel.onEvent(UserProfileEvent.SetUserProfile)

        viewModel.userProfilePictureState.observe(this) {
            if(it != null) {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                binding.ivProfilePic.setImageBitmap(bitmap)
            }
        }

        viewModel.userProfileState.observe(this) {
            binding.tvEmail.text = it.email
            binding.tvUserName.text = it.userName

            if(it.gender == null) {
                binding.tvGender.text = getString(R.string.not_set)
            } else {
                binding.tvGender.text = it.gender
            }

            if(it.age == 0) {
                binding.tvAge.text = getString(R.string.not_set)
            } else {
                binding.tvAge.text = it.age.toString()
            }

            if(it.phoneNumber == null) {
                binding.tvPhoneNumber.text = getString(R.string.not_set)
            } else {
                binding.tvPhoneNumber.text = it.phoneNumber
            }

            if(it.streetNumber == null) {
                binding.tvStreetNumber.text = getString(R.string.not_set)
            } else {
                binding.tvStreetNumber.text = it.streetNumber
            }

            if(it.city == null) {
                binding.tvCity.text = getString(R.string.not_set)
            } else {
                binding.tvCity.text = it.city
            }

            if(it.state == null) {
                binding.tvState.text = getString(R.string.not_set)
            } else {
                binding.tvState.text = it.state
            }
        }
    }

    private fun onProfileImageClick() {
        binding.ivProfilePic.setOnClickListener {
            getImagePermission()
        }
    }

    private fun getImagePermission() {
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
                pickImage()
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
                pickImage()
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
            pickImage()
        }
        if (requestCode == PERMISSION_REQUEST_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage()
        }
    }

    private fun pickImage() {
        val intentGallery = Intent(Intent.ACTION_PICK)
        intentGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intentGallery, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data ?: return // Handle cancelled selection
            try {
                val imageByteArray = getByteArrayFromImageUri(selectedImageUri)

                if (imageByteArray != null) {
                    val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                    binding.ivProfilePic.setImageBitmap(bitmap)

                    viewModel.onEvent(UserProfileEvent.UpdateProfileImage(imageByteArray))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        setUserProfile()
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

    private fun onGenderUpdateClick() {
        binding.uGender.setOnClickListener {
            updateGender()
        }

        binding.tvGender.setOnClickListener {
            updateGender()
        }
    }

    private fun updateGender() {
        val view = View.inflate(
            this,
            R.layout.raw_update_gender,
            null
        )

        val rawGenderBinding = RawUpdateGenderBinding.bind(view)

        val genderSelected = viewModel.userProfileState.value?.gender
        genderSelected?.let {
            when(genderSelected) {
                "Male" -> rawGenderBinding.rdMale.isChecked = true
                "Female"-> rawGenderBinding.rdFemale.isChecked = true
                else -> rawGenderBinding.rdOther.isChecked = true
            }
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Update Gender")
            .setView(view)
            .setPositiveButton("Update") {_,_ ->
                val checkedRadioButtonId = rawGenderBinding.rdgGender.checkedRadioButtonId
                val gender = when(checkedRadioButtonId) {
                    R.id.rd_male -> "Male"
                    R.id.rd_female -> "Female"
                    else -> "Other"
                }

                viewModel.onEvent(UserProfileEvent.UpdateGender(gender))
            }
            .setNegativeButton("Cancel") {_,_ -> }
            .show()
    }

    private fun onAgeUpdateClick() {
        binding.uAge.setOnClickListener {
            updateAge()
        }
        binding.tvAge.setOnClickListener {
            updateAge()
        }
    }

    private fun updateAge() {
        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
            val view = View.inflate(
                this,
                R.layout.raw_update_age,
                null
            )

            val rawUpdateAgeBinding = RawUpdateAgeBinding.bind(view)

            val userAge = viewModel.userProfileState.value?.age
            if(userAge != null && userAge != 0) {
                rawUpdateAgeBinding.txtFieldAge.editText?.setText(userAge.toString())
            }

            MaterialAlertDialogBuilder(this)
                .setTitle("Update Age")
                .setView(view)
                .setPositiveButton("Update") {_,_ ->
                    val age = if(rawUpdateAgeBinding.txtFieldAge.editText?.text.toString() == "") {
                        0
                    } else {
                        rawUpdateAgeBinding.txtFieldAge.editText?.text.toString().toInt()
                    }
                    val ageError = UserProfileValidationUtil.validateAge(age)
                    when(ageError) {
                        Error.EmptyField -> {
                            Toast.makeText(this, "Age can't be empty", Toast.LENGTH_SHORT).show()
                        }
                        Error.InValidAge -> {
                            Toast.makeText(this, "Invalid Age", Toast.LENGTH_SHORT).show()
                        }
                        null -> {
                            viewModel.onEvent(UserProfileEvent.UpdateAge(age))
                        } else -> {}
                    }

                }
                .setNegativeButton("Cancel") {_,_ -> }
                .show()
        } else {
            MaterialAlertDialogBuilder(this)
                .setTitle("Connection error")
                .setMessage("Please check your network connection")
                .setPositiveButton("dismiss") { _, _ -> }
                .show()
        }
    }

    private fun onPhoneNumberUpdateClick() {
        binding.uPhoneNumber.setOnClickListener {
            updatePhoneNumber()
        }
        binding.tvPhoneNumber.setOnClickListener {
            updatePhoneNumber()
        }
    }

    private fun updatePhoneNumber() {
        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
            val view = View.inflate(
                this,
                R.layout.raw_update_phone_number,
                null
            )

            val phoneNumberBinding = RawUpdatePhoneNumberBinding.bind(view)

            val userPhoneNumber = viewModel.userProfileState.value?.phoneNumber
            if(!userPhoneNumber.isNullOrEmpty()) {
                phoneNumberBinding.txtFiledPhoneNumber.editText?.setText(userPhoneNumber)
            }

            MaterialAlertDialogBuilder(this)
                .setTitle("Update Phone Number")
                .setView(view)
                .setPositiveButton("Update") {_,_ ->
                    val phoneNumber = phoneNumberBinding.txtFiledPhoneNumber.editText?.text.toString()
                    val phoneNumberError = UserProfileValidationUtil.validatePhoneNumber(phoneNumber)
                    when(phoneNumberError) {
                        Error.EmptyField -> {
                            Toast.makeText(this, "Phone Number can't be empty", Toast.LENGTH_SHORT).show()
                        }
                        Error.InvalidPhoneNumber -> {
                            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
                        }
                        null -> {
                            viewModel.onEvent(UserProfileEvent.UpdatePhoneNumber(phoneNumber))
                        } else -> {}
                    }
                }
                .setNegativeButton("Cancel") {_,_ -> }
                .show()
        } else {
            MaterialAlertDialogBuilder(this)
                .setTitle("Connection error")
                .setMessage("Please check your network connection")
                .setPositiveButton("dismiss") { _, _ -> }
                .show()
        }
    }

    private fun onAddressUpdateClick() {
        binding.uAddressStreet.setOnClickListener {
            updateAddress()
        }
        binding.tvStreetNumber.setOnClickListener {
            updateAddress()
        }
    }

    private fun updateAddress() {
        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
            val view = View.inflate(
                this,
                R.layout.raw_update_address,
                null
            )

            val addressBinding = RawUpdateAddressBinding.bind(view)

            val userStreetNumber = viewModel.userProfileState.value?.streetNumber
            val userCity = viewModel.userProfileState.value?.city
            val userState = viewModel.userProfileState.value?.state

            if(!userStreetNumber.isNullOrEmpty()) {
                addressBinding.txtFieldStreetNumber.editText?.setText(userStreetNumber)
            }

            if(!userCity.isNullOrEmpty()) {
                addressBinding.txtFieldCity.editText?.setText(userCity)
            }

            if(!userState.isNullOrEmpty()) {
                addressBinding.txtFieldState.editText?.setText(userState)
            }

            MaterialAlertDialogBuilder(this)
                .setTitle("Update Address")
                .setView(view)
                .setPositiveButton("Update") {_,_ ->
                    val streetNumber = addressBinding.txtFieldStreetNumber.editText?.text.toString()
                    val city = addressBinding.txtFieldCity.editText?.text.toString()
                    val state = addressBinding.txtFieldState.editText?.text.toString()

                    val streetNumberError = UserProfileValidationUtil.validateForEmpty(streetNumber)
                    val cityError = UserProfileValidationUtil.validateForEmpty(city)
                    val stateError = UserProfileValidationUtil.validateForEmpty(state)

                    var addressValid = true

                    when(streetNumberError) {
                        Error.EmptyField -> {
                            Toast.makeText(this, "Street Number can't be empty", Toast.LENGTH_SHORT).show()
                            addressValid = false
                        }
                        else -> {}
                    }
                    when(cityError) {
                        Error.EmptyField -> {
                            Toast.makeText(this, "City can't be empty", Toast.LENGTH_SHORT).show()
                            addressValid = false
                        }
                        else -> {}
                    }
                    when(stateError) {
                        Error.EmptyField -> {
                            Toast.makeText(this, "State can't be empty", Toast.LENGTH_SHORT).show()
                            addressValid = false
                        }
                        else -> {}
                    }

                    if(addressValid) {
                        viewModel.onEvent(UserProfileEvent.UpdateAddress(streetNumber, city, state))
                    }
                }
                .setNegativeButton("Cancel") {_,_ -> }
                .show()
        } else {
            MaterialAlertDialogBuilder(this)
                .setTitle("Connection error")
                .setMessage("Please check your network connection")
                .setPositiveButton("dismiss") { _, _ -> }
                .show()
        }
    }

    private fun observeNetworkConnection() {
        viewModel.connectionStatus.observe(this) {}
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 4096 // Adjust as needed
        private const val GALLERY_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_EXTERNAL_STORAGE = 2
        private const val PERMISSION_REQUEST_MEDIA_IMAGES = 3
        private const val TAG = "User Profile Activity"
    }
}