package ru.kpfu.itis.android.lobanov.itisandroidtasks

import android.os.Bundle
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments.StartPageFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ActionType

class MainActivity : BaseActivity() {

    override val fragmentContainerId: Int = R.id.main_activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            goToScreen(
                ActionType.REPLACE,
                StartPageFragment(),
                StartPageFragment.START_PAGE_FRAGMENT_TAG,
                true
            )
            supportFragmentManager.beginTransaction()
                .replace(
                    fragmentContainerId,
                    StartPageFragment(),
                    StartPageFragment.START_PAGE_FRAGMENT_TAG,
                )
                .commit()
        }
    }

    override fun goToScreen(
        actionType: ActionType,
        destination: BaseFragment,
        tag: String?,
        isAddToBackStack: Boolean,
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
//                this.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            }
        }.commit()
    }
}
