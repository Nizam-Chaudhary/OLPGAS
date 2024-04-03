package com.example.olpgas.manage_room.presentation.add_room

sealed class AddRoomEvent {
    data class EnteredRoomName(val roomName: String): AddRoomEvent()
    data class EnteredRoomNumber(val roomNumber: String): AddRoomEvent()
    data class EnteredStreetNumber(val streetNumber: String): AddRoomEvent()
    data class EnteredLandMark(val landMark: String): AddRoomEvent()
    data class SelectedState(val state: String): AddRoomEvent()
    data class SelectedCity(val city: String): AddRoomEvent()
    data class SelectedRoomType(val roomType: String): AddRoomEvent()
    data class EnteredShareableBy(val shareableBy: String): AddRoomEvent()
    data class EnteredRentAmount(val rentAmount: String): AddRoomEvent()
    data class EnteredDeposit(val deposit: String): AddRoomEvent()
    data class EnteredRoomArea(val roomArea: String): AddRoomEvent()
    data class AddedAmenity(val amenity: String): AddRoomEvent()
    data class AddedSuitableFor(val suitableFor: String): AddRoomEvent()
    data class EnteredDescription(val description: String): AddRoomEvent()
    data class AddedImage(val image: ByteArray): AddRoomEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AddedImage

            return image.contentEquals(other.image)
        }

        override fun hashCode(): Int {
            return image.contentHashCode()
        }
    }

    data object OnSubmit: AddRoomEvent()
}