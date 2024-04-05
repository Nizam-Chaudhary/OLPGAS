package com.example.olpgas.core.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NetworkUnavailableDialog(context: Context) {
    val show: AlertDialog = MaterialAlertDialogBuilder(context)
    .setTitle("Connection error")
    .setMessage("Please check your network connection")
    .setPositiveButton("dismiss") { _, _ -> }
    .show()
}