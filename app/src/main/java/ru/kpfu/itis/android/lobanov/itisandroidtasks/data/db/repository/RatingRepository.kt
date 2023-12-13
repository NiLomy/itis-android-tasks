package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository

import android.provider.ContactsContract.CommonDataKinds.Email
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmRatingEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmRatingModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.toFilmModel

object RatingRepository {
    suspend fun save(rating: FilmRatingModel) {
        val film = ServiceLocator.getDbInstance().getFilmDao().get(rating.filmName, rating.filmDate)
        val user = ServiceLocator.getDbInstance().getUserDao().getByEmail(rating.userEmail)
        film?.let {  filmEntity ->
            user?.let { userEntity ->
                ServiceLocator.getDbInstance().getRatingDao().save(FilmRatingEntity(0, filmEntity.filmId, userEntity.userId, rating.rating))
            }
        }
    }

    suspend fun getFilmRating(filmModel: FilmModel, email: String): Double? {
        val film = ServiceLocator.getDbInstance().getFilmDao().get(filmModel.name, filmModel.date)
        val user = ServiceLocator.getDbInstance().getUserDao().getByEmail(email)
        if (film != null && user != null) {
            val rating = ServiceLocator.getDbInstance().getRatingDao().getFilmRating(film.filmId, user.userId)
            if (rating != null) {
                return rating.rating
            }
        }
        return null
    }

    suspend fun getAllFilmRating(film: FilmModel): Double? {
        val filmEntity = ServiceLocator.getDbInstance().getFilmDao().get(film.name, film.date)
        filmEntity?.let {
            val rating = ServiceLocator.getDbInstance().getRatingDao().getAllFilmRating(filmEntity.filmId)
            if (rating.isEmpty()) return null
            return rating.map { filmRatingEntity -> filmRatingEntity.rating }.average()
        }
        return null
    }
}