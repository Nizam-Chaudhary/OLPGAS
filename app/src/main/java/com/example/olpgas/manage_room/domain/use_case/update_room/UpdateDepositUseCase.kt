package com.example.olpgas.manage_room.domain.use_case.update_room

import com.example.olpgas.manage_room.domain.repository.ManageRoomRepository

class UpdateDepositUseCase (
    private val manageRoomRepository: ManageRoomRepository
) {
    suspend operator fun invoke(
        id: Int,
        deposit: Int
    ) {
        manageRoomRepository.updateDeposit(id, deposit)
        manageRoomRepository.updateDepositAllRoomDetails(id, deposit)
        manageRoomRepository.updateDepositFullRoomDetails(id, deposit)
    }
}