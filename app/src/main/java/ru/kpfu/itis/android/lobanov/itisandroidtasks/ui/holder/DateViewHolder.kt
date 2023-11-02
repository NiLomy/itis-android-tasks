package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemDateBinding

class DateViewHolder (
    private val viewBinding: ItemDateBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindItem(date: String) {
        viewBinding.tvDate.text = date
    }
}
