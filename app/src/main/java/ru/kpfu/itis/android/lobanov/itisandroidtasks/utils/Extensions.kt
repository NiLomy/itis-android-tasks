package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import android.util.DisplayMetrics
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.UserEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmCatalog.FilmRVModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.UserModel

fun Int.getValueInPx(dm: DisplayMetrics): Int {
    return (this * dm.density).toInt()
}

// mapper
fun UserModel.toUserEntity() = UserEntity(
    userId = id,
    name = name,
    phone = phone,
    email = email,
    password = password,
    deletionDate = deletionDate
)

fun UserEntity.toUserModel() = UserModel(
    id = userId,
    name = name,
    phone = phone,
    email = email,
    password = password,
    deletionDate = deletionDate
)

fun FilmModel.toFilmEntity(id: Int) = FilmEntity(
    filmId = id,
    name = name,
    date = date,
    description = description
)

fun FilmEntity.toFilmModel() = FilmModel(
    name = name,
    date = date,
    description = description
)

fun FilmRVModel.toFilmModel() = FilmModel(
    name = name,
    date = date,
    description = description
)
