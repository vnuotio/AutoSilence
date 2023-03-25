package fi.vnuotio.autosilence.background

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.Service
import android.content.Intent
import android.content.Intent.ACTION_SCREEN_OFF
import android.content.Intent.ACTION_SCREEN_ON
import android.content.IntentFilter
import android.os.IBinder
import fi.vnuotio.autosilence.R
import fi.vnuotio.autosilence.background.ScreenStatusService.Constants.CHANNEL_ID


class ScreenStatusService : Service() {
    private object Constants {
        const val CHANNEL_ID = "AutoSilenceNotifications"
    }

    private var receiver: ScreenStatusReceiver? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter()
        filter.addAction(ACTION_SCREEN_ON)
        filter.addAction(ACTION_SCREEN_OFF)

        if (receiver == null) {
            receiver = ScreenStatusReceiver()
            registerReceiver(receiver, filter)
        }

        val notificationChannel = NotificationChannel(CHANNEL_ID,
            "AutoSilence", IMPORTANCE_DEFAULT)

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(notificationChannel)

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(resources.getText(R.string.notificationTitle))
            .setContentText(resources.getText(R.string.notificationBody))
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}