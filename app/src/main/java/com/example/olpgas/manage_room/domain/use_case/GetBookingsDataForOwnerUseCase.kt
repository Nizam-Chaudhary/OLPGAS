package com.example.olpgas.manage_room.domain.use_case

import androidx.lifecycle.LiveData
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.bookings_history.domain.repository.RoomBookingRepository
import com.example.olpgas.user_profile.data.local.UserProfileSharedPreferences

class GetBookingsDataForOwnerUseCase (
    private val bookingRepository: RoomBookingRepository,
    private val userProfileSharedPreferences: UserProfileSharedPreferences
) {
    suspend operator fun invoke() : LiveData<List<RoomBookingLocal>> {
        val userId = userProfileSharedPreferences.getUserProfile().userId ?: ""
        return bookingRepository.getAllRoomBookingsUserFromLocalForOwner(userId)
    }
}