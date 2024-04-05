package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.view_room_details.domain.use_case.GetFullRoomDetailsFromLocalDBUseCase

data class UpdateRoomUseCases(
    val updateAddressUseCase: UpdateAddressUseCase,
    val updateDepositUseCase: UpdateDepositUseCase,
    val updateDescriptionUseCase: UpdateDescriptionUseCase,
    val updateRentUseCase: UpdateRentUseCase,
    val updateRoomAreaUseCase: UpdateRoomAreaUseCase,
    val updateRoomNameUseCase: UpdateRoomNameUseCase,
    val updateRoomTypeUseCase: UpdateRoomTypeUseCase,
    val updateShareableByUseCase: UpdateShareableByUseCase,
    val updateAmenityUseCase: UpdateAmenityUseCase,
    val updateSuitableForUseCase: UpdateSuitableForUseCase,
    val removeRoomUseCase: RemoveRoomUseCase,
    val connectivityObserver: ConnectivityObserver,
    val getFullRoomDetailsFromLocalDBUseCase: GetFullRoomDetailsFromLocalDBUseCase
)
