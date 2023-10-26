package ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemQuestionBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.Answer
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder.QuestionHolder

class QuestionAdapter(
    val items: MutableList<Answer>,
    private val onItemChecked: ((Int) -> Unit)? = null,
    private val onRootClicked: ((Int) -> Unit)? = null,
) : RecyclerView.Adapter<QuestionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionHolder {
        return QuestionHolder(
            viewBinding = ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onItemChecked = onItemChecked,
            onRootClicked = onRootClicked,
        )
    }

    override fun onBindViewHolder(holder: QuestionHolder, position: Int) {
        holder.bindItem(item = items[position], itemCount)
    }

    override fun getItemCount(): Int = items.count()
}
