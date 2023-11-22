package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PermissionRequestHandler(
    activity: AppCompatActivity,
    private val callback: (() -> Unit)? = null,
    private val rationaleCallback: (() -> Unit)? = null,
    private val deniedCallback: (() -> Unit)? = null,
) {
    private var currentPermission = ""

    private val singlePermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                callback?.invoke()
            } else {
                if (currentPermission.isNotEmpty() && activity.shouldShowRequestPermissionRationale(
                        currentPermission
                    )
                ) {
                    rationaleCallback?.invoke()
                } else {
                    deniedCallback?.invoke()
                }
            }
        }

    fun requestPermission(permission: String) {
        this.currentPermission = permission
        singlePermissionLauncher.launch(permission)
    }
}
