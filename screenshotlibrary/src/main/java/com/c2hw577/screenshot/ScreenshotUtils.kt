package com.c2hw577.screenshot

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.WindowManager
import java.nio.ByteBuffer

class ScreenshotUtils(private val context: Context) {

    companion object {
        var screenshotUtils: ScreenshotUtils? = null

        fun getInstance(context: Context): ScreenshotUtils {
            if (screenshotUtils == null) {
                screenshotUtils = ScreenshotUtils(context)
                screenshotUtils?.init()
            }
            return screenshotUtils!!
        }

        fun updateMediaProjection(mediaProjection: MediaProjection) {
            screenshotUtils?.updateMediaProjection(mediaProjection)
        }

    }

    private var mediaProjection: MediaProjection? = null
    private var onScreenshotListener: OnScreenshotListener? = null

    private var windowWidth: Int = 0
    private var windowHeight: Int = 0
    private var screenDensity: Int = 0

    private fun init() {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowWidth = windowManager.defaultDisplay.width
        windowHeight = windowManager.defaultDisplay.height

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenDensity = displayMetrics.densityDpi
    }


    fun screenshot(onScreenshotListener: OnScreenshotListener) {
        this.onScreenshotListener = onScreenshotListener
        if (mediaProjection == null) {
            val intent = Intent(context, ScreenshotActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        getBitmap()
    }

    private fun updateMediaProjection(mediaProjection: MediaProjection) {
        this.mediaProjection = mediaProjection
        onScreenshotListener ?: return
        getBitmap()
    }


    private fun getBitmap() {
        val imageReader = ImageReader.newInstance(windowWidth, windowHeight, 0x1, 2);
        val virtualDisplay = mediaProjection?.createVirtualDisplay(
            "screen-mirror",
            windowWidth, windowHeight, screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.surface, null, null
        )

        object : Thread() {
            override fun run() {
                try {
                    var image: Image? = null
                    image = imageReader.acquireLatestImage()
                    while (image == null) {
                        SystemClock.sleep(10)
                        image = imageReader.acquireLatestImage()
                    }

                    val width: Int = image.width
                    val height: Int = image.height
                    val planes: Array<Image.Plane> = image.planes

                    val buffer: ByteBuffer = planes[0].buffer
                    //每个像素的间距
                    //每个像素的间距
                    val pixelStride: Int = planes[0].pixelStride
                    //总的间距
                    //总的间距
                    val rowStride: Int = planes[0].rowStride
                    val rowPadding = rowStride - pixelStride * width

                    var bitmap =
                        Bitmap.createBitmap(
                            width + rowPadding / pixelStride,
                            height,
                            Bitmap.Config.ARGB_8888
                        )
                    bitmap.copyPixelsFromBuffer(buffer)
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)

                    image.close()
                    virtualDisplay?.release()

                    Handler(Looper.getMainLooper()).post {
                        onScreenshotListener?.onSuccess(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Handler(Looper.getMainLooper()).post {
                        onScreenshotListener?.onError(e)
                    }
                }
            }
        }.start()
    }
}