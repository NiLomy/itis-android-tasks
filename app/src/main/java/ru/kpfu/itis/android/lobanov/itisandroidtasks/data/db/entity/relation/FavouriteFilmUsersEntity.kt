package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.UserEntity

data class FavouriteFilmUsersEntity(
    @Embedded
    val film: FilmEntity,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "userId",
        associateBy = Junction(UserFilmCrossRefEntity::class)
    )
    val users: List<UserEntity>
)
