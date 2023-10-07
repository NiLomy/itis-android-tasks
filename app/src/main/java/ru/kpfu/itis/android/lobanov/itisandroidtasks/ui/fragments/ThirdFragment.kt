package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentThirdScreenBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsKey

class ThirdFragment : BaseFragment(R.layout.fragment_third_screen) {
    private var _viewBinding: FragmentThirdScreenBinding? = null
    private val viewBinding: FragmentThirdScreenBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentThirdScreenBinding.inflate(inflater)
        return viewBinding.root
    }

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
                    tv.text = getString(R.string.third_screen)
                }
            }
        }
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    companion object {
        const val THIRD_SCREEN_FRAGMENT_TAG = "THIRD_SCREEN_FRAGMENT_TAG"

        fun newInstance(message: String) = ThirdFragment().apply {
            arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
        }
    }
}
