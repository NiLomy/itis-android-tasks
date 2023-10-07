package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentFourthScreenBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsKey

class FourthFragment : BaseFragment(R.layout.fragment_fourth_screen) {
    private var _binding: FragmentFourthScreenBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFourthScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initViews() {
        with(binding) {
            arguments?.getString("message1")?.let { message1 ->
                arguments?.getString("message2")?.let { message2 ->
                    arguments?.getString("message3")?.let { message3 ->
                        tv1.text = message1
                        tv2.text = message2
                        tv3.text = message3
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val FOURTH_SCREEN_FRAGMENT_TAG = "FOURTH_SCREEN_FRAGMENT_TAG"

        fun newInstance(message: String) = FourthFragment().apply {
            arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
        }
    }
}