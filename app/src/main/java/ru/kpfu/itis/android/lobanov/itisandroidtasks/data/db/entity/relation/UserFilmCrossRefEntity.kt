package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "filmId"], tableName = "user_film")
data class UserFilmCrossRefEntity(
    val userId: Int,
    val filmId: Int,
)
