package fi.vnuotio.autosilence.background

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ScreenStatusService : Service() {
    companion object {
        private var INSTANCE: ScreenStatusService? = null

        fun getService(): ScreenStatusService? {
            return INSTANCE
        }
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}