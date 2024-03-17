package com.example.olpgas.profile.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.olpgas.databinding.ActivityUserAccountBinding
import com.example.olpgas.profile.viewmodel.UserProfileViewModel

class UserAccount : AppCompatActivity() {


    private val binding: ActivityUserAccountBinding by lazy {
        ActivityUserAccountBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserProfileViewModel by lazy {
        ViewModelProvider(this)[UserProfileViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.uEdit.setOnClickListener {
            startActivity(Intent(this@UserAccount, UserProfileEdit::class.java))
        }

    }

    private fun setUserProfileData(lifecycleOwner: LifecycleOwner) {
        userViewModel.getUserProfileData()

        userViewModel.userProfileData.observe(lifecycleOwner) {user->
            binding.uName.text = user.userName
            binding.uEmail.text = user.email
            binding.uPhoneNumber.text = user.phoneNumber
            binding.uAddressStreet.text = user.streetNumber
            binding.uAddressCity.text = user.city
            binding.uAddressState.text = user.state
            binding.uGender.text = user.gender
            if(user.age == null) {
                binding.uAge.text = ""
            } else {
                binding.uAge.text = user.age.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        setUserProfileData(this)
    }
}