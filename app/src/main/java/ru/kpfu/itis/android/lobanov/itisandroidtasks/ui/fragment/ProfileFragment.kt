package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentProfileBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.MainActivity

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

                (requireActivity() as MainActivity).updateUser(
                    userPhone,
                    userEmail,
                    userPassword,
                    newPhone,
                    newEmail,
                    oldPassword,
                    newPassword,
                    confirmPassword,
                    phoneEt,
                    emailEt,
                    oldPasswordEt,
                    newPasswordEt,
                    confirmPasswordEt
                )
            }

            exitIv.setOnClickListener {
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

            deleteIv.setOnClickListener {
                // implement optional task
            }
        }
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
        if (userPassword != oldPassword) {
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
