package com.example.myapplication.helpers
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView

object ChangeColor {
    fun invertColors(imageView: ImageView?) {
        val matrix = ColorMatrix().apply {
            set(floatArrayOf(
                -1f,  0f,  0f, 0f, 255f,
                0f, -1f,  0f, 0f, 255f,
                0f,  0f, -1f, 0f, 255f,
                0f,  0f,  0f, 1f, 0f
            ))
        }
        val filter = ColorMatrixColorFilter(matrix)
        imageView?.setColorFilter(filter)
    }

    fun makeImageWhite(imageView: ImageView?) {
        val matrix = ColorMatrix().apply {
            set(floatArrayOf(
                1f, 0f, 0f, 0f, 255f, // Увеличение красного канала
                0f, 1f, 0f, 0f, 255f, // Увеличение зеленого канала
                0f, 0f, 1f, 0f, 255f, // Увеличение синего канала
                0f, 0f, 0f, 1f, 0f    // Не изменять альфа-канал
            ))
        }
        val filter = ColorMatrixColorFilter(matrix)
        imageView?.colorFilter = filter
    }

}
