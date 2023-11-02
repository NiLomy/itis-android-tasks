package ru.kpfu.itis.android.lobanov.itisandroidtasks.base

import androidx.appcompat.app.AppCompatActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ActionType

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val fragmentContainerId: Int

    abstract fun goToScreen(
        actionType: ActionType,
        destination: BaseFragment,
        tag: String? = null,
        isAddToBackStack: Boolean = true,
    )
}
