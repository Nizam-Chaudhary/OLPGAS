package com.example.olpgas.profile.ui


import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.olpgas.R
import com.example.olpgas.databinding.ActivityUserAccountBinding
import com.example.olpgas.profile.viewmodel.UserProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout


class UserAccount : AppCompatActivity() {

    private var ageArray = arrayOf<String>()

    private val binding: ActivityUserAccountBinding by lazy {
        ActivityUserAccountBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserProfileViewModel by lazy {
        ViewModelProvider(this)[UserProfileViewModel::class.java]
    }

    private var profileImageByteArray: ByteArray? = null
    private var allFieldValid = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        setUserProfileData()
//        binding.uProfile.setOnClickListener {
//            if(binding.uProfile.text == "Save Profile") {
//                val user = performValidationsAndExtractValue()
//                if(allFieldValid) {
//                    userViewModel.saveUserProfile(user)
//                    if(profileImageByteArray != null) {
//                        userViewModel.saveUserProfilePicture(profileImageByteArray!!)
//                    }
//                }
//            }
//            if (binding.EditLayout.visibility == View.VISIBLE && allFieldValid) offEditProfileLayout() else onEditProfileLayout()
//        }
//
//        userViewModel.profileSaveStatus.observe(this) {
//            when(it) {
//                ProfileSaveStatus.Success -> {
//                    //setUserProfileData()
//                }
//                else -> {}
//            }
//        }


        val imageButtonIds = intArrayOf(
            binding.uName.id,
            binding.uGender.id,
            binding.uAge.id,
            binding.uPhoneNumber.id,
            binding.uAddressStreet.id,
            binding.uAddressCity.id,
            binding.uAddressState.id
        )

        for (imageButtonId in imageButtonIds) {
            findViewById<ImageButton>(imageButtonId).setOnClickListener { view ->
                when (view.id) {
                    binding.uName.id -> {
                        val view = View.inflate(this, R.layout.change_user_profiel_raw, null)


                        val userNameInput = view.findViewById<TextInputLayout>(R.id.changeUserProfileData)
                        userNameInput.hint = "Name"

                        val dialog = MaterialAlertDialogBuilder(this)
                            .setView(view)
                            .setTitle("Change Name")
                            .setPositiveButton("Save") {_, _ ->

                                val userName = userNameInput.editText?.text.toString()

                                Toast.makeText(this, userName, Toast.LENGTH_SHORT).show()

                            }
                            .setNegativeButton("Cancel") {_, _ ->

                            }
                            .show()
                    }

                    binding.uGender.id -> {
                        val view = View.inflate(this, R.layout.userprofile_gender_raw, null)

                        val genderMale = view.findViewById<RadioButton>(R.id.userProfileRadioMale)
                        val genderFemale =
                            view.findViewById<RadioButton>(R.id.userProfileRadioFemale)


                        val dialog =
                            MaterialAlertDialogBuilder(this).setView(view).setTitle("Change Gender")
                                .setPositiveButton("Save") { _, _ ->

                                    if (genderMale.isChecked) {
                                        Toast.makeText(this, "male", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show()
                                    }

                                }.setNegativeButton("Cancel") { _, _ ->

                                }.show()
                    }

                    binding.uAge.id -> {

                        val view = View.inflate(this, R.layout.userprofile_age_raw, null)

                        val ageSpinner = view.findViewById<Spinner>(R.id.userProfileAgeSpinner)

                        val numberArray = IntArray(61) { index -> index + 15 }
                        numberArray.map { it.toString() }.toTypedArray()
                        ageArray =
                            Array(numberArray.size) { index -> numberArray[index].toString() }


                        val ageSpinnerAdapter = ArrayAdapter(
                            this, android.R.layout.simple_dropdown_item_1line, ageArray
                        )

                        ageSpinner.adapter = ageSpinnerAdapter


                        //set age from here
                        ageSpinner.setSelection(0)


                        val dialog =
                            MaterialAlertDialogBuilder(this).setView(view).setTitle("Change Age")
                                .setPositiveButton("Save") { _, _ ->

                                    Toast.makeText(
                                        this, ageSpinner.selectedItem.toString(), Toast.LENGTH_SHORT
                                    ).show()

                                }.setNegativeButton("Cancel") { _, _ ->

                                }.show()
                    }

                    binding.uPhoneNumber.id -> {
                        val view = View.inflate(this, R.layout.change_user_profiel_raw, null)


                        val userPhoneNumberInput =
                            view.findViewById<TextInputLayout>(R.id.changeUserProfileData)
                        userPhoneNumberInput.hint = "Phone Number"

                        val dialog = MaterialAlertDialogBuilder(this)
                            .setView(view)
                            .setTitle("Change Phone Number")
                            .setPositiveButton("Save") { _, _ ->

                                val userName = userPhoneNumberInput.editText?.text.toString()


                            }
                            .setNegativeButton("Cancel") { _, _ ->

                            }
                            .show()
                    }

                    binding.uAddressStreet.id -> {
                        val view = View.inflate(this, R.layout.change_user_profiel_raw, null)


                        val userAddressStreetInput =
                            view.findViewById<TextInputLayout>(R.id.changeUserProfileData)
                        userAddressStreetInput.hint = "Name"

                        val dialog = MaterialAlertDialogBuilder(this)
                            .setView(view)
                            .setTitle("Change Name")
                            .setPositiveButton("Save") { _, _ ->

                                val userName = userAddressStreetInput.editText?.text.toString()


                            }
                            .setNegativeButton("Cancel") { _, _ ->

                            }
                            .show()
                    }

                    binding.uAddressCity.id -> {
                        val view = View.inflate(this, R.layout.change_user_profiel_raw, null)


                        val userAddressCityInput =
                            view.findViewById<TextInputLayout>(R.id.changeUserProfileData)
                        userAddressCityInput.hint = "Name"

                        val dialog = MaterialAlertDialogBuilder(this)
                            .setView(view)
                            .setTitle("Change Name")
                            .setPositiveButton("Save") { _, _ ->

                                val userName = userAddressCityInput.editText?.text.toString()


                            }
                            .setNegativeButton("Cancel") { _, _ ->

                            }
                            .show()
                    }

                    binding.uAddressState.id -> {
                        val view = View.inflate(this, R.layout.change_user_profiel_raw, null)


                        val userAddressStateInput =
                            view.findViewById<TextInputLayout>(R.id.changeUserProfileData)
                        userAddressStateInput.hint = "Name"

                        val dialog = MaterialAlertDialogBuilder(this)
                            .setView(view)
                            .setTitle("Change Name")
                            .setPositiveButton("Save") { _, _ ->

                                val userName = userAddressStateInput.editText?.text.toString()


                            }
                            .setNegativeButton("Cancel") { _, _ ->

                            }
                            .show()
                    }
                }
            }
        }



    }
//
//
//    private fun setUserProfileData() {
//        userViewModel.getUserProfileData()
//
//        userViewModel.userProfileData.observe(this) {user->
//            if(user.age == null) {
//                binding.ageSpinner.setSelection(0)
//            } else {
//                binding.ageSpinner.setSelection(user.age - 15)
//            }
//            binding.uName.editText?.setText(user.userName)
//            binding.uEmail.editText?.setText(user.email)
//            binding.uPhoneNumber.editText?.setText(user.phoneNumber)
//            binding.uAddressStreet.editText?.setText(user.streetNumber)
//            binding.uAddressCity.editText?.setText(user.city)
//            binding.uAddressState.editText?.setText(user.state)
//            binding.uGender.editText?.setText(user.gender)
//            if(user.gender == "Female") {
//                binding.radioFemale.isChecked = true
//            } else {
//                binding.radioMale.isChecked = true
//            }
//            binding.uAge.editText?.setText(user.age.toString())
//        }
//        userViewModel.userProfileImageByteArray.observe(this) {
//            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
//            binding.ivProfilePic.setImageBitmap(bitmap)
//        }
//    }
//
//
//    private fun onEditProfileLayout() {
//        binding.EditLayout.visibility = View.VISIBLE
//
//        binding.uAge.visibility = View.GONE
//        binding.uGender.visibility = View.GONE
//
//        if(allFieldValid) {
//            setUpSpinner()
//            setUserProfileData()
//        }
//
//        val constraintLayout = binding.uPhoneNumber.parent as ConstraintLayout
//        val layoutParams =
//            binding.uPhoneNumber.layoutParams as ConstraintLayout.LayoutParams
//        layoutParams.topToBottom = binding.EditLayout.id
//        constraintLayout.requestLayout()
//
//        binding.uProfile.text = "Save Profile"
//
//        binding.uName.editText?.isEnabled = true
//        binding.uAge.editText?.isEnabled = true
//        binding.uGender.editText?.isEnabled = true
//        binding.uPhoneNumber.editText?.isEnabled = true
//        binding.uAddressStreet.editText?.isEnabled = true
//        binding.uAddressCity.editText?.isEnabled = true
//        binding.uAddressState.editText?.isEnabled = true
//
//        binding.ivProfilePic.setOnClickListener {
//            getImagesPermission()
//        }
//    }
//
//    companion object {
//        private const val DEFAULT_BUFFER_SIZE = 4096 // Adjust as needed
//        private const val GALLERY_REQUEST_CODE = 1
//        private const val PERMISSION_REQUEST_EXTERNAL_STORAGE = 2
//        private const val PERMISSION_REQUEST_MEDIA_IMAGES = 3
//    }
//
//    private fun getImagesPermission() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    PERMISSION_REQUEST_EXTERNAL_STORAGE
//                )
//            } else {
//                picImages()
//            }
//        } else {
//            if (ContextCompat.checkSelfPermission(
//                    this, Manifest.permission.READ_MEDIA_IMAGES
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
//                    PERMISSION_REQUEST_MEDIA_IMAGES
//                )
//            } else {
//                picImages()
//            }
//        }
//    }
//
//    private fun picImages() {
//        val intentGallery = Intent(Intent.ACTION_PICK)
//        intentGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intentGallery, GALLERY_REQUEST_CODE)
//    }
//
//    private fun getByteArrayFromImageUri(imageUri: Uri): ByteArray? {
//        val inputStream = contentResolver.openInputStream(imageUri) ?: return null
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        try {
//            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
//            var bytesRead: Int
//
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead)
//            }
//
//            return byteArrayOutputStream.toByteArray()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return null
//        } finally {
//            try {
//                inputStream.close()
//                byteArrayOutputStream.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSION_REQUEST_MEDIA_IMAGES && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            picImages()
//        }
//        if (requestCode == PERMISSION_REQUEST_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            picImages()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
//            val selectedImageUri = data?.data ?: return // Handle cancelled selection
//            try {
//                val imageByteArray = getByteArrayFromImageUri(selectedImageUri)
//
//                if(imageByteArray != null) {
//                    profileImageByteArray = imageByteArray
//                    val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
//                    binding.ivProfilePic.setImageBitmap(bitmap)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                // Handle exceptions (e.g., file not found, decoding error)
//                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun offEditProfileLayout() {
//
//        binding.EditLayout.visibility = View.GONE
//
//        binding.uAge.visibility = View.VISIBLE
//        binding.uGender.visibility = View.VISIBLE
//
//
//        val constraintLayout = binding.uPhoneNumber.parent as ConstraintLayout
//        val layoutParams =
//            binding.uPhoneNumber.layoutParams as ConstraintLayout.LayoutParams
//        layoutParams.topToBottom = binding.uGender.id
//        constraintLayout.requestLayout()
//
//        binding.uProfile.text = "Edit Profile"
//
//
//        binding.uName.editText?.isEnabled = false
//        binding.uAge.editText?.isEnabled = false
//        binding.uGender.editText?.isEnabled = false
//        binding.uPhoneNumber.editText?.isEnabled = false
//        binding.uAddressStreet.editText?.isEnabled = false
//        binding.uAddressCity.editText?.isEnabled = false
//        binding.uAddressState.editText?.isEnabled = false
//
//        setUserProfileData()
//    }
//
//    private fun setUpSpinner() {
//        val numberArray = IntArray(61) { index -> index + 15 }
//        numberArray.map { it.toString() }.toTypedArray()
//        ageArray = Array(numberArray.size) { index -> numberArray[index].toString() }
//
//
//        val ageSpinnerAdapter =
//            ArrayAdapter(this, R.layout.simple_dropdown_item_1line, ageArray)
//
//        binding.ageSpinner.adapter = ageSpinnerAdapter
//
//
//        //set age from here
//        binding.ageSpinner.setSelection(0)
//    }
//
//    private fun performValidationsAndExtractValue() : User {
//        val userName = binding.uName.editText?.text.toString()
//        val email = binding.uEmail.editText?.text.toString()
//        val age = binding.ageSpinner.selectedItem.toString().toInt()
//        val gender = if(binding.radioMale.isChecked) "Male" else "Female"
//        val phoneNumber = binding.uPhoneNumber.editText?.text.toString()
//        val streetNumber = binding.uAddressStreet.editText?.text.toString()
//        val city = binding.uAddressCity.editText?.text.toString()
//        val state = binding.uAddressState.editText?.text.toString()
//
//        val user = User(
//            userName, email, age, gender, phoneNumber, streetNumber, city, state
//        )
//
//        if(userName.isEmpty()) {
//            binding.uName.error = "User name cannot be empty"
//            allFieldValid = false
//            return user
//        } else {
//            binding.uName.error = null
//            allFieldValid = true
//        }
//
//        if(phoneNumber.length != 10) {
//            binding.uPhoneNumber.error = "Invalid Phone Number"
//            allFieldValid = false
//            return user
//        } else {
//            binding.uPhoneNumber.error = null
//            allFieldValid = true
//        }
//
//        if(streetNumber.isEmpty()) {
//            binding.uAddressStreet.error = "Street number cannot be empty"
//            allFieldValid = false
//            return user
//        } else {
//            binding.uAddressStreet.error = null
//            allFieldValid = true
//        }
//
//        if(city.isEmpty()) {
//            binding.uAddressCity.error = "City number cannot be empty"
//            allFieldValid = false
//            return user
//        } else {
//            binding.uAddressCity.error = null
//            allFieldValid = true
//        }
//
//        if(state.isEmpty()) {
//            binding.uAddressState.error = "Statr number cannot be empty"
//            allFieldValid = false
//            return user
//        } else {
//            binding.uAddressState.error = null
//            allFieldValid = true
//        }
//
//        return user
//     }
}