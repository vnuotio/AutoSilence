package fi.vnuotio.autosilence.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

fun createNeutralPopup(context: Context, title: String, body: String,
                       listener: DialogInterface.OnClickListener?) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(body)
        .setNeutralButton("Ok", listener)
        .show()
}