package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository

import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.toFilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.toFilmModel
import java.sql.Date

object FIlmRepository {
    suspend fun save(film: FilmModel) {
        ServiceLocator.getDbInstance().getFilmDao().save(film.toFilmEntity(0))
    }

    suspend fun getFilm(name: String, date: Date): FilmModel? {
        val filmEntity: FilmEntity? = ServiceLocator.getDbInstance().getFilmDao().get(name, date)
        return filmEntity?.toFilmModel()
    }

    suspend fun getAll(): List<FilmModel>? {
        val filmEntityList: List<FilmEntity>? = ServiceLocator.getDbInstance().getFilmDao().getAll()
        return filmEntityList?.map { filmEntity -> filmEntity.toFilmModel() }
//        return filmEntityList
    }
}
