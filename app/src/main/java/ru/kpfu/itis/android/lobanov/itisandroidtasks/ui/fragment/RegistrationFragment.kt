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

class RegistrationFragment: Fragment(R.layout.fragment_registration) {
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
                        val user: UserModel? = UserRepository.getUser(email, password)
                        if (user != null) {
                           makeToast("This user currently exists. Please log in")
                        } else {
                            UserRepository.save(UserModel(0, name, phone, email, password, null))
                            (activity as MainActivity).navigateTo(
                                AuthorizationFragment(),
                                AuthorizationFragment.AUTHORIZATION_FRAGMENT_TAG
                            )
                        }
                    }
                }
            }
        }
    }

    private fun isFieldsValid(name: String, phone: String, email: String, password: String, confirmPassword: String): Boolean {
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return false
        }
        if (password != confirmPassword) {
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
