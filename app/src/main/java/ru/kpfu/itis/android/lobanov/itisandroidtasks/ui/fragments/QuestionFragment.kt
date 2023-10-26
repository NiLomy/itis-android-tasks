package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter.QuestionAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentQuestionBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.Question
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.utils.ParamsKey

class QuestionFragment : BaseFragment(R.layout.fragment_question) {
    private val viewBinding: FragmentQuestionBinding by viewBinding(FragmentQuestionBinding::bind)
    private var rvAdapter: QuestionAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val question: Question = arguments?.getSerializable(ParamsKey.QUESTIONS_KEY) as Question
        initView(question)
    }

    private fun initView(question: Question) {
        viewBinding.apply {
            tvQuestion.text = question.questionCondition
            rvAdapter = QuestionAdapter(
                items = question.answers,
                onItemChecked = { position -> update(position) }
            )
            { position -> update(position) }
            rvQuestionnaire.adapter = rvAdapter
        }
    }

    private fun update(position: Int) {
        rvAdapter?.let { adapter ->
            val previousSelection = adapter.items.indexOfFirst { it.isChecked }
            if (previousSelection != -1) {
                adapter.items[previousSelection].isChecked = false
                adapter.notifyItemChanged(previousSelection)
            }
            adapter.items[position].isChecked = true
            adapter.notifyItemChanged(position)
            val vpFragment =
                requireActivity().supportFragmentManager.findFragmentByTag(ViewPagerFragment.VIEW_PAGER_FRAGMENT_TAG) as ViewPagerFragment
            val currentQuestion = arguments?.getInt(ParamsKey.CURRENT_QUESTION_KEY)
            if (currentQuestion != null) {
                vpFragment.answerQuestion(currentQuestion)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        rvAdapter = null
    }

    companion object {
        const val QUESTION_FRAGMENT_TAG = "QUESTION_FRAGMENT_TAG"

        fun newInstance(questions: Question, currentQuestionNumber: Int) =
            QuestionFragment().apply {
                arguments = bundleOf(
                    ParamsKey.QUESTIONS_KEY to questions,
                    ParamsKey.CURRENT_QUESTION_KEY to currentQuestionNumber
                )
            }
    }
}
