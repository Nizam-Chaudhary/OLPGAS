package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateDepositUseCase (
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        roomFeatureId: Int,
        deposit: Int
    ) {
        manageRoomRepository.updateDeposit(roomFeatureId, deposit)
        manageRoomRepository.updateDepositAllRoomDetails(id, deposit)
        manageRoomRepository.updateDepositFullRoomDetails(id, deposit)
    }
}