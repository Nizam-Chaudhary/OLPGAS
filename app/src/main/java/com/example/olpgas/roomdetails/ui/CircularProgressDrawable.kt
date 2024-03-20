package com.example.olpgas.roomdetails.ui

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.olpgas.R

fun getCircularProgressDrawable(context: Context, centerRadius: Float = 40f): Drawable {
    return CircularProgressDrawable(context).apply {
        this.strokeWidth = 7f
        this.centerRadius = centerRadius
        this.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.textColor
            ), PorterDuff.Mode.SRC_IN
        )
        this.start()
    }
}