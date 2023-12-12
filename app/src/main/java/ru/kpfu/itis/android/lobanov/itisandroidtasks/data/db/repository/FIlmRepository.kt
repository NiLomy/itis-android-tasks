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

    suspend fun delete(film: FilmModel) {
        val filmEntity = ServiceLocator.getDbInstance().getFilmDao().get(film.name, film.date)
        if (filmEntity != null) {
            ServiceLocator.getDbInstance().getFilmDao().delete(filmEntity)
        }
    }

    suspend fun getFilm(name: String, date: Date): FilmModel? {
        val filmEntity: FilmEntity? = ServiceLocator.getDbInstance().getFilmDao().get(name, date)
        return filmEntity?.toFilmModel()
    }

    suspend fun getAllByDateDesc(): List<FilmModel>? {
        val filmEntityList: List<FilmEntity>? = ServiceLocator.getDbInstance().getFilmDao().getAllByDateDesc()
        return filmEntityList?.map { filmEntity -> filmEntity.toFilmModel() }
    }

    suspend fun getAllByDateAsc(): List<FilmModel>? {
        val filmEntityList: List<FilmEntity>? = ServiceLocator.getDbInstance().getFilmDao().getAllByDateAsc()
        return filmEntityList?.map { filmEntity -> filmEntity.toFilmModel() }
    }

    suspend fun getAllByRatingDesc(): List<FilmModel>? {
        val filmEntityList: List<FilmEntity>? = ServiceLocator.getDbInstance().getFilmDao().getAllByRatingDesc()
        return filmEntityList?.map { filmEntity -> filmEntity.toFilmModel() }
    }

    suspend fun getAllByRatingAsc(): List<FilmModel>? {
        val filmEntityList: List<FilmEntity>? = ServiceLocator.getDbInstance().getFilmDao().getAllByRatingAsc()
        return filmEntityList?.map { filmEntity -> filmEntity.toFilmModel() }
    }
}
