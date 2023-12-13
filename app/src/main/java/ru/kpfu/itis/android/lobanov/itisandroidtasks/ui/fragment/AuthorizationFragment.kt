package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.AppDatabase
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.UserModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentAuthorizationBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.MainActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsConstants
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.PasswordEncrypter

class AuthorizationFragment: Fragment(R.layout.fragment_authorization) {
    private var _viewBinding: FragmentAuthorizationBinding? = null
    private val viewBinding: FragmentAuthorizationBinding
        get() = _viewBinding!!
    private var db: AppDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentAuthorizationBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation)?.visibility = View.GONE
        db = ServiceLocator.getDbInstance()
    }

    private fun initViews() {
        with(viewBinding) {
            authorizeBtn.setOnClickListener {
                val email: String = emailEt.text.toString()
                val password: String = passwordEt.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    makeToast("This fields should not be empty")
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val user: UserModel? = UserRepository.getUser(email, password)
                        if (user == null) {
                            makeToast("There is no such user. Please register in")
                        } else {
                            if (user.deletionDate != null) {
                                val dialog = RestoreDialogFragment(user = user, signIn = ::signIn)
                                dialog.show(parentFragmentManager, RestoreDialogFragment.RESTORE_DIALOG_FRAGMENT_TAG)
                            } else {
                                signIn(user)
                            }
                        }
                    }
                }
            }

            registerBtn.setOnClickListener {
                (activity as MainActivity).navigateTo(
                    RegistrationFragment(),
                    RegistrationFragment.REGISTRATION_FRAGMENT_TAG
                )
            }
        }
    }

    private fun signIn(user: UserModel) {
        ServiceLocator.getSharedPreferences().edit()
            .putString("username", user.name)
            .putString("userPhone", user.phone)
            .putString("userEmail", user.email)
            .putString("userPassword", PasswordEncrypter.encrypt(user.password, ParamsConstants.ENCRYPTING_ALGORITHM))
            .apply()
        (activity as MainActivity).navigateTo(
            HomeFragment(),
            HomeFragment.HOME_FRAGMENT_TAG
        )
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
        const val AUTHORIZATION_FRAGMENT_TAG = "AUTHORIZATION_FRAGMENT_TAG"
    }
}
