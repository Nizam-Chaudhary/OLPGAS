package com.example.olpgas.manage_room.presentation.post_room

sealed class PostRoomEvent {
    data object OnCreate: PostRoomEvent()
    data class EnteredRoomName(val roomName: String): PostRoomEvent()
    data class EnteredRoomNumber(val roomNumber: String): PostRoomEvent()
    data class EnteredStreetNumber(val streetNumber: String): PostRoomEvent()
    data class EnteredLandMark(val landMark: String): PostRoomEvent()
    data class SelectedState(val state: String): PostRoomEvent()
    data class SelectedCity(val city: String): PostRoomEvent()
    data class SelectedRoomType(val roomType: String): PostRoomEvent()
    data class EnteredShareableBy(val shareableBy: String): PostRoomEvent()
    data class EnteredRentAmount(val rentAmount: String): PostRoomEvent()
    data class EnteredDeposit(val deposit: String): PostRoomEvent()
    data class EnteredRoomArea(val roomArea: String): PostRoomEvent()
    data class AddedAmenity(val amenity: ChipField): PostRoomEvent()
    data class AddedSuitableFor(val suitableFor: ChipField): PostRoomEvent()
    data class EnteredDescription(val description: String): PostRoomEvent()
    data class FeatureCheckChange(val chipField: ChipField, val index: Int) : PostRoomEvent()
    data class SuitableForCheckChange(val chipField: ChipField, val index: Int) : PostRoomEvent()
    data class AddedImage(val image: ByteArray): PostRoomEvent() {
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

    data object OnSubmit: PostRoomEvent()
}