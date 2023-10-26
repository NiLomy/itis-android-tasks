package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter.FragmentAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentViewPagerBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.utils.ParamsKey
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.utils.QuestionGenerator

class ViewPagerFragment : BaseFragment(R.layout.fragment_view_pager) {
    private val binding: FragmentViewPagerBinding by viewBinding(FragmentViewPagerBinding::bind)
    private var vpAdapter: FragmentAdapter? = null
    private var answers: MutableMap<Int, Boolean> = HashMap()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val count = arguments?.getInt(ParamsKey.QUESTIONS_KEY)
        if (count != null) {
            vpAdapter = context?.let { QuestionGenerator.getQuestions(it, count) }?.let {
                FragmentAdapter(
                    manager = parentFragmentManager, lifecycle,
                    it
                )
            }
            binding.apply {
                fragmentVp.adapter = vpAdapter

                fragmentVp.run {
                    registerOnPageChangeCallback(object : OnPageChangeCallback() {
                        override fun onPageScrolled(
                            position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int
                        ) {
                            if (position == 0 && positionOffset == 0.0f) {
                                setCurrentItem(count, false)
                            } else if (position == count + 1 && positionOffset == 0.0f) {
                                setCurrentItem(1, false)
                            }
                        }

                        override fun onPageSelected(position: Int) {
                            val pos = when (position) {
                                0 -> count
                                count + 1 -> 1
                                else -> position
                            }
                            "$pos/$count".also { binding.tvHeader.text = it }
                        }

                        override fun onPageScrollStateChanged(state: Int) {
                            super.onPageScrollStateChanged(state)
                        }
                    })
                    setCurrentItem(1, false)
                }
                btnFinish.setOnClickListener {
                    Toast.makeText(context, "You successfully passed the test!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun answerQuestion(questionNumber: Int) {
        answers[questionNumber] = true
        val count = arguments?.getInt(ParamsKey.QUESTIONS_KEY)
        if (answers.size == count) {
            binding.btnFinish.visibility = View.VISIBLE
        }
    }

    companion object {
        const val VIEW_PAGER_FRAGMENT_TAG = "VIEW_PAGER_FRAGMENT_TAG"

        fun newInstance(count: Int) = ViewPagerFragment().apply {
            arguments = bundleOf(ParamsKey.QUESTIONS_KEY to count)
        }
    }
}
