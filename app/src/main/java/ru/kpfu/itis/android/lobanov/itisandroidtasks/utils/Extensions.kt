package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import android.util.DisplayMetrics

fun Int.getValueInPx(dm: DisplayMetrics): Int {
    return (this * dm.density).toInt()
}
