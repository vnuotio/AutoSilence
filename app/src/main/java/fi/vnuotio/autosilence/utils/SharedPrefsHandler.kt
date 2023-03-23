package fi.vnuotio.autosilence.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.media.AudioManager.RINGER_MODE_SILENT
import fi.vnuotio.autosilence.utils.SharedPrefsHandler.Constants.LAST_RINGER_MODE
import fi.vnuotio.autosilence.utils.SharedPrefsHandler.Constants.USER_DATA_PATH

class SharedPrefsHandler(context: Context) {
    private object Constants{
        const val USER_DATA_PATH = "userData"
        const val LAST_RINGER_MODE = "lastRingerMode"
    }
    private val sharedPrefs = context.getSharedPreferences(USER_DATA_PATH, MODE_PRIVATE)

    fun getLastRingerMode(): Int {
        return sharedPrefs.getInt(LAST_RINGER_MODE, RINGER_MODE_SILENT)
    }

    fun setLastRingerMode(mode: Int) {
        sharedPrefs
            .edit()
            .putInt(LAST_RINGER_MODE, mode)
            .apply()
    }
}