package com.demo.clear.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.PendingIntent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.demo.clear.R
import com.demo.clear.activity.HomeActivity
import com.demo.clear.activity.MainActivity
import kotlin.random.Random


class NotifiService:Service() {
    private var isOpenNotificationService=false
    private val NOTIFICATION_ID="11111"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        build()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun build() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanel = NotificationChannel(
                NOTIFICATION_ID,
                "TopBooster",
                NotificationManager.IMPORTANCE_HIGH
            )
            chanel.enableLights(true)
            chanel.setShowBadge(true)
            manager.createNotificationChannel(chanel)
        }

        val builder = NotificationCompat.Builder(this, NOTIFICATION_ID)
            .setSmallIcon(R.drawable.logo)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(getString(R.string.app_name))
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    setStyle(NotificationCompat.DecoratedCustomViewStyle())
                }
            }

        builder.setCustomContentView(createCustomView())
        startForeground(11, builder.build())
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            isOpenNotificationService = true
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getActivityPendingIntent(type: String): PendingIntent {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val intent = Intent(this.application, HomeActivity::class.java).apply {
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("type", type)
        }
        return PendingIntent.getActivity(
            this,
            Random.nextInt(1000, 3000),
            intent,
            flags
        )
    }



    private fun createCustomView(): RemoteViews {
        val remoteView = RemoteViews(packageName, R.layout.layout_notification)
        remoteView.setOnClickPendingIntent(
            R.id.tv_clean,
            getActivityPendingIntent("clean")
        )
//        remoteView.setOnClickPendingIntent(R.id.tv_clean, getActivityPendingIntent(ScanType.CLEAN))
//        remoteView.setOnClickPendingIntent(R.id.tv_cpu, getActivityPendingIntent(ScanType.CPU))
//        remoteView.setOnClickPendingIntent(
//            R.id.tv_booster,
//            getActivityPendingIntent(ScanType.BOOST)
//        )
        return remoteView
    }

    override fun onDestroy() {
        super.onDestroy()
        isOpenNotificationService = false
    }

}