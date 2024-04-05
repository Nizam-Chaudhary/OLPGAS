package com.example.olpgas.manage_room.domain.util

object Constants {
    const val ROOM_DETAILS_TABLE = "RoomDetails"
    const val ROOM_MASTER_TABLE = "RoomMaster"
    const val ROOM_PICS_BUCKET = "RoomPics"

    // Columns of Room Master Table
    const val COL_ID_ROOM_MASTER = "id"
    const val COL_ROOM_NAME = "roomName"
    const val COL_STREET_NUMBER = "streetNumber"
    const val COL_LANDMARK = "landMark"
    const val COL_CITY = "city"
    const val COL_STATE = "state"

    // Columns of Room Details Table
    const val COL_ID_ROOM_DETAILS = "id"
    const val COL_RENT_AMOUNT = "rentAmount"
    const val COL_DEPOSIT = "deposit"
    const val COL_SHAREABLE = "shareable"
    const val COL_ROOM_TYPE = "roomType"
    const val COL_ROOM_AREA = "roomArea"
    const val COL_ROOM_DESCRIPTION = "description"
    const val COL_FEATURES = "features"
    const val COL_SUITABLE_FOR = "suitableFor"
}