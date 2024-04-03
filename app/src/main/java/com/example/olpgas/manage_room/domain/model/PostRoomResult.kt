package com.example.olpgas.manage_room.domain.model

import com.example.olpgas.core.util.SimpleResource
import com.example.olpgas.core.util.Error as Error

data class PostRoomResult(
    val roomAreaError: Error? = null,
    val shareableError: Error? = null,
    val roomTypeError: Error? = null,
    val rentAmountError: Error? = null,
    val depositError: Error? = null,
    val descriptionError: Error? = null,
    val suitableForError: Error? = null,
    val featuresError: Error? = null,
    val roomNameError: Error? = null,
    val roomNumberError: Error? = null,
    val streetNumberError: Error? = null,
    val landMarkError: Error? = null,
    val cityError: Error? = null,
    val stateError: Error? = null,
    val result: SimpleResource? = null
)