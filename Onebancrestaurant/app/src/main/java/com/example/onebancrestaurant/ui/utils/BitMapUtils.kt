package com.example.onebancrestaurant.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL

fun getResizedBitmapFromUrl(urlStr: String, reqWidth: Int, reqHeight: Int): Bitmap? {
    return try {

        val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeStream(URL(urlStr).openStream(), null, boundsOptions)

        val (width, height) = boundsOptions.outWidth to boundsOptions.outHeight
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }


        val finalOptions = BitmapFactory.Options().apply { this.inSampleSize = inSampleSize }
        val newInput = URL(urlStr).openStream()
        BitmapFactory.decodeStream(newInput, null, finalOptions)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
