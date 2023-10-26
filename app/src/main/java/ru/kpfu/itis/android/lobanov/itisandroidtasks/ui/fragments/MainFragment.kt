package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentMainBinding

class MainFragment : BaseFragment(R.layout.fragment_main) {
    private var _viewBinding: FragmentMainBinding? = null
    private val viewBinding: FragmentMainBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentMainBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            val maxQuestionNumber = context.let { resources.getStringArray(R.array.questions).size }
            buildPhoneMask(et1)

            et1.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (et1.text.isEmpty()) {
                        et1.setText("+7 (9")
                    }
                } else {
                    if (!isValidPhone(et1.text.toString())) {
                        et1.error = "You entered invalid phone"
                    }
                    enableButton(btn, et1, et2, maxQuestionNumber)
                }
            }

            et2.addTextChangedListener {
                if (et2.text.isEmpty()) {
                    et2.error = "You entered wrong number"
                } else if (et2.text.toString().toInt() >= maxQuestionNumber || et2.text.toString()
                        .toInt() <= 0
                ) {
                    et2.error = "Number should be between 1 and $maxQuestionNumber"
                }

                enableButton(btn, et1, et2, maxQuestionNumber)
            }

            btn.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(
                    R.id.main_activity_container,
                    ViewPagerFragment.newInstance(et2.text.toString().toInt()),
                    ViewPagerFragment.VIEW_PAGER_FRAGMENT_TAG
                ).addToBackStack(null).commit()
            }
        }
    }

    private fun enableButton(btn: Button, et1: EditText, et2: EditText, maxQuestionNumber: Int) {
        btn.isEnabled =
            isQuestionsCountValid(
                et2.text.toString(),
                maxQuestionNumber
            ) && isValidPhone(et1.text.toString())
    }

    private fun isQuestionsCountValid(countOfQuestions: String, maxQuestionNumber: Int): Boolean {
        return !(countOfQuestions.isEmpty() || countOfQuestions[0] == '0' || countOfQuestions.toInt() > maxQuestionNumber)
    }

    private fun buildPhoneMask(editText: EditText) {
        var lastChar = ' '

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                s?.let {
                    val countOfChars = s.length
                    if (countOfChars != 0) {
                        lastChar = editText.text.last()
                    }
                    if (countOfChars <= 5) {
                        editText.removeTextChangedListener(this)
                        editText.setText("+7 (9")
                        editText.setSelection(editText.text.length)
                        editText.addTextChangedListener(this)
                    }
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    val countOfChars = editText.text.length
                    if (before > 0) {
                        if (lastChar == '-') {
                            editText.removeTextChangedListener(this)
                            if (s.last() == ')') {
                                editText.setText(editText.text.substring(0, countOfChars - 2))
                            } else {
                                editText.setText(editText.text.substring(0, countOfChars - 1))
                            }
                            editText.setSelection(editText.text.length)
                            editText.addTextChangedListener(this)
                        }
                    } else {
                        when (countOfChars) {
                            5 -> {
                                editText.removeTextChangedListener(this)
                                if (s.length == 1) {
                                    editText.append(s)
                                } else {
                                    editText.append(s.substring(5))
                                }
                                editText.setSelection(editText.text.length)
                                editText.addTextChangedListener(this)
                            }

                            7 -> {
                                editText.removeTextChangedListener(this)
                                editText.append(")-")
                                editText.setSelection(editText.text.length)
                                editText.addTextChangedListener(this)
                            }

                            12, 15 -> {
                                editText.removeTextChangedListener(this)
                                editText.append("-")
                                editText.setSelection(editText.text.length)
                                editText.addTextChangedListener(this)
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^\\+7 \\(9\\d{2}\\)-\\d{3}-\\d{2}-\\d{2}\$"))
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    companion object {
        const val MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG"
    }
}
