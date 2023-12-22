package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.diffutil.FilmDiffUtil
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmCatalog
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemFilmBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder.FavoritesViewHolder
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder.FilmViewHolder

class FilmAdapter(
    private val onFilmClicked: ((FilmCatalog.FilmRVModel) -> Unit),
    private val favoritesRV: RecyclerView?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var filmsList = mutableListOf<FilmCatalog>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            R.layout.item_film -> FilmViewHolder(
                viewBinding = ItemFilmBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                ),
                onFilmClicked = onFilmClicked,
            )

            else -> throw RuntimeException(parent.context.getString(R.string.there_is_no_such_holder))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> {
                holder.bindItem(item = filmsList[position] as FilmCatalog.FilmRVModel)
            }

            is FavoritesViewHolder -> {
                holder.bindItem()
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = filmsList.size

    override fun getItemViewType(position: Int): Int {
        return when (filmsList[position]) {
            is FilmCatalog.FilmRVModel -> R.layout.item_film
            is FilmCatalog.FavoritesRV -> R.layout.item_favorites
        }
    }

    fun itemPosition(item: FilmCatalog.FilmRVModel): Int {
        for (i: Int in filmsList.indices) {
            val film = filmsList[i] as FilmCatalog.FilmRVModel
            if (item.name == film.name && item.date == film.date) {
                return i
            }
        }
        return -1
    }

    fun setItems(list: List<FilmCatalog.FilmRVModel>) {
        val diff = FilmDiffUtil(oldItemsList = filmsList, newItemsList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        filmsList.clear()
        filmsList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addItem(position: Int, item: FilmCatalog.FilmRVModel) {
        this.filmsList.add(position, item)
        for (i: Int in position until filmsList.size) {
            notifyItemChanged(position)
        }
        if (favoritesRV != null) {
            favoritesRV.visibility = View.VISIBLE
        }
    }

    fun removeAt(position: Int) {
        filmsList.removeAt(position)
        notifyItemRemoved(position)
        changeRVVisibility()
    }

    fun remove(item: FilmCatalog.FilmRVModel) {
        for (i: Int in 0 until filmsList.size) {
            val film = filmsList[i] as FilmCatalog.FilmRVModel
            if (item.name == film.name && item.date == film.date) {
                filmsList.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
        changeRVVisibility()
    }

    fun get(position: Int): FilmCatalog.FilmRVModel {
        return filmsList[position] as FilmCatalog.FilmRVModel
    }

    fun get(item: FilmCatalog.FilmRVModel): FilmCatalog.FilmRVModel? {
        for (i: FilmCatalog in filmsList) {
            val film = i as FilmCatalog.FilmRVModel
            if (item.name == film.name && item.date == film.date && item.description == film.description) return film
        }
        return null
    }

    private fun changeRVVisibility() {
        if (favoritesRV != null) {
            if (filmsList.isEmpty()) {
                favoritesRV.visibility = View.GONE
            }
        }
    }
}