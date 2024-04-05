package com.example.olpgas.manage_room.presentation.update_room

sealed class UpdateRoomEvent{
    data class OnCreate(val id: Int): UpdateRoomEvent()
    data class OnUpdateRoomName(val roomName: String) : UpdateRoomEvent()
    data class OnUpdateAddress(
        val streetNumber: String,
        val landMark: String,
        val state: String,
        val city: String
    ) : UpdateRoomEvent()

    data class OnUpdateRent(
        val rent: Int
    ) : UpdateRoomEvent()

    data class OnUpdateDeposit(
        val deposit: Int
    ) : UpdateRoomEvent()

    data class OnUpdateShareableBy(
        val shareableBy: Int
    ) : UpdateRoomEvent()

    data class OnUpdateRoomType(
        val roomType: String
    ) : UpdateRoomEvent()

    data class OnUpdateRoomArea(
        val roomArea: Int
    ) : UpdateRoomEvent()

    data class OnUpdateDescription(
        val description: String
    ) : UpdateRoomEvent()

    data object OnRemoveRoom : UpdateRoomEvent()

    data class AddAmenity(val amenity: String) : UpdateRoomEvent()
    data class RemoveAmenity(val amenity: String) : UpdateRoomEvent()

    data class AddSuitableFor(val suitableFor: String) : UpdateRoomEvent()
    data class RemoveSuitableFor(val suitableFor: String) : UpdateRoomEvent()
}
