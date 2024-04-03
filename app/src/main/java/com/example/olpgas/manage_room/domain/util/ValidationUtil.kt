package com.example.olpgas.manage_room.domain.util

import com.example.olpgas.core.util.Error

class ValidationUtil {
    companion object {
        fun validateEmptyField(value: String) : Error? {
            if(value.isEmpty()) {
                return Error.FieldEmpty
            }
            return null
        }

        fun validateNumberEmptyField(value: Int) : Error? {
            if(value == 0) {
                return Error.FieldEmpty
            }
            return null
        }

        fun validateEmptyList(value: List<String>) : Error? {
            if(value.isEmpty()) {
                return Error.NoItemSelected
            }
            return null
        }

        fun validateEmptyListByteArray(value: List<ByteArray>) : Error? {
            if(value.isEmpty()) {
                return Error.NoItemSelected
            }
            return null
        }
    }
}