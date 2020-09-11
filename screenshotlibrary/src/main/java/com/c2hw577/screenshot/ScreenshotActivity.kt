package com.c2hw577.screenshot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler


class ScreenshotActivity : Activity() {

    private var mediaProjectionManager: MediaProjectionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val attributes = window.attributes
        attributes.height = 1
        attributes.width = 1
        attributes.dimAmount = 0.0f
        window.attributes = attributes
        super.onCreate(null)

        mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val it = mediaProjectionManager?.createScreenCaptureIntent()
        startActivityForResult(it, 577)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val mediaProjection = try {
                mediaProjectionManager?.getMediaProjection(resultCode, data)
            } catch (e: Exception) {
                null
            }
            if (mediaProjection != null) {
                Handler().postDelayed({
                    ScreenshotUtils.updateMediaProjection(mediaProjection)
                }, 100)
                finish()
                return
            }
        }
        ScreenshotUtils.onFail()
        finish()
    }
}