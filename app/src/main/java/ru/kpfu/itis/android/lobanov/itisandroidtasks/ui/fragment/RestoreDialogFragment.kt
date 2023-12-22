package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.UserModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsConstants

class RestoreDialogFragment(
    private val user: UserModel,
    private val signIn: ((UserModel) -> Unit)
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.your_account_was_deleted_do_you_want_to_restore_your_account))
            .setPositiveButton(getString(R.string.restore)) { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    UserRepository.setDeletionDate(user.email, null)
                    WorkManager.getInstance(requireContext()).cancelUniqueWork(ParamsConstants.DELETE_USER_WORK_NAME)
                    val userModel = UserRepository.getUserByEmail(user.email)
                    if (userModel == null) {
                        UserRepository.save(user)
                    }
                    signIn(user)
                    this@RestoreDialogFragment.dismiss()
                }
            }
            .setNegativeButton(getString(R.string.delete_permanently)) { _, _ ->
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