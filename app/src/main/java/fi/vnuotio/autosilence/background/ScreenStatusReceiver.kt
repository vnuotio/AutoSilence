package fi.vnuotio.autosilence.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenStatusReceiver : BroadcastReceiver() {
    companion object {
        private var INSTANCE: ScreenStatusReceiver? = null

        fun getInstance(): ScreenStatusReceiver {
            synchronized(this) { return INSTANCE ?: ScreenStatusReceiver() }
        }
    }
    override fun onReceive(p0: Context?, p1: Intent?) {
        TODO("Not yet implemented")
    }
}