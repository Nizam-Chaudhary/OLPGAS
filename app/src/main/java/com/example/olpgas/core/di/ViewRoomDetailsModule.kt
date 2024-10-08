package com.example.olpgas.core.di

import android.app.Application
import com.example.olpgas.view_room_details.data.local.database.FullRoomDetailsDatabase
import com.example.olpgas.view_room_details.data.remote.SupabaseBookRoom
import com.example.olpgas.view_room_details.data.remote.SupabaseRoomDetails
import com.example.olpgas.view_room_details.data.repository.ViewRoomDetailsRepositoryImpl
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository
import com.example.olpgas.view_room_details.domain.use_case.BookRoomUseCase
import com.example.olpgas.view_room_details.domain.use_case.GetFullRoomDetailsFromLocalDBUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewRoomDetailsModule {

    @Provides
    @Singleton
    fun provideSupabaseRoomDetails() : SupabaseRoomDetails {
        return SupabaseRoomDetails()
    }

    @Provides
    @Singleton
    fun provideFullRoomDetailsDatabase(application: Application) : FullRoomDetailsDatabase {
        return FullRoomDetailsDatabase.Companion(application)
    }

    @Provides
    @Singleton
    fun provideSupabaseBookRoom() : SupabaseBookRoom {
        return SupabaseBookRoom()
    }


    @Provides
    @Singleton
    fun provideViewRoomDetailsRepository(
        supabaseRoomDetails: SupabaseRoomDetails,
        supabaseBookRoom: SupabaseBookRoom,
        fullRoomDetailsDatabase: FullRoomDetailsDatabase
    ) : ViewRoomDetailsRepository {
        return ViewRoomDetailsRepositoryImpl(supabaseRoomDetails,supabaseBookRoom, fullRoomDetailsDatabase)
    }

    @Provides
    @Singleton
    fun provideGetFullRoomDetailsFromLocalDBUseCase(repository: ViewRoomDetailsRepository) : GetFullRoomDetailsFromLocalDBUseCase {
        return GetFullRoomDetailsFromLocalDBUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideBookRoomUseCase(repository: ViewRoomDetailsRepository) : BookRoomUseCase {
        return BookRoomUseCase(repository)
    }
}