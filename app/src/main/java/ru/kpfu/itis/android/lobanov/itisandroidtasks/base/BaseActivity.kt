package ru.kpfu.itis.android.lobanov.itisandroidtasks.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.utils.ActionType

abstract class BaseActivity : AppCompatActivity() {
    protected abstract val fragmentContainerId: Int

    abstract fun goToScreen(
        actionType: ActionType,
        destination: BaseFragment,
        tag: String? = null,
        isAddToBackStack: Boolean = true,
    )
}
