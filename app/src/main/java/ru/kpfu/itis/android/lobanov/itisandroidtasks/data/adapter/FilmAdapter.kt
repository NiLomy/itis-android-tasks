package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.diffutil.FilmDiffUtil
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmRVModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemFilmBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder.FilmViewHolder

class FilmAdapter (
    private val onFilmClicked: ((View, FilmRVModel) -> Unit),
    private val onLikeClicked: ((Int, FilmRVModel) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filmsList = mutableListOf<FilmRVModel>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return FilmViewHolder(
                viewBinding = ItemFilmBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                ),
                onFilmClicked = onFilmClicked,
                onLikeClicked = onLikeClicked,
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? FilmViewHolder)?.bindItem(item = filmsList[position])
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as? Boolean)?.let {
                (holder as? FilmViewHolder)?.changeLikeBtnStatus(it)
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = filmsList.size

    fun setItems(list: List<FilmRVModel>) {
        val diff = FilmDiffUtil(oldItemsList = filmsList, newItemsList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        filmsList.clear()
        filmsList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(position: Int, item: FilmRVModel) {
        this.filmsList[position] = item
        notifyItemChanged(position, item.isFavoured)
    }

    fun removeAt(position: Int) {
        filmsList.removeAt(position)
        notifyItemRemoved(position)
    }
}