package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model

import java.sql.Date

data class FilmRVModel(
    val id: Int,
    val name: String,
    val date: Date,
    val description: String,
    val isFavoured: Boolean
)