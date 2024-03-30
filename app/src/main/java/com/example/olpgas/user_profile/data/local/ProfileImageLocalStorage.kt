package com.example.olpgas.user_profile.data.local

import android.app.Application
import android.util.Log
import com.example.olpgas.user_profile.domain.util.Constants.PROFILE_FILE_NAME
import java.io.File
import java.io.FileOutputStream

class ProfileImageLocalStorage(
    private val app: Application
) {
    fun saveProfileImageToInternalStorage(byteArray: ByteArray) {
        val file = File(app.dataDir, PROFILE_FILE_NAME)
        try {
            val outputStream = FileOutputStream(file)
            outputStream.write(byteArray)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving image to internal storage", e)
        }
    }

    fun loadProfileImageFromInternalStorage(): ByteArray? {
        val file = File(app.dataDir, PROFILE_FILE_NAME)
        return if (file.exists()) {
            file.readBytes()
        } else {
            null
        }
    }

    fun deleteProfileImageFromInternalStorage() {
        val file = File(app.dataDir, PROFILE_FILE_NAME)
        if (file.exists()) {
            file.delete()
        }
    }

    companion object{
        const val TAG = "ProfileImageLocalStorage"
    }
}