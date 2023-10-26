package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemQuestionBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.Answer

class QuestionHolder(
    private val viewBinding: ItemQuestionBinding,
    private val onItemChecked: ((Int) -> Unit)? = null,
    private val onRootClicked: ((Int) -> Unit)? = null,
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.questionCb.setOnClickListener {
            onItemChecked?.invoke(adapterPosition)
        }

        viewBinding.root.setOnClickListener {
            onRootClicked?.invoke(adapterPosition)
        }
    }

    fun bindItem(item: Answer, itemsCount: Int) {
        with(viewBinding) {
            questionTv.text = item.answer
            questionCb.isChecked = item.isChecked
            questionCb.isEnabled = !item.isChecked

            divider.isVisible = adapterPosition != itemsCount - 1
        }
    }
}
