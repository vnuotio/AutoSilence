package fi.vnuotio.autosilence.background

import android.app.Service
import android.content.Intent
import android.content.Intent.ACTION_SCREEN_OFF
import android.content.Intent.ACTION_SCREEN_ON
import android.content.IntentFilter
import android.os.IBinder

class ScreenStatusService : Service() {
    companion object {
        private var INSTANCE: ScreenStatusService? = null

        fun getService(): ScreenStatusService? {
            synchronized(this) { return INSTANCE }
        }
    }

    private lateinit var receiver: ScreenStatusReceiver

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        receiver = ScreenStatusReceiver.getInstance()
        val filter = IntentFilter()

        filter.addAction(ACTION_SCREEN_ON)
        filter.addAction(ACTION_SCREEN_OFF)

        registerReceiver(receiver, filter)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}