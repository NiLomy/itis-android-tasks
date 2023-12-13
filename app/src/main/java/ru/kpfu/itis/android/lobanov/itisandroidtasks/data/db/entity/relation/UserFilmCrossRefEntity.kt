package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation

import androidx.room.Entity
import androidx.room.ForeignKey
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.UserEntity

@Entity(
    primaryKeys = ["userId", "filmId"],
    tableName = "user_film",
    foreignKeys = [ForeignKey(
        entity = FilmEntity::class,
        parentColumns = ["filmId"],
        childColumns = ["filmId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserFilmCrossRefEntity(
    val userId: Int,
    val filmId: Int,
)
