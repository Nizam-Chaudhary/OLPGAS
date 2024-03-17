package com.example.olpgas.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.data.network.SupabaseClient.client
import com.example.olpgas.profile.data.model.ProfileSaveStatus
import com.example.olpgas.profile.data.model.User
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class UserProfileViewModel: ViewModel() {

    private val _profileSaveStatus = MutableLiveData<ProfileSaveStatus>()
    val profileSaveStatus: LiveData<ProfileSaveStatus> = _profileSaveStatus

    private val _userProfileData = MutableLiveData<User>()
    val userProfileData: LiveData<User> = _userProfileData

    fun saveUserProfile(user: User) {
        viewModelScope.launch {
            try {
                client.postgrest.from("Users")
                    .upsert(user)
                _profileSaveStatus.value = ProfileSaveStatus.Success
            }catch (e: Exception) {
                _profileSaveStatus.value = ProfileSaveStatus.Error(e.message.toString())
                Log.d("User Profile", e.message.toString())
            }
        }
    }

    fun getUserProfileData() {
        viewModelScope.launch {
            try {
                _userProfileData.value = client.postgrest.from("Users")
                    .select().decodeSingle<User>()
            }catch (e: Exception) {
                Log.d("User Profile", e.message.toString())
            }
        }
    }
}