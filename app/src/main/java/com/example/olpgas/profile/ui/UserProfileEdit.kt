package com.example.olpgas.profile.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.olpgas.databinding.ActivityUserProfileEditBinding
import com.example.olpgas.profile.data.model.ProfileSaveStatus
import com.example.olpgas.profile.data.model.User
import com.example.olpgas.profile.viewmodel.UserProfileViewModel

class UserProfileEdit : AppCompatActivity() {

    private var ageArray = arrayOf<String>()

    private val binding: ActivityUserProfileEditBinding by lazy {
        ActivityUserProfileEditBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserProfileViewModel by lazy {
        ViewModelProvider(this)[UserProfileViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUserProfileData(this)

        setUpSpinner()

        userViewModel.profileSaveStatus.observe(this) { profileSaveStatus ->
            when(profileSaveStatus) {
                ProfileSaveStatus.Success -> {
                    finish()
                }
                is ProfileSaveStatus.Error -> {
                    Toast.makeText(this, "Error: ${profileSaveStatus.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.uSave.setOnClickListener {

            val selectedGender: Int = binding.genderRadioGroup.checkedRadioButtonId
            val radioButton: RadioButton = findViewById(selectedGender)

            val gender = radioButton.text.toString()

            val name = binding.uNameEdit.text.toString()
            val age = binding.ageSpinner.selectedItem.toString().toInt()
            val phoneNumber = binding.uPhoneNumberEdit.text.toString()
            val streetNumber = binding.uAddressStreetEdit.text.toString()
            val city = binding.uAddressCityEdit.text.toString()
            val state = binding.uAddressStateEdit.text.toString()

            val user = User(
                userName = name,
                age = age,
                gender = gender,
                phoneNumber = phoneNumber,
                streetNumber = streetNumber,
                city = city,
                state = state
            )

            userViewModel.saveUserProfile(user)
        }

    }

    private fun setUpSpinner() {
        val numberArray = IntArray(61) { index -> index + 15 }
        numberArray.map { it.toString() }.toTypedArray()
        ageArray = Array(numberArray.size) { index -> numberArray[index].toString() }


        val ageSpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ageArray)

        binding.ageSpinner.adapter = ageSpinnerAdapter


        //set age from here
        binding.ageSpinner.setSelection(0)
    }


    override fun onStop() {
        super.onStop()
        finish()
    }

    private fun setUserProfileData(lifecycleOwner: LifecycleOwner) {
        userViewModel.getUserProfileData()

        userViewModel.userProfileData.observe(lifecycleOwner) {user->
            binding.uNameEdit.setText(user.userName)
            binding.uEmailEdit.setText(user.email)
            binding.uPhoneNumberEdit.setText(user.phoneNumber)
            binding.uAddressStreetEdit.setText(user.streetNumber)
            binding.uAddressCityEdit.setText(user.city)
            binding.uAddressStateEdit.setText(user.state)

            if(user.gender == "Female") {
                binding.radioFemale.isChecked = true
            } else {
                binding.radioMale.isChecked = true
            }

            val age = user.age ?: 15
            binding.ageSpinner.setSelection(age-15)
        }
    }

}