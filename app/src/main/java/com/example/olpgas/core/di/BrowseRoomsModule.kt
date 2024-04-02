package com.example.olpgas.core.di

import android.app.Application
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.remote.SupabaseListRooms
import com.example.olpgas.browse_rooms.data.repository.BrowseRoomsRepositoryImpl
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.browse_rooms.domain.use_case.GetAllRoomDetailsFromLocalDBUseCase
import com.example.olpgas.browse_rooms.domain.use_case.RefreshFullRoomDetailsLocalCacheUseCase
import com.example.olpgas.browse_rooms.domain.use_case.RefreshLocalCacheUseCase
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BrowseRoomsModule {

    @Provides
    @Singleton
    fun provideSupabaseListRoom() : SupabaseListRooms {
        return SupabaseListRooms()
    }

    @Provides
    @Singleton
    fun provideBrowseRoomDatabase(application: Application) : BrowseRoomDatabase {
        return BrowseRoomDatabase.Companion(application)
    }

    @Provides
    @Singleton
    fun provideBrowserRoomRepository(supabaseListRooms: SupabaseListRooms, database: BrowseRoomDatabase) : BrowseRoomsRepository {
        return BrowseRoomsRepositoryImpl(supabaseListRooms, database)
    }

    @Provides
    @Singleton
    fun provideGetAllRoomDetailsFromLocalDBUseCase(repository: BrowseRoomsRepository) : GetAllRoomDetailsFromLocalDBUseCase {
        return GetAllRoomDetailsFromLocalDBUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRefreshLocalCacheUseCase(repository: BrowseRoomsRepository) : RefreshLocalCacheUseCase {
        return RefreshLocalCacheUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRefreshFullRoomDetailsLocalCacheUseCase(repository: ViewRoomDetailsRepository) : RefreshFullRoomDetailsLocalCacheUseCase {
        return RefreshFullRoomDetailsLocalCacheUseCase(repository)
    }
}