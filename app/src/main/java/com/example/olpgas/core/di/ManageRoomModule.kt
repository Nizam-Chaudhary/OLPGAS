package com.example.olpgas.core.di

import android.content.SharedPreferences
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.manage_room.data.remote.SupabaseManageRoom
import com.example.olpgas.manage_room.data.repository.ManageRoomRepositoryImpl
import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository
import com.example.olpgas.manage_room.domain.use_case.GetAllOwnedRoomsUseCase
import com.example.olpgas.manage_room.domain.use_case.PostRoomUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManageRoomModule {

    @Provides
    @Singleton
    fun provideSupabaseManageRoom() : SupabaseManageRoom {
        return SupabaseManageRoom()
    }

    @Provides
    @Singleton
    fun provideManageRoomRepository(database: BrowseRoomDatabase, supabaseManageRoom: SupabaseManageRoom) : ManageRoomRepository {
        return ManageRoomRepositoryImpl(database, supabaseManageRoom)
    }

    @Provides
    @Singleton
    fun provideGetAllOwnedRoomsUseCase(
        repository: ManageRoomRepository,
        authSharedPreferences: SharedPreferences
        ) : GetAllOwnedRoomsUseCase {
        return GetAllOwnedRoomsUseCase(repository, authSharedPreferences)
    }

    @Provides
    @Singleton
    fun providePostRoomUseCase(
        repository: ManageRoomRepository,
        authSharedPreferences: SharedPreferences
    ) : PostRoomUseCase {
        return PostRoomUseCase(repository, authSharedPreferences)
    }
}