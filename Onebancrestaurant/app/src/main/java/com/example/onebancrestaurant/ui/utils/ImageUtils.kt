// File: ui/utils/ImageUtils.kt
package com.example.onebancrestaurant.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.net.URL

fun loadOptimizedBitmapFromUrl(urlString: String, reqWidth: Int, reqHeight: Int): Bitmap? {
    return try {
        val url = URL(urlString)
        val connection = url.openConnection()
        connection.doInput = true
        connection.connect()
        val inputStream = BufferedInputStream(connection.getInputStream())

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        inputStream.mark(inputStream.available())
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream.reset()

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false

        BitmapFactory.decodeStream(inputStream, null, options)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while ((halfHeight / inSampleSize) >= reqHeight &&
            (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
