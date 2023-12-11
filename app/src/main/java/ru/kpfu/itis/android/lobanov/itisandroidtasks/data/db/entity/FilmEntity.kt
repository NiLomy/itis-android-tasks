package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey(autoGenerate = true)
    val filmId: Int,
    val name: String,
    val date: Date,
    val description: String
)
