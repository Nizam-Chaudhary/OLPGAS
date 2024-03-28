package com.example.olpgas.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.data.network.SupabaseClient.client
import com.example.olpgas.manage_room.model.WorkState
import com.example.olpgas.profile.data.model.ProfileSaveStatus
import com.example.olpgas.profile.data.model.User
import com.example.olpgas.roomdetails.data.model.OwnerId
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch

class UserProfileViewModel: ViewModel() {

    private val _profileSaveStatus = MutableLiveData<ProfileSaveStatus>()
    val profileSaveStatus: LiveData<ProfileSaveStatus> = _profileSaveStatus

    private val _userProfileData = MutableLiveData<User>()
    val userProfileData: LiveData<User> = _userProfileData

    private val _userProfileImageByteArray = MutableLiveData<ByteArray>()
    val userProfileImageByteArray: LiveData<ByteArray> = _userProfileImageByteArray

    private val _userProfilePictureStatus = MutableLiveData<WorkState>()
    val userProfilePictureStatus: LiveData<WorkState> =_userProfilePictureStatus

    fun saveUserProfile(user: User) {
        viewModelScope.launch {
            try {
                _profileSaveStatus.value = ProfileSaveStatus.Success
                client.postgrest.from("Users")
                    .upsert(user)
                _profileSaveStatus.value = ProfileSaveStatus.Success
            }catch (e: Exception) {
                _profileSaveStatus.value = ProfileSaveStatus.Error(e.message.toString())
                Log.d("User Profile", e.message.toString())
            }
        }
    }

    fun saveUserProfilePicture(imageByteArray: ByteArray) {
        viewModelScope.launch {
            try {
                _userProfilePictureStatus.value = WorkState.Loading
                val ownerId = client.auth.currentUserOrNull()?.id
                client.storage.from("ProfilePics")
                    .upload("$ownerId/profile.jpg", imageByteArray, upsert = true)
                _userProfilePictureStatus.value = WorkState.Success("Profile image uploaded")
            } catch (e: Exception) {
                _userProfilePictureStatus.value = WorkState.Error("error uploading image")
                Log.d("User Profile", "Error: ${e.message}")
            }
        }
    }

    fun getUserProfileData() {
        viewModelScope.launch {
            try {
                _userProfileData.value = client.postgrest.from("UserDetails")
                    .select().decodeSingle<User>()
            }catch (e: Exception) {
                Log.d("User Profile", e.message.toString())
            }
        }
    }

    fun getUserProfileByteArray() {
        viewModelScope.launch {
            try {
                val ownerId = client.postgrest.from("UserDetails")
                    .select(Columns.list("userId")).decodeSingle<OwnerId>().userId
                _userProfileImageByteArray.value = client.storage.from("ProfilePics")
                    .downloadAuthenticated("$ownerId/profile.jpg")
            } catch (e: Exception) {
                Log.e("UserProfilePicture","Error: ${e.message}")
            }
        }
    }
}