package com.c2hw577.screenshot

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.ServiceCompat

class ScreenshotService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //分组（可选）
            //groupId要唯一
            val groupId = "group_001"
            val group =
                NotificationChannelGroup(groupId, resources.getString(R.string.nc_name_screen))
            //创建group
            notificationManager.createNotificationChannelGroup(group)

            //channelId要唯一
            val channelId = "channel_001"
            val adChannel = NotificationChannel(
                channelId,
                resources.getString(R.string.nc_name_screen),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            //补充channel的含义（可选）
            //adChannel.description = resources.getString(R.string.nc_name_float_content);
            //将渠道添加进组（先创建组才能添加）
            adChannel.group = groupId
            //创建channel
            notificationManager.createNotificationChannel(adChannel)

            //创建通知时，标记你的渠道id
            val notification = Notification.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(resources.getString(R.string.nc_name_screen_content))
                .setAutoCancel(true)
                .build()

            startForeground(1, notification)
        }


        val resultCode = intent?.getIntExtra("code", -1)
        val data = intent?.getParcelableExtra<Intent>("data");

        val mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        val mediaProjection = try {
            mediaProjectionManager.getMediaProjection(resultCode!!, data!!)
        } catch (e: Exception) {
            null
        }
        if (mediaProjection != null) {
            Handler().postDelayed({
                ScreenshotUtils.updateMediaProjection(mediaProjection)
            }, 100)
            return super.onStartCommand(intent, flags, startId)
        }
        ScreenshotUtils.onFail()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}