package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.UserEntity

data class UserFavouriteFilmsEntity(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "filmId",
        associateBy = Junction(UserFilmCrossRefEntity::class)
    )
    val films: List<FilmEntity>
)
