package fi.vnuotio.autosilence

import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.RINGER_MODE_SILENT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import fi.vnuotio.autosilence.background.ScreenStatusService
import fi.vnuotio.autosilence.utils.SharedPrefsHandler
import fi.vnuotio.autosilence.utils.createNeutralPopup

class MainActivity : AppCompatActivity() {
    // Managers + handlers
    private lateinit var nm: NotificationManager
    private lateinit var am: AudioManager
    private lateinit var sph: SharedPrefsHandler

    // UI
    private lateinit var statusText: TextView
    private lateinit var switch: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        am = getSystemService(AUDIO_SERVICE) as AudioManager
        sph = SharedPrefsHandler(applicationContext)

        statusText = findViewById(R.id.statusText)
        switch = findViewById(R.id.statusSwitch)

        setSwitchStatus()

        setSwitchActions()
    }

    private fun setSwitchStatus() {
        val isEnabled = sph.isEnabled()

        statusText.text = if (isEnabled) {
            resources.getText(R.string.statusEnabled)
        }
        else {
            resources.getText(R.string.statusDisabled)
        }

        switch.isChecked = isEnabled
    }

    private fun setSwitchActions() {
        switch.setOnCheckedChangeListener { _, isChecked ->
            val isPermissionGranted = nm.isNotificationPolicyAccessGranted
            val intent = Intent(applicationContext, ScreenStatusService::class.java)

            // Service enabled
            if (isChecked) {
                if (!isPermissionGranted) {
                    switch.isChecked = false
                    showPermissionPopup()
                    return@setOnCheckedChangeListener
                }
                sph.setLastRingerMode(am.ringerMode)
                am.ringerMode = RINGER_MODE_SILENT

                statusText.text = resources.getText(R.string.statusEnabled)
                sph.setEnabledStatus(true)

                startForegroundService(intent)
            } // Service disabled
            else {
                stopService(intent)

                if (isPermissionGranted) am.ringerMode = sph.getLastRingerMode()

                statusText.text = resources.getText(R.string.statusDisabled)
                sph.setEnabledStatus(false)
            }
        }
    }

    private fun showPermissionPopup() {
        val listener = DialogInterface.OnClickListener {_, _ ->
            requestNotificationPermissions()
        }
        createNeutralPopup(this,
            "Permission required",
            "'Do not disturb' access is required for this app to function",
            listener)
    }

    private fun requestNotificationPermissions() {
        val intent = Intent(ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        startActivity(intent)
    }
}