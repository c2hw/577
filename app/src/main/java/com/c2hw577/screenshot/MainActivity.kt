package com.c2hw577.screenshot

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun screenshot(view: View) {

        ScreenshotUtils.getInstance(applicationContext).screenshot(object : OnScreenshotListener {

            override fun onSuccess(bitmap: Bitmap) {
                imageView.setImageBitmap(bitmap)
            }

            override fun onError(e: Exception) {
                Toast.makeText(applicationContext, "Error:$e", Toast.LENGTH_SHORT).show()
            }
        })
    }

}