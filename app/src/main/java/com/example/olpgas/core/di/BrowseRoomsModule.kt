package com.example.olpgas.core.di

import android.app.Application
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.remote.SupabaseListRooms
import com.example.olpgas.browse_rooms.data.repository.BrowseRoomsRepositoryImpl
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.browse_rooms.domain.use_case.GetAllRoomDetailsFromLocalDBUseCase
import com.example.olpgas.browse_rooms.domain.use_case.GetLocalCacheUseCase
import com.example.olpgas.browse_rooms.domain.use_case.RefreshLocalCacheUseCase
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
    fun provideGetLocalCacheUseCase(application: Application) : GetLocalCacheUseCase {
        return GetLocalCacheUseCase(application)
    }

    @Provides
    @Singleton
    fun provideRefreshLocalCacheUseCase(application: Application) : RefreshLocalCacheUseCase {
        return RefreshLocalCacheUseCase(application)
    }
}