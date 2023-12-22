package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmCatalog

class FilmDiffUtil(
    private val oldItemsList: List<FilmCatalog>,
    private val newItemsList: List<FilmCatalog>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return if (oldItem is FilmCatalog.FilmRVModel && newItem is FilmCatalog.FilmRVModel) {
            oldItem.id == newItem.id
        } else false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return if (oldItem is FilmCatalog.FilmRVModel && newItem is FilmCatalog.FilmRVModel) {
            (oldItem.name == newItem.name) &&
                    (oldItem.description == newItem.description) &&
                    (oldItem.date == newItem.date)
        } else false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        if (oldItem is FilmCatalog.FilmRVModel && newItem is FilmCatalog.FilmRVModel) {
            if (oldItem.isFavoured != newItem.isFavoured) {
                return newItem.isFavoured
            }
        }
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}