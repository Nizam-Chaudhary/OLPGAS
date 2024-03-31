package com.example.olpgas.core.di

import com.example.olpgas.browse_rooms.data.remote.SupabaseListRooms
import com.example.olpgas.browse_rooms.data.repository.BrowseRoomsRepositoryImpl
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.browse_rooms.domain.use_case.GetRoomsImageForListingUseCase
import com.example.olpgas.browse_rooms.domain.use_case.GetRoomsForListingUseCase
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
    fun provideBrowserRoomRepository(supabaseListRooms: SupabaseListRooms) : BrowseRoomsRepository {
        return BrowseRoomsRepositoryImpl(supabaseListRooms)
    }

    @Provides
    @Singleton
    fun provideGetRoomsForListingUseCase(repository: BrowseRoomsRepository) : GetRoomsForListingUseCase {
        return GetRoomsForListingUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetRoomImageForListingUseCase(repository: BrowseRoomsRepository) : GetRoomsImageForListingUseCase {
        return GetRoomsImageForListingUseCase(
            repository
        )
    }

}