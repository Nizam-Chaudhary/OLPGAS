package com.example.olpgas.core.util

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NetworkUnavailableDialog(context: Context) {
    val networkUnavailable = MaterialAlertDialogBuilder(context)
    .setTitle("Connection error")
    .setMessage("Please check your network connection")
    .setPositiveButton("dismiss") { _, _ -> }
    .show()
}