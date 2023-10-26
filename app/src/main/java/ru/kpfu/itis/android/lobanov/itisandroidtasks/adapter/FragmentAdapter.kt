package ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.Question
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments.QuestionFragment

class FragmentAdapter(
    manager: FragmentManager,
    lifecycle: Lifecycle,
    private val questions: List<Question>,
) : FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int = questions.size

    override fun createFragment(position: Int): Fragment {
        return QuestionFragment.newInstance(questions[position], position)
    }
}
