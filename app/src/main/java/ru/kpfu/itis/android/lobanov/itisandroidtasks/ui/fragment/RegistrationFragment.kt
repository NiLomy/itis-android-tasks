package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.UserModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentRegistrationBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.MainActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsConstants
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.PasswordEncrypter

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private var _viewBinding: FragmentRegistrationBinding? = null
    private val viewBinding: FragmentRegistrationBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentRegistrationBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            registrationBtn.setOnClickListener {
                val name: String = nameEt.text.toString()
                val phone: String = phoneEt.text.toString()
                val email: String = emailEt.text.toString()
                val password: String = passwordEt.text.toString()
                val confirmPassword: String = confirmPasswordEt.text.toString()

                if (isFieldsValid(name, phone, email, password, confirmPassword)) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        if (UserRepository.getUserByPhone(phone) != null) {
                            makeToast(getString(R.string.this_phone_is_currently_in_use))
                        } else if (UserRepository.getUserByEmail(email) != null) {
                            makeToast(getString(R.string.this_email_is_currently_in_use))
                        } else {
                            val user: UserModel? = UserRepository.getUser(email, PasswordEncrypter.encrypt(
                                password,
                                ParamsConstants.ENCRYPTING_ALGORITHM
                            ))
                            if (user != null) {
                                makeToast(getString(R.string.this_user_currently_exists_please_log_in))
                            } else {
                                UserRepository.save(UserModel(0, name, phone, email, PasswordEncrypter.encrypt(
                                    password,
                                    ParamsConstants.ENCRYPTING_ALGORITHM
                                ), null))
                                (activity as MainActivity).navigateTo(
                                    AuthorizationFragment(),
                                    AuthorizationFragment.AUTHORIZATION_FRAGMENT_TAG
                                )
                            }
                        }
                    }
                }
            }

            loginBtn.setOnClickListener {
                (activity as MainActivity).navigateTo(
                    AuthorizationFragment(),
                    AuthorizationFragment.AUTHORIZATION_FRAGMENT_TAG
                )
            }
        }
    }

    private fun isFieldsValid(
        name: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            makeToast(getString(R.string.you_should_fill_all_the_fields))
            return false
        }
        if (!phone.matches(Regex(ParamsConstants.PHONE_REGEX))) {
            makeToast(getString(R.string.this_is_not_correct_phone))
            return false
        }
        if (!email.matches(Regex(ParamsConstants.EMAIL_REGEX))) {
            makeToast(getString(R.string.this_is_not_correct_email))
            return false
        }
        if (password != confirmPassword) {
            makeToast(getString(R.string.your_passwords_don_t_match))
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
        const val REGISTRATION_FRAGMENT_TAG = "REGISTRATION_FRAGMENT_TAG"
    }
}
