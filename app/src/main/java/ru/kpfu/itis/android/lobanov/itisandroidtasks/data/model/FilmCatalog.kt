package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model

import java.sql.Date

sealed class FilmCatalog {
    object FavoritesRV : FilmCatalog()

    data class FilmRVModel(
        val id: Int,
        val name: String,
        val date: Date,
        val description: String,
        var isFavoured: Boolean
    ) : FilmCatalog()
}
