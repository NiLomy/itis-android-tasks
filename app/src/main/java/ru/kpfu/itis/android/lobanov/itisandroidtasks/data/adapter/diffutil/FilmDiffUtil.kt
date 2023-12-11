package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmRVModel

class FilmDiffUtil(
    private val oldItemsList: List<FilmRVModel>,
    private val newItemsList: List<FilmRVModel>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return (oldItem.name == newItem.name) &&
                (oldItem.description == newItem.description) &&
                (oldItem.date == newItem.date)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return if (oldItem.isFavoured != newItem.isFavoured) {
            newItem.isFavoured
        } else {
            super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}