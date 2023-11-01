package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemPlanetBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.PlanetModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments.PlanetsFragment

class PlanetViewHolder(
    private val viewBinding: ItemPlanetBinding,
    private val onNewsClicked: ((PlanetModel) -> Unit),
    private val onNewsClickedLong: ((PlanetModel, ItemPlanetBinding) -> Unit),
    private val onBinClicked: ((Int) -> Unit),
    private val onBinClickedLong: ((PlanetModel, ItemPlanetBinding) -> Unit),
    private val onLikeClicked: ((Int, PlanetModel) -> Unit),
    newsCount: Int
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: PlanetModel? = null

    init {
        viewBinding.root.setOnClickListener {
            this.item?.let(onNewsClicked)
        }
        viewBinding.ivFavouriteBtn.setOnClickListener {
            this.item?.let {
                val data = it.copy(isFavoured = !it.isFavoured)
                onLikeClicked(adapterPosition, data)
            }
        }

        viewBinding.ivBin.setOnClickListener {
            onBinClicked(adapterPosition)
        }

        viewBinding.ivBin.setOnLongClickListener {
            item?.let { item -> onBinClickedLong(item, viewBinding) }
            true
        }

        if (newsCount > PlanetsFragment.LINEAR_LAYOUT_THRESHOLD) {
            viewBinding.root.setOnLongClickListener {
                item?.let { item -> onNewsClickedLong(item, viewBinding) }
                true
            }
        }
    }

    fun bindItem(item: PlanetModel) {
        this.item = item
        with(viewBinding) {
            tvPlanetName.text = item.planetName
            item.planetImage?.let { res ->
                ivPlanetImage.setImageResource(res)
            }
            changeLikeBtnStatus(isChecked = item.isFavoured)
            changeSelectStatus(isSelected = item.isSelected)
        }
    }

    fun changeLikeBtnStatus(isChecked: Boolean) {
        val likeDrawable = if (isChecked) R.drawable.ic_favourite_red else R.drawable.ic_favourite_gray
        viewBinding.ivFavouriteBtn.setImageResource(likeDrawable)
    }

    private fun changeSelectStatus(isSelected: Boolean) {
        when (isSelected) {
            true -> viewBinding.ivBin.visibility = View.VISIBLE
            false -> viewBinding.ivBin.visibility = View.GONE
        }
    }
}
