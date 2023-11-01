package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentStartPageBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ActionType
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.PlanetsDataRepository

class StartPageFragment : BaseFragment(R.layout.fragment_start_page) {

    private var _viewBinding: FragmentStartPageBinding? = null
    private val viewBinding: FragmentStartPageBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentStartPageBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            etNewsCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = etNewsCount.text.toString()
                    if (input.isNotEmpty()) {
                        val planetCount = input.toInt()
                        if (planetCount > PLANETS_COUNT_THRESHOLD) {
                            etNewsCount.removeTextChangedListener(this)
                            etNewsCount.setText(input.substring(0, input.length - 1))
                            etNewsCount.text?.let { etNewsCount.setSelection(it.length) }
                            etNewsCount.addTextChangedListener(this)

                            Toast.makeText(
                                context,
                                getString(R.string.you_should_enter_a_number_between_0_and_45),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}

            })
            btnSubmit.setOnClickListener {
                val planetCount = etNewsCount.text.toString()
                if (isValidInput(planetCount)) {
                    PlanetsDataRepository.clearAll()
                    (requireActivity() as? BaseActivity)?.goToScreen(
                        actionType = ActionType.REPLACE,
                        destination = PlanetsFragment.newInstance(planetCount),
                        tag = PlanetsFragment.PLANETS_FRAGMENT_TAG,
                        isAddToBackStack = true,
                    )
                } else Toast.makeText(
                    context,
                    getString(R.string.you_should_enter_a_number_between_0_and_45),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidInput(input: String): Boolean {
        if (input.isEmpty()) return false
        val planetCount = input.toInt()
        if (planetCount > PLANETS_COUNT_THRESHOLD) return false
        return true
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    companion object {
        const val START_PAGE_FRAGMENT_TAG = "START_PAGE_FRAGMENT_TAG"
        private const val PLANETS_COUNT_THRESHOLD = 45
    }
}
