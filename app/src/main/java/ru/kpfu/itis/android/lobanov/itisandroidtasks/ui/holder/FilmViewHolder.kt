package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmCatalog
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemFilmBinding

class FilmViewHolder(
    private val viewBinding: ItemFilmBinding,
    private val onFilmClicked: ((FilmCatalog.FilmRVModel) -> Unit),
) : RecyclerView.ViewHolder(viewBinding.root) {
    private var item: FilmCatalog.FilmRVModel? = null

    init {
        viewBinding.root.setOnClickListener {
            this.item?.let { onFilmClicked(it) }
        }
    }

    fun bindItem(item: FilmCatalog.FilmRVModel) {
        this.item = item
        with(viewBinding) {
            tvFilmName.text = item.name
            tvFilmDate.text = item.date.toString()
        }
    }
}