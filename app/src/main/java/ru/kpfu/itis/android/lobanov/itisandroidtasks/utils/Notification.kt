package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import android.os.Build
import androidx.annotation.RequiresApi

object Notification {
    var title = ""
    var content = ""
    @RequiresApi(Build.VERSION_CODES.N)
    var importance = NotificationParams.priority["high"]
    var visibility = NotificationParams.visibility["public"]
    var isBigText = false
    var isButtonsShown = false
}