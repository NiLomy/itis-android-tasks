package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import android.app.Notification.CATEGORY_MESSAGE
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ru.kpfu.itis.android.lobanov.itisandroidtasks.MainActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R

class NotificationProvider(private val context: Context) {
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var builder: NotificationCompat.Builder? = null
    private val pendingIntent: PendingIntent = createPendingIntent()

    fun setUpBuilder(importance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (pair in NotificationParams.priority) {
                if (pair.value == importance) {
                    builder = NotificationCompat.Builder(
                        context,
                        context.getString(R.string.default_notification_channel_id) + pair.key
                    )
                }
            }
            if (builder == null) throw RuntimeException(context.getString(R.string.unsupported_priority_type_text))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun buildNotification() {
        builder
            ?.setSmallIcon(R.drawable.ic_notification)
            ?.setContentTitle(Notification.title)
            ?.setContentText(Notification.content)
            ?.setCategory(CATEGORY_MESSAGE)
            ?.setContentIntent(pendingIntent)
            ?.setAutoCancel(true)

        Notification.importance?.let {
            builder?.setPriority(it)
        }

        Notification.visibility?.let {
            builder?.setVisibility(it)
        }

        if (Notification.isBigText) {
            builder?.setStyle(NotificationCompat.BigTextStyle().bigText(Notification.content))
        }

        if (Notification.isButtonsShown) {
            setButtons()
        }
    }

    private fun setButtons() {
        val startPendingIntent = createPendingIntentWithExtras(
            ParamsKey.INTENT_EXTRA_KEY,
            ParamsKey.INTENT_START_FRAGMENT_VALUE,
            PENDING_INTENT_REQUEST_CODE + 1
        )
        builder?.addAction(R.drawable.ic_home, context.getString(R.string.home), startPendingIntent)

        val settingsPendingIntent = createPendingIntentWithExtras(
            ParamsKey.INTENT_EXTRA_KEY,
            ParamsKey.INTENT_SETTINGS_FRAGMENT_VALUE,
            PENDING_INTENT_REQUEST_CODE + 2
        )
        builder?.addAction(
            R.drawable.ic_notification_settings,
            context.getString(R.string.settings),
            settingsPendingIntent
        )
    }

    fun showNotification() {
        notificationManager.notify(NOTIFICATION_ID++, builder?.build())
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        return PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_CODE,
            intent,
            FLAG_ONE_SHOT or FLAG_IMMUTABLE
        )
    }

    private fun createPendingIntentWithExtras(
        extraKey: String,
        extraValue: String,
        requestCode: Int
    ): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(extraKey, extraValue)

        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            FLAG_ONE_SHOT or FLAG_IMMUTABLE
        )
    }

    companion object {
        private const val PENDING_INTENT_REQUEST_CODE = 100
        private var NOTIFICATION_ID = 1
    }
}
