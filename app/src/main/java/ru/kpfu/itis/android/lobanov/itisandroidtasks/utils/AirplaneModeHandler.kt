package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Settings

class AirplaneModeHandler(context: Context) {
    private var isAirplaneModeOn: Boolean = Settings.Global.getInt(
        context.contentResolver,
        Settings.Global.AIRPLANE_MODE_ON,
        0
    ) != 0
    private var callback: ((Boolean) -> Unit)? = null

    init {
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                isAirplaneModeOn = intent?.getBooleanExtra("state", false) == true
                callback?.invoke(isAirplaneModeOn)
            }
        }
        context.registerReceiver(receiver, intentFilter)
    }

    fun handle(callback: (Boolean) -> Unit) {
        this.callback = callback
        this.callback?.invoke(isAirplaneModeOn)
    }
}
