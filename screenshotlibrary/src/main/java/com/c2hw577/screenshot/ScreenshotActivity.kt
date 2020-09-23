package com.c2hw577.screenshot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ServiceCompat.stopForeground


class ScreenshotActivity : Activity() {

    private var mediaProjectionManager: MediaProjectionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val attributes = window.attributes
        attributes.height = 1
        attributes.width = 1
        attributes.dimAmount = 0.0f
        window.attributes = attributes
        super.onCreate(null)

        val mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val it = mediaProjectionManager.createScreenCaptureIntent()
        startActivityForResult(it, 577)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (Build.VERSION.SDK_INT >= 29) {
                val service = Intent(this, ScreenshotService::class.java)
                service.putExtra("code", resultCode)
                service.putExtra("data", data)
                startForegroundService(service)
                finish()
                return
            } else {
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
        }
        ScreenshotUtils.onFail()
        finish()
    }

}