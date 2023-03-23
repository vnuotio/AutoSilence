package fi.vnuotio.autosilence

import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import fi.vnuotio.autosilence.utils.SharedPrefsHandler
import fi.vnuotio.autosilence.utils.createNeutralPopup

class MainActivity : AppCompatActivity() {
    private lateinit var nm: NotificationManager
    private lateinit var am: AudioManager
    private lateinit var sph: SharedPrefsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        am = getSystemService(AUDIO_SERVICE) as AudioManager
        sph = SharedPrefsHandler(applicationContext)

        setSwitchActions()
    }

    private fun setSwitchActions() {
        val switch = findViewById<SwitchCompat>(R.id.statusSwitch)
        switch.setOnCheckedChangeListener { _, isChecked ->
            val isPermissionGranted = nm.isNotificationPolicyAccessGranted
            val statusText = findViewById<TextView>(R.id.statusText)

            if (isChecked) {
                if (!isPermissionGranted) {
                    switch.isChecked = false
                    showPermissionPopup()
                    return@setOnCheckedChangeListener
                }
                sph.setLastRingerMode(am.ringerMode)
                statusText.text = resources.getText(R.string.statusEnabled)
                // TODO: Enable background functionality
            }
            else {
                if (isPermissionGranted) am.ringerMode = sph.getLastRingerMode()
                statusText.text = resources.getText(R.string.statusDisabled)
                // TODO: Disable background functionality
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