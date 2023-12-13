package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.UserModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment.AuthorizationFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment.HomeFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment.ProfileFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment.UploadFilmFragment

class MainActivity : AppCompatActivity() {
    private val fragmentContainerId: Int = R.id.main_activity_container

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

    override fun onDestroy() {
        super.onDestroy()
//        repository = null
    }
}
