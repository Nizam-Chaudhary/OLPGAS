package com.example.olpgas.core.di

import android.app.Application
import com.example.olpgas.bookings_history.data.local.database.RoomBookingDatabase
import com.example.olpgas.bookings_history.data.remote.SupabaseBookings
import com.example.olpgas.bookings_history.data.repository.RoomBookingRepositoryImpl
import com.example.olpgas.bookings_history.domain.repository.RoomBookingRepository
import com.example.olpgas.bookings_history.domain.use_case.GetBookingsDataForUserUseCase
import com.example.olpgas.more_details.data.local.SettingsSharedPreferences
import com.example.olpgas.user_profile.data.local.UserProfileSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomBookingModule {
    @Provides
    @Singleton
    fun provideRoomBookingDatabase(application: Application) :
            RoomBookingDatabase {
        return RoomBookingDatabase.Companion(application)
    }

    @Provides
    @Singleton
    fun provideRoomBookingRepository(database: RoomBookingDatabase) :
            RoomBookingRepository {
        return RoomBookingRepositoryImpl(database, SupabaseBookings())
    }

    @Provides
    @Singleton
    fun provideGetBookingsDataForUserUseCase(
        userProfileSharedPreferences: UserProfileSharedPreferences,
        bookingRepository: RoomBookingRepository
    ) : GetBookingsDataForUserUseCase {
        return GetBookingsDataForUserUseCase(
            bookingRepository, userProfileSharedPreferences
        )
    }
}