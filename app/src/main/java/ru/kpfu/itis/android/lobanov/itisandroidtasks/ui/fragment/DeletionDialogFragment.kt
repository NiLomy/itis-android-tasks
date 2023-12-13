package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.background.greedy.DelayedWorkTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.DeleteUserWorker
import java.sql.Date
import java.util.Calendar
import java.util.concurrent.TimeUnit

class DeletionDialogFragment(
    private val userEmail: String,
    private val clearData: ((SharedPreferences) -> Unit)
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage("Are you sure you want to delete your current account?")
            .setPositiveButton("YES") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val calendar: Calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val deletionDate = Date.valueOf("$year-$month-$day")
                    UserRepository.setDeletionDate(userEmail, deletionDate)
                    val sharedPref: SharedPreferences = ServiceLocator.getSharedPreferences()
                    clearData(sharedPref)

                    val constraints: Constraints = Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                    val data = Data.Builder()
                    data.putString("userEmail", userEmail)

                    val deleteUserWork = OneTimeWorkRequestBuilder<DeleteUserWorker>()
                        .setConstraints(constraints)
                        .setInitialDelay(7, TimeUnit.DAYS)
                        .addTag("DELETE_USER_WORK_TAG")
                        .setInputData(data.build())
                        .build()
                    WorkManager.getInstance(requireContext()).enqueueUniqueWork("delayedDeleteUser", ExistingWorkPolicy.KEEP, deleteUserWork)
                    this@DeletionDialogFragment.dismiss()
                }
            }
            .setNegativeButton("NO") { _, _ ->
                this.dismiss()
            }
        return builder.create()
    }

    companion object {
        const val DELETION_DIALOG_FRAGMENT_TAG = "DELETION_DIALOG_FRAGMENT_TAG"
    }
}