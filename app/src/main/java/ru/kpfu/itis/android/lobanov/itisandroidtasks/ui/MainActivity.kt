package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.FilmAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.FIlmRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmRVModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.UserModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment.AuthorizationFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment.HomeFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment.ProfileFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment.UploadFilmFragment
import java.sql.Date

class MainActivity : AppCompatActivity() {
    private val fragmentContainerId: Int = R.id.main_activity_container
//    private var repository: Repository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnv: BottomNavigationView = findViewById(R.id.main_bottom_navigation)
        bnv.visibility = View.GONE
        bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    navigateTo(
                        HomeFragment(),
                        HomeFragment.HOME_FRAGMENT_TAG
                    )
                    true
                }

                R.id.profileFragment -> {
                    navigateTo(
                        ProfileFragment(),
                        ProfileFragment.PROFILE_FRAGMENT_TAG
                    )
                    true
                }

                R.id.uploadFilmFragment -> {
                    navigateTo(
                        UploadFilmFragment(),
                        UploadFilmFragment.UPLOAD_FILM_FRAGMENT_TAG
                    )
                    true
                }

                else -> false
            }
        }

        bnv.setOnItemReselectedListener {}

        val username: String? = ServiceLocator.getSharedPreferences().getString("username", "")
        if (username.isNullOrEmpty()) {
            if (savedInstanceState == null) {
                navigateTo(
                    AuthorizationFragment(),
                    AuthorizationFragment.AUTHORIZATION_FRAGMENT_TAG
                )
            }
        } else {
            navigateTo(HomeFragment(), HomeFragment.HOME_FRAGMENT_TAG)
        }
    }

    fun navigateTo(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                fragmentContainerId,
                fragment,
                fragmentTag,
            )
            .addToBackStack(null)
            .commit()
    }

    fun updateUser(
        userPhone: String,
        userEmail: String,
        userPassword: String,
        newPhone: String,
        newEmail: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
        phoneEt: EditText,
        emailEt: EditText,
        oldPasswordEt: EditText,
        newPasswordEt: EditText,
        confirmPasswordEt: EditText
    ) {
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
                    ServiceLocator.getSharedPreferences().edit().putString("userPhone", newPhone).apply()
                    runOnUiThread {
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
                    ServiceLocator.getSharedPreferences().edit().putString("userEmail", newEmail).apply()
                    runOnUiThread {
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
                runOnUiThread {
                    oldPasswordEt.setText("")
                    newPasswordEt.setText("")
                    confirmPasswordEt.setText("")
                }
            }
        }
    }

    fun uploadFilm(filmName: String, date: String, description: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val film: FilmModel? = FIlmRepository.getFilm(filmName, Date(date.toLong()))
            if (film != null) {
                makeToast("This film currently exists")
            } else {
                FIlmRepository.save(FilmModel(filmName, Date(date.toLong()), description))
            }
        }
    }

    fun showAllFilms(filmAdapter: FilmAdapter?, noFilmsTv: TextView) {
        lifecycleScope.async(Dispatchers.IO) {
            val result: MutableList<FilmRVModel> = ArrayList()
            val films: List<FilmModel>? = FIlmRepository.getAllByDateDesc()
            if (films != null) {
                for (i in films.indices) {
                    val f = films[i]
                    result.add(FilmRVModel(i, f.name, f.date, f.description, false))
                }
                runOnUiThread {
                    filmAdapter?.setItems(result)

                }
                runOnUiThread {
                    noFilmsTv.visibility = View.GONE
                }
            } else {
                runOnUiThread {
                    noFilmsTv.visibility = View.VISIBLE
                }
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
        if (newPassword != confirmPassword) {
            makeToast("Passwords doesn't match")
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
        return true
    }

    private fun makeToast(text: String) {
        runOnUiThread {
            Toast.makeText(
                this,
                text,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        repository = null
    }
}
