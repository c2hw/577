package com.c2hw577.screenshot

import android.graphics.Bitmap

interface OnScreenshotListener {

    fun onSuccess(bitmap: Bitmap)

    fun onError(e: Exception)

}