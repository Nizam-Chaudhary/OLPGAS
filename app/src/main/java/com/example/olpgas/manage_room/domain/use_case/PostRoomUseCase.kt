package com.example.olpgas.manage_room.domain.use_case

import android.content.SharedPreferences
import android.util.Log
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.core.util.Constants
import com.example.olpgas.core.util.Resource
import com.example.olpgas.manage_room.data.remote.model.RoomDetails
import com.example.olpgas.manage_room.data.remote.model.RoomMaster
import com.example.olpgas.manage_room.domain.model.PostRoomResult
import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository
import com.example.olpgas.manage_room.domain.util.ValidationUtil

class PostRoomUseCase(
    private val repository: ManageRoomRepository,
    private val authSharedPreferences: SharedPreferences,
) {
    suspend operator fun invoke(
        roomArea: Int,
        shareable: Int,
        roomType: String,
        rentAmount: Int,
        deposit: Int,
        description: String,
        suitableFor: List<String>,
        features: List<String>,
        ratings: Float,
        roomName: String,
        roomNumber: String,
        streetNumber: String,
        landMark: String,
        city: String,
        state: String,
        bookingStatus: String,
        images: List<ByteArray>
    ) : PostRoomResult {

        val roomAreaError = ValidationUtil.validateNumberEmptyField(roomArea)
        val shareableError = ValidationUtil.validateNumberEmptyField(shareable)
        val roomTypeError = ValidationUtil.validateEmptyField(roomType)
        val rentAmountError = ValidationUtil.validateNumberEmptyField(rentAmount)
        val depositError = ValidationUtil.validateNumberEmptyField(deposit)
        val descriptionError = ValidationUtil.validateEmptyField(description)
        val suitableForError = ValidationUtil.validateEmptyList(suitableFor)
        val featuresError = ValidationUtil.validateEmptyList(features)
        val roomNameError = ValidationUtil.validateEmptyField(roomName)
        val roomNumberError = ValidationUtil.validateEmptyField(roomNumber)
        val streetNumberError = ValidationUtil.validateEmptyField(streetNumber)
        val landMarkError = ValidationUtil.validateEmptyField(landMark)
        val cityError = ValidationUtil.validateCity(city)
        val stateError = ValidationUtil.validateEmptyField(state)
        val imagesError = ValidationUtil.validateEmptyListByteArray(images)

        if(roomAreaError != null || shareableError != null || roomTypeError != null || rentAmountError != null ||
            depositError != null || descriptionError != null || suitableForError != null || featuresError != null ||
            roomNameError != null || roomNumberError != null || streetNumberError != null || landMarkError != null ||
            cityError != null || stateError != null || imagesError != null) {

            return PostRoomResult(
                roomAreaError,
                shareableError,
                roomTypeError,
                rentAmountError,
                depositError,
                descriptionError,
                suitableForError,
                featuresError,
                roomNameError,
                roomNumberError,
                streetNumberError,
                landMarkError,
                cityError,
                stateError,
                imagesError
            )
        }

        val roomDetails = RoomDetails(
            roomArea,
            shareable,
            roomType,
            rentAmount,
            deposit,
            description,
            suitableFor,
            features,
            ratings
        )
        Log.d("Post Room", roomDetails.toString())
        val roomFeatureId = repository.upsertRoomDetails(roomDetails)

        if(roomFeatureId != null) {
            val ownerId = authSharedPreferences.getString(Constants.USER_ID,null)
            ownerId?.let {
                val roomMaster = RoomMaster(
                    roomName,
                    ownerId,
                    roomNumber,
                    streetNumber,
                    landMark,
                    city,
                    state,
                    roomFeatureId,
                    bookingStatus
                )
                Log.d("Post Room", roomMaster.toString())
                repository.uploadImages(ownerId, roomName, images)
                val result = repository.upsertRoomMaster(roomMaster)
                return PostRoomResult(
                    result = result
                )
            }
        }
        return PostRoomResult(
            result = Resource.Error("Error Posting Room")
        )
    }
}