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
import com.example.olpgas.databinding.ActivityUserProfileBinding
import com.example.olpgas.databinding.RawUpdateAgeBinding
import com.example.olpgas.databinding.RawUpdateGenderBinding
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
    }

    private fun setUserProfile() {
        viewModel.onEvent(UserProfileEvent.setUserProfile)

        viewModel.userProfilePictureState.observe(this) {
            if(it != null) {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                binding.ivProfilePic.setImageBitmap(bitmap)
            }
        }

        viewModel.userProfileState.observe(this) {
            binding.tvEmail.text = it.email
            binding.tvUserName.text = it.userName
            binding.tvGender.text = it.gender
            binding.tvAge.text = it.age.toString()
            binding.tvPhoneNumber.text = it.phoneNumber
            binding.tvStreetNumber.text = it.streetNumber
            binding.tvCity.text = it.city
            binding.tvState.text = it.state
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

                    viewModel.onEvent(UserProfileEvent.updateProfileImage(imageByteArray))
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

                viewModel.onEvent(UserProfileEvent.updateGender(gender))
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
        val view = View.inflate(
            this,
            R.layout.raw_update_age,
            null
        )

        val rawUpdateAgeBinding = RawUpdateAgeBinding.bind(view)

        val userAge = viewModel.userProfileState.value?.age
        userAge?.let {
            rawUpdateAgeBinding.txtFieldAge.editText?.setText(userAge.toString())
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Update Age")
            .setView(view)
            .setPositiveButton("Update") {_,_ ->

            }
            .setNegativeButton("Cancel") {_,_ -> }
            .show()
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 4096 // Adjust as needed
        private const val GALLERY_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_EXTERNAL_STORAGE = 2
        private const val PERMISSION_REQUEST_MEDIA_IMAGES = 3
        private const val TAG = "User Profile Activity"
    }
}