package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmRVModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemFilmBinding

class FilmViewHolder(
    private val viewBinding: ItemFilmBinding,
    private val onFilmClicked: ((View, FilmRVModel) -> Unit),
    private val onLikeClicked: ((Int, FilmRVModel) -> Unit),
) : RecyclerView.ViewHolder(viewBinding.root) {
    private var item: FilmRVModel? = null

    init {
        viewBinding.root.setOnClickListener {
            this.item?.let{ onFilmClicked(viewBinding.filmImageIv, it) }
        }
        viewBinding.isFavouriteBtn.setOnClickListener {
            this.item?.let {
                val data = it.copy(isFavoured = !it.isFavoured)
                onLikeClicked(adapterPosition, data)
            }
        }
    }

    fun bindItem(item: FilmRVModel) {
        this.item = item
        with(viewBinding) {
            tvFilmName.text = item.name
            changeLikeBtnStatus(isChecked = item.isFavoured)
        }
    }

    fun changeLikeBtnStatus(isChecked: Boolean) {
        val likeDrawable = if (isChecked) R.drawable.ic_favorite else R.drawable.ic_unfavorite
        viewBinding.isFavouriteBtn.setImageResource(likeDrawable)
    }
}