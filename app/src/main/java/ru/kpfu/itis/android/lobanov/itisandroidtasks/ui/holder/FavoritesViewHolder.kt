package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder

import android.content.Context
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.FilmAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmCatalog
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemFavoritesBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator

class FavoritesViewHolder(
    private val viewBinding: ItemFavoritesBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(viewBinding.root) {
    private var favouritesAdapter: FilmAdapter? = null

    fun bindItem() {
    }
}