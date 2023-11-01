package ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.PlanetModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.DataModel

class PlanetDiffUtil(
    private val oldItemsList: List<DataModel>,
    private val newItemsList: List<DataModel>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        if (oldItem is PlanetModel && newItem is PlanetModel) {
            return oldItem.planetId == newItem.planetId
        }

        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        if (oldItem is PlanetModel && newItem is PlanetModel) {
            return (oldItem.planetName == newItem.planetName) &&
                    (oldItem.planetDetails == newItem.planetDetails)
        }

        return false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        if (oldItem is PlanetModel && newItem is PlanetModel) {
            if (oldItem.isFavoured != newItem.isFavoured) {
                return newItem.isFavoured
            }
        }

        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
