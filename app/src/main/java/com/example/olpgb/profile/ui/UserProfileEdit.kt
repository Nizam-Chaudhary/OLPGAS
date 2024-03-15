package com.example.olpgb.profile.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgb.databinding.ActivityUserProfileEditBinding

class UserProfileEdit : AppCompatActivity() {

    private var ageArray = arrayOf<String>()
    private var email: String = ""
    private var name: String = ""
    private var age: Int = 15
    private var gender: String = ""
    private var phoneNumber: String = ""
    private var addressStreet: String = ""
    private var addressCity: String = ""
    private var addressState: String = ""

    private val binding: ActivityUserProfileEditBinding by lazy {
        ActivityUserProfileEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fillOldData()
        setUpSpinner()


        binding.uSave.setOnClickListener {

            val selectedGender: Int = binding.genderRadioGroup.checkedRadioButtonId
            val radioButton: RadioButton = findViewById(selectedGender)

            gender = radioButton.text.toString()
            finish()
        }

    }

    private fun fillOldData() {
        binding.uEmailEdit.setText(email)
        binding.uNameEdit.setText(name)
        binding.uPhoneNumberEdit.setText(phoneNumber)
        binding.uAddressStreetEdit.setText(addressStreet)
        binding.uAddressCityEdit.setText(addressCity)
        binding.uAddressStateEdit.setText(addressState)

        if (gender == "") {
            binding.radioMale.isChecked = true
        } else {
            binding.radioFemale.isChecked = true
        }

    }

    private fun setUpSpinner() {
        val numberArray = IntArray(46) { index -> index + 15 }
        numberArray.map { it.toString() }.toTypedArray()
        ageArray = Array(numberArray.size) { index -> numberArray[index].toString() }


        val ageSpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ageArray)

        binding.ageSpinner.adapter = ageSpinnerAdapter


        //set age from here
        binding.ageSpinner.setSelection(ageSpinnerAdapter.getPosition(age.toString()))
    }


    override fun onStop() {
        super.onStop()
        finish()
    }

}