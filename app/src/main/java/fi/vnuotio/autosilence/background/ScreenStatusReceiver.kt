package fi.vnuotio.autosilence.background

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.ACTION_SCREEN_OFF
import android.content.Intent.ACTION_SCREEN_ON
import android.media.AudioManager
import android.media.AudioManager.RINGER_MODE_SILENT
import fi.vnuotio.autosilence.utils.SharedPrefsHandler

class ScreenStatusReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val nm = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val am = context.getSystemService(AUDIO_SERVICE) as AudioManager
        val sph = SharedPrefsHandler(context)

        if (!nm.isNotificationPolicyAccessGranted) return

        if (intent.action == ACTION_SCREEN_ON) {
            am.ringerMode = RINGER_MODE_SILENT
        }
        else if (intent.action == ACTION_SCREEN_OFF) {
            am.ringerMode = sph.getLastRingerMode()
        }
    }
}