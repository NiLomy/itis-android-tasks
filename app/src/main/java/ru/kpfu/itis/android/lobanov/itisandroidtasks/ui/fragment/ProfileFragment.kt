package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.UserModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentProfileBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.MainActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsConstants
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.PasswordEncrypter

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _viewBinding: FragmentProfileBinding? = null
    private val viewBinding: FragmentProfileBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentProfileBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            val sharedPref: SharedPreferences = ServiceLocator.getSharedPreferences()
            val username: String = sharedPref.getString("username", "").toString()
            val userPhone: String = sharedPref.getString("userPhone", "").toString()
            val userEmail: String = sharedPref.getString("userEmail", "").toString()
            val userPassword: String = sharedPref.getString("userPassword", "").toString()

            nameEt.setText(username)
            phoneEt.setText(userPhone)
            emailEt.setText(userEmail)

            applyChangesBtn.setOnClickListener {
                val newPhone: String = phoneEt.text.toString()
                val newEmail: String = emailEt.text.toString()
                val oldPassword: String = oldPasswordEt.text.toString()
                val newPassword: String = newPasswordEt.text.toString()
                val confirmPassword: String = confirmPasswordEt.text.toString()

                updateUser(
                    userPhone,
                    userEmail,
                    userPassword,
                    newPhone,
                    newEmail,
                    oldPassword,
                    newPassword,
                    confirmPassword
                )
            }

            exitIv.setOnClickListener {
                clearData(sharedPref)
            }

            deleteIv.setOnClickListener {
                val dialog = DeletionDialogFragment(userEmail = userEmail, clearData = ::clearData)
                dialog.show(parentFragmentManager, DeletionDialogFragment.DELETION_DIALOG_FRAGMENT_TAG)
            }
        }
    }

    private fun updateUser(
        userPhone: String,
        userEmail: String,
        userPassword: String,
        newPhone: String,
        newEmail: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        with(viewBinding) {
            lifecycleScope.launch(Dispatchers.IO) {
                val currentUser = UserRepository.getUser(userEmail, userPassword)
                if (newPhone != userPhone) {
                    val user: UserModel? = UserRepository.getUserByPhone(newPhone)
                    if (user != null) {
                        makeToast("User with such phone number is currently exists")
                    } else {
                        currentUser?.let { userModel ->
                            UserRepository.updatePhone(
                                userModel,
                                newPhone
                            )
                        }
                        ServiceLocator.getSharedPreferences().edit().putString("userPhone", newPhone)
                            .apply()
                        activity?.runOnUiThread {
                            phoneEt.setText(newPhone)
                        }
                    }
                }
                if (newEmail != userEmail) {
                    val user: UserModel? = UserRepository.getUserByEmail(newEmail)
                    if (user != null) {
                        makeToast("User with such phone number is currently exists")
                    } else {
                        currentUser?.let { userModel ->
                            UserRepository.updateEmail(
                                userModel,
                                newEmail
                            )
                        }
                        ServiceLocator.getSharedPreferences().edit().putString("userEmail", newEmail)
                            .apply()
                        activity?.runOnUiThread {
                            emailEt.setText(newEmail)
                        }
                    }
                }
                if (isPasswordInputsValid(userPassword, oldPassword, newPassword, confirmPassword)) {
                    currentUser?.let { userModel ->
                        UserRepository.updatePassword(
                            userModel,
                            newPassword
                        )
                    }
                    activity?.runOnUiThread {
                        oldPasswordEt.setText("")
                        newPasswordEt.setText("")
                        confirmPasswordEt.setText("")
                    }
                }
            }
        }
    }

    private fun clearData(sharedPref: SharedPreferences) {
        // use remove instead of clear for a more safe logic
        sharedPref.edit()
            .remove("username")
            .remove("userPhone")
            .remove("userEmail")
            .apply()
        (activity as MainActivity).navigateTo(
            AuthorizationFragment(),
            AuthorizationFragment.AUTHORIZATION_FRAGMENT_TAG
        )
    }

    private fun isPasswordInputsValid(
        userPassword: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        if (oldPassword.isEmpty() && newPassword.isEmpty() && confirmPassword.isEmpty()) return false

        if (oldPassword.isEmpty() && newPassword.isNotEmpty()) {
            makeToast("You should enter your current password")
            return false
        }
        if (userPassword != PasswordEncrypter.encrypt(oldPassword, ParamsConstants.ENCRYPTING_ALGORITHM)) {
            makeToast("You entered wrong password")
            return false
        }
        if (oldPassword.isNotEmpty() && newPassword.isEmpty()) {
            makeToast("You should enter your new password to change")
            return false
        }
        if (newPassword != confirmPassword) {
            makeToast("Passwords doesn't match")
            return false
        }
        return true
    }

    private fun makeToast(text: String) {
        activity?.runOnUiThread {
            Toast.makeText(
                context,
                text,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        const val PROFILE_FRAGMENT_TAG = "PROFILE_FRAGMENT_TAG"
    }
}
