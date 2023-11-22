package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

object NotificationParams {
    @RequiresApi(Build.VERSION_CODES.N)
    val priority: Map<String, Int> = mapOf(
        Pair("medium", NotificationManager.IMPORTANCE_LOW),
        Pair("high", NotificationManager.IMPORTANCE_DEFAULT),
        Pair("urgent", NotificationManager.IMPORTANCE_HIGH)
    )

    val visibility: Map<String, Int> = mapOf(
        Pair("private", NotificationCompat.VISIBILITY_PRIVATE),
        Pair("secret", NotificationCompat.VISIBILITY_SECRET),
        Pair("public", NotificationCompat.VISIBILITY_PUBLIC)
    )
}