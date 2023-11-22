package ru.kpfu.itis.android.lobanov.itisandroidtasks

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments.CoroutineSettingsFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments.NotificationSettingsFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments.StartFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.AirplaneModeHandler
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.CoroutineSettings
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.NotificationParams
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsKey
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.PermissionRequestHandler

class MainActivity : AppCompatActivity() {
    private val fragmentContainerId: Int = R.id.main_activity_container
    private var permissionRequestHandler: PermissionRequestHandler? = null
    private var coroutineJob: Job? = null
    private var canceledCoroutinesCount: Int = 0
    private var isCoroutinesStopOnBackground: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleAirplaneMode()
        setUpBottomNavigation()
        setUpPermissionHandler()
        createChannels()

        if (Build.VERSION.SDK_INT >= TIRAMISU) {
            requestPermission(permission = POST_NOTIFICATIONS)
        }

        if (savedInstanceState == null) {
            navigateTo(StartFragment(), StartFragment.START_FRAGMENT_TAG)
        }
    }

    private fun handleAirplaneMode() {
        val airplaneOnView: ConstraintLayout = findViewById(R.id.airplane_mode_on_cl)
        val airplaneModeHandler = AirplaneModeHandler(this)
        airplaneModeHandler.handle {
            airplaneOnView.isGone = !it
        }
    }

    private fun setUpBottomNavigation() {
        val bnv: BottomNavigationView = findViewById(R.id.main_bnv)
        bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main_start -> {
                    navigateTo(
                        StartFragment(),
                        StartFragment.START_FRAGMENT_TAG
                    )
                    true
                }

                R.id.main_notification_settings -> {
                    navigateTo(
                        NotificationSettingsFragment(),
                        NotificationSettingsFragment.NOTIFICATION_SETTINGS_FRAGMENT_TAG
                    )
                    true
                }

                R.id.main_coroutine_settings -> {
                    navigateTo(
                        CoroutineSettingsFragment(),
                        CoroutineSettingsFragment.COROUTINE_SETTINGS_FRAGMENT_TAG
                    )
                    true
                }

                else -> false
            }
        }
    }

    private fun setUpPermissionHandler() {
        permissionRequestHandler = PermissionRequestHandler(
            activity = this,
            callback = {
                Toast.makeText(
                    this,
                    getString(R.string.gave_permission, getString(R.string.sending_notifications)),
                    Toast.LENGTH_SHORT
                )
                    .show()
            },
            rationaleCallback = {
                val dialog = AlertDialog.Builder(this)
                    .setTitle(
                        getString(
                            R.string.permission_denied_pattern,
                            getString(R.string.notifications)
                        )
                    )
                    .setMessage(
                        getString(R.string.rational_message_text)
                    )
                    .setPositiveButton(
                        getString(R.string.confirm),
                        null
                    )
                    .show()
                val b = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                b.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= TIRAMISU) {
                        requestPermission(permission = POST_NOTIFICATIONS)
                    }
                    dialog.dismiss()
                }
            },
            deniedCallback = {
                AlertDialog.Builder(this)
                    .setTitle(
                        getString(
                            R.string.permission_denied_pattern,
                            getString(R.string.notifications)
                        )
                    )
                    .setMessage(
                        getString(
                            R.string.denied_permission_message_text,
                            getString(R.string.sending_notifications)
                        )
                    )
                    .setPositiveButton(
                        getString(R.string.go_to_settings)
                    ) { _, _ ->
                        val appSettingsIntent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + this.packageName)
                        )
                        startActivity(appSettingsIntent)
                    }
                    .show()
            }
        )
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (pair in NotificationParams.priority) {
                createNotificationChannel(
                    getString(R.string.default_notification_channel_id, pair.key),
                    getString(R.string.default_notification_channel_name, pair.key),
                    pair.value
                )
            }
        }
    }

    private fun createNotificationChannel(
        id: String,
        name: String,
        importance: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                id,
                name,
                importance
            ).also {
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                    it
                )
            }
        }
    }

    private fun requestPermission(permission: String) {
        permissionRequestHandler?.requestPermission(permission)
    }

    private fun navigateTo(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                fragmentContainerId,
                fragment,
                fragmentTag,
            )
            .commit()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val bundle: Bundle? = intent?.extras

        if (bundle != null) {
            val text = bundle.getString(ParamsKey.INTENT_EXTRA_KEY)
            if (text == ParamsKey.INTENT_START_FRAGMENT_VALUE) {
                Toast.makeText(
                    this,
                    getString(R.string.main_page_greetings_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (text == ParamsKey.INTENT_SETTINGS_FRAGMENT_VALUE) {
                navigateTo(
                    NotificationSettingsFragment(),
                    NotificationSettingsFragment.NOTIFICATION_SETTINGS_FRAGMENT_TAG
                )
            }
        }
    }

    fun startCoroutines() {
        canceledCoroutinesCount = CoroutineSettings.coroutinesCount
        isCoroutinesStopOnBackground = CoroutineSettings.isStopOnBackground

        coroutineJob = lifecycleScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    repeat(CoroutineSettings.coroutinesCount) {
                        if (CoroutineSettings.isAsync) {
                            launch {
                                coroutineTask(it)
                            }
                        } else {
                            coroutineTask(it)
                        }
                    }
                }
            }.onSuccess {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.my_job_here_is_done),
                    Toast.LENGTH_SHORT
                ).show()
            }.onFailure {
                Log.e(
                    this@MainActivity.localClassName,
                    getString(R.string.cancelled_coroutines, canceledCoroutinesCount),
                    it
                )
            }
        }
        coroutineJob = null
    }

    private suspend fun coroutineTask(i: Int) {
        delay(2000)
        println(getString(R.string.coroutine_finish_text, i))
        canceledCoroutinesCount--
    }

    override fun onStop() {
        super.onStop()
        if (isCoroutinesStopOnBackground) {
            coroutineJob?.cancel(getString(R.string.user_cancelled_coroutines_by_collapsing_the_application))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionRequestHandler = null
    }
}
