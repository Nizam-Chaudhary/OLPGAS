package com.example.olpgas.profile.ui


import android.R
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.olpgas.databinding.ActivityUserAccountBinding
import com.example.olpgas.profile.data.model.User
import com.example.olpgas.profile.viewmodel.UserProfileViewModel


class UserAccount : AppCompatActivity() {

    private var ageArray = arrayOf<String>()

    private val binding: ActivityUserAccountBinding by lazy {
        ActivityUserAccountBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserProfileViewModel by lazy {
        ViewModelProvider(this)[UserProfileViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.uProfile.setOnClickListener {
            if(binding.uProfile.text == "Save Profile") {
                val user = User(
                    userName = binding.uName.editText?.text.toString(),
                    email = binding.uEmail.editText?.text.toString(),
                    age = binding.ageSpinner.selectedItem.toString().toInt(),
                    gender = if(binding.radioMale.isChecked) "Male" else "Female",
                    phoneNumber = binding.uPhoneNumber.editText?.text.toString(),
                    streetNumber = binding.uAddressStreet.editText?.text.toString(),
                    city = binding.uAddressCity.editText?.text.toString(),
                    state = binding.uAddressState.editText?.text.toString()
                )
                userViewModel.saveUserProfile(user)
            }
            setUserProfileData()
            if (binding.EditLayout.visibility == View.VISIBLE) offEditProfileLayout() else onEditProfileLayout()
        }

        setUserProfileData()
    }


    private fun setUserProfileData() {
        userViewModel.getUserProfileData()

        userViewModel.userProfileData.observe(this) {user->
            if(user.age == null) {
                binding.ageSpinner.setSelection(0)
            } else {
                binding.ageSpinner.setSelection(user.age - 15)
            }
            binding.uName.editText?.setText(user.userName)
            binding.uEmail.editText?.setText(user.email)
            binding.uPhoneNumber.editText?.setText(user.phoneNumber)
            binding.uAddressStreet.editText?.setText(user.streetNumber)
            binding.uAddressCity.editText?.setText(user.city)
            binding.uAddressState.editText?.setText(user.state)
            binding.uGender.editText?.setText(user.gender)
            if(user.gender == "Female") {
                binding.radioFemale.isChecked = true
            } else {
                binding.radioMale.isChecked = true
            }
            binding.uAge.editText?.setText(user.age.toString())
        }
    }


    private fun onEditProfileLayout() {
        binding.EditLayout.visibility = View.VISIBLE

        binding.uAge.visibility = View.GONE
        binding.uGender.visibility = View.GONE

        setUpSpinner()

        val constraintLayout = binding.uPhoneNumber.parent as ConstraintLayout
        val layoutParams =
            binding.uPhoneNumber.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topToBottom = binding.EditLayout.id
        constraintLayout.requestLayout()

        binding.uProfile.text = "Save Profile"

        binding.uName.editText?.isEnabled = true
        binding.uAge.editText?.isEnabled = true
        binding.uGender.editText?.isEnabled = true
        binding.uPhoneNumber.editText?.isEnabled = true
        binding.uAddressStreet.editText?.isEnabled = true
        binding.uAddressCity.editText?.isEnabled = true
        binding.uAddressState.editText?.isEnabled = true
    }

    private fun offEditProfileLayout() {

        binding.EditLayout.visibility = View.GONE

        binding.uAge.visibility = View.VISIBLE
        binding.uGender.visibility = View.VISIBLE


        val constraintLayout = binding.uPhoneNumber.parent as ConstraintLayout
        val layoutParams =
            binding.uPhoneNumber.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topToBottom = binding.uGender.id
        constraintLayout.requestLayout()

        binding.uProfile.text = "Edit Profile"


        binding.uName.editText?.isEnabled = false
        binding.uAge.editText?.isEnabled = false
        binding.uGender.editText?.isEnabled = false
        binding.uPhoneNumber.editText?.isEnabled = false
        binding.uAddressStreet.editText?.isEnabled = false
        binding.uAddressCity.editText?.isEnabled = false
        binding.uAddressState.editText?.isEnabled = false
    }

    private fun setUpSpinner() {
        val numberArray = IntArray(61) { index -> index + 15 }
        numberArray.map { it.toString() }.toTypedArray()
        ageArray = Array(numberArray.size) { index -> numberArray[index].toString() }


        val ageSpinnerAdapter =
            ArrayAdapter(this, R.layout.simple_dropdown_item_1line, ageArray)

        binding.ageSpinner.adapter = ageSpinnerAdapter


        //set age from here
        binding.ageSpinner.setSelection(0)
    }
}