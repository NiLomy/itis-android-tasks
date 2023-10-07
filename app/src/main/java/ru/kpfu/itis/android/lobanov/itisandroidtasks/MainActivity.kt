package ru.kpfu.itis.android.lobanov.itisandroidtasks

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentContainerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments.FirstFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments.FourthFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ActionType

class MainActivity : BaseActivity() {
    override val fragmentContainerId: Int = R.id.main_activity_container
    private var counter: Int = 0
    private var texts = arrayOf(
        "Fourth screen",
        "Fourth screen",
        "Fourth screen"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val projectName = application.applicationInfo.packageName.split(".").last()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    fragmentContainerId,
                    FirstFragment.newInstance(message = projectName),
                    FirstFragment.FIRST_SCREEN_FRAGMENT_TAG,
                ).add(
                    R.id.fragment_fourth,
                    FourthFragment.newInstance(message = ""),
                    FourthFragment.FOURTH_SCREEN_FRAGMENT_TAG,
                )
                .commit()
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById<FragmentContainerView>(R.id.fragment_fourth).visibility = View.VISIBLE
        } else {
            findViewById<FragmentContainerView>(R.id.fragment_fourth).visibility = View.GONE
        }
    }

    fun passData(input: String) {
        texts[counter % 3] = input
        counter++

        val bundle = Bundle()
        bundle.putString("message1", texts[0])
        bundle.putString("message2", texts[1])
        bundle.putString("message3", texts[2])

        val transaction =
            this.supportFragmentManager.beginTransaction()
        val fragment = FourthFragment()
        fragment.arguments = bundle
        transaction.replace(R.id.fragment_fourth, fragment)
        transaction.commit()
    }


    override fun goToScreen(
        actionType: ActionType,
        destination: BaseFragment,
        tag: String?,
        isAddToBackStack: Boolean
    ) {
        supportFragmentManager.beginTransaction().apply {
            when (actionType) {
                ActionType.ADD -> {
                    this.add(fragmentContainerId, destination, tag)
                }

                ActionType.REPLACE -> {
                    this.replace(fragmentContainerId, destination, tag)
                }

                ActionType.REMOVE -> {
                    this.remove(destination)
                }

                else -> Unit
            }
            if (isAddToBackStack) {
                this.addToBackStack(null)
            }
        }.commit()
    }
}
