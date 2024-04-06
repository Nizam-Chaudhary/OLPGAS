package com.example.olpgas.core.di

import android.app.Application
import android.content.SharedPreferences
import com.example.olpgas.bookings_history.domain.repository.RoomBookingRepository
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.browse_rooms.domain.use_case.RefreshLocalCacheUseCase
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.manage_room.data.remote.SupabaseManageRoom
import com.example.olpgas.manage_room.data.repository.ManageRoomRepositoryImpl
import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository
import com.example.olpgas.manage_room.domain.use_case.GetAllOwnedRoomsUseCase
import com.example.olpgas.manage_room.domain.use_case.PostRoomUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.AddImageUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.RemoveImageUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.RemoveRoomUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateAddressUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateAmenityUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateDepositUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateDescriptionUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateRentUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateRoomAreaUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateRoomNameUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateRoomTypeUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateRoomUseCases
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateShareableByUseCase
import com.example.olpgas.manage_room.domain.use_case.update_room.UpdateSuitableForUseCase
import com.example.olpgas.view_room_details.data.local.database.FullRoomDetailsDatabase
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository
import com.example.olpgas.view_room_details.domain.use_case.GetFullRoomDetailsFromLocalDBUseCase
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
    fun provideManageRoomRepository(
        browseRoomDatabase: BrowseRoomDatabase,
        fullRoomDatabase: FullRoomDetailsDatabase,
        supabaseManageRoom: SupabaseManageRoom
    ) : ManageRoomRepository {
        return ManageRoomRepositoryImpl(browseRoomDatabase, fullRoomDatabase, supabaseManageRoom)
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
    fun provideRemoveRoomUseCase(
        application: Application
    ) : RemoveRoomUseCase {
        return RemoveRoomUseCase(application)
    }

    @Provides
    @Singleton
    fun providePostRoomUseCase(
        repository: ManageRoomRepository,
        authSharedPreferences: SharedPreferences
    ) : PostRoomUseCase {
        return PostRoomUseCase(repository, authSharedPreferences)
    }

    @Provides
    @Singleton
    fun provideUpdateRoomUseCases(
        application: Application,
        manageRoomRepository: ManageRoomRepository,
        connectivityObserver: ConnectivityObserver,
        browseRoomsRepository: BrowseRoomsRepository,
        viewRoomsRepository: ViewRoomDetailsRepository,
        roomBookingRepository: RoomBookingRepository,
        getFullRoomDetailsFromLocalDBUseCase: GetFullRoomDetailsFromLocalDBUseCase
    ) : UpdateRoomUseCases{
        return UpdateRoomUseCases(
            UpdateAddressUseCase(manageRoomRepository),
            UpdateDepositUseCase(manageRoomRepository),
            UpdateDescriptionUseCase(manageRoomRepository),
            UpdateRentUseCase(manageRoomRepository),
            UpdateRoomAreaUseCase(manageRoomRepository),
            UpdateRoomNameUseCase(manageRoomRepository),
            UpdateRoomTypeUseCase(manageRoomRepository),
            UpdateShareableByUseCase(manageRoomRepository),
            UpdateAmenityUseCase(manageRoomRepository),
            UpdateSuitableForUseCase(manageRoomRepository),
            RemoveRoomUseCase(application),
            RemoveImageUseCase(manageRoomRepository),
            AddImageUseCase(manageRoomRepository),
            RefreshLocalCacheUseCase(browseRoomsRepository, viewRoomsRepository, roomBookingRepository),
            connectivityObserver,
            getFullRoomDetailsFromLocalDBUseCase
        )
    }
}