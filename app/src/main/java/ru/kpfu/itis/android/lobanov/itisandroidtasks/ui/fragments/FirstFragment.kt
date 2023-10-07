package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.kpfu.itis.android.lobanov.itisandroidtasks.MainActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentFirstScreenBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ActionType
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsKey

class FirstFragment : BaseFragment(R.layout.fragment_first_screen) {
    private var _viewBinding: FragmentFirstScreenBinding? = null
    private val viewBinding: FragmentFirstScreenBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentFirstScreenBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY)?.let { message ->
                tv.text = message
            }

            btnFirst.setOnClickListener {
                (requireActivity() as? BaseActivity)?.goToScreen(
                    actionType = ActionType.REPLACE,
                    destination = SecondFragment.newInstance(message = et.text.toString()),
                    tag = SecondFragment.SECOND_SCREEN_FRAGMENT_TAG,
                    isAddToBackStack = true,
                )

                (requireActivity() as? BaseActivity)?.goToScreen(
                    actionType = ActionType.REPLACE,
                    destination = ThirdFragment.newInstance(message = et.text.toString()),
                    tag = ThirdFragment.THIRD_SCREEN_FRAGMENT_TAG,
                    isAddToBackStack = true,
                )
            }

            btnSecond.setOnClickListener {
                (activity as MainActivity).passData(et.text.toString())
                et.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    companion object {
        const val FIRST_SCREEN_FRAGMENT_TAG = "FIRST_SCREEN_FRAGMENT_TAG"

        fun newInstance(message: String) = FirstFragment().apply {
            arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
        }
    }
}
