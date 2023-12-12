package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model

import java.sql.Date

data class FilmRatingModel(
    val filmName: String,
    val filmDate: Date,
    val userEmail: String,
    val rating: Double
)