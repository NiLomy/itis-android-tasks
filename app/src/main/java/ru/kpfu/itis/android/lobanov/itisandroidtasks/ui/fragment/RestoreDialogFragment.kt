package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.UserModel

class RestoreDialogFragment(
    private val user: UserModel,
    private val signIn: ((UserModel) -> Unit)
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage("Your account was deleted. Do you want to restore your account?")
            .setPositiveButton("RESTORE") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    UserRepository.setDeletionDate(user.email, null)
                    WorkManager.getInstance(requireContext()).cancelUniqueWork("delayedDeleteUser")
                    val userModel = UserRepository.getUserByEmail(user.email)
                    if (userModel == null) {
                        UserRepository.save(user)
                    }
                    signIn(user)
                    this@RestoreDialogFragment.dismiss()
                }
            }
            .setNegativeButton("DELETE PERMANENTLY") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    UserRepository.delete(user.email)
                    this@RestoreDialogFragment.dismiss()
                }
            }
        return builder.create()
    }

    companion object {
        const val RESTORE_DIALOG_FRAGMENT_TAG = "RESTORE_DIALOG_FRAGMENT_TAG"
    }
}