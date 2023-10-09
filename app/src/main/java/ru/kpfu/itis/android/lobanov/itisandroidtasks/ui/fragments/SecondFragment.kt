package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentSecondScreenBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ActionType
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsKey

class SecondFragment : BaseFragment(R.layout.fragment_second_screen) {
    private val viewBinding: FragmentSecondScreenBinding by viewBinding(FragmentSecondScreenBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY)?.let { message ->
                if (message != "") {
                    tv.text = message
                } else {
                    tv.text = getString(R.string.second_screen)
                }

                btn1.setOnClickListener {
                    parentFragmentManager.popBackStack()

                    (requireActivity() as? BaseActivity)?.goToScreen(
                        actionType = ActionType.REPLACE,
                        destination = ThirdFragment.newInstance(
                            message = message
                        ),
                        tag = ThirdFragment.THIRD_SCREEN_FRAGMENT_TAG,
                        isAddToBackStack = true,
                    )
                }
            }

            btn2.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    companion object {
        const val SECOND_SCREEN_FRAGMENT_TAG = "SECOND_SCREEN_FRAGMENT_TAG"

        fun newInstance(message: String) = SecondFragment().apply {
            arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
        }
    }
}
