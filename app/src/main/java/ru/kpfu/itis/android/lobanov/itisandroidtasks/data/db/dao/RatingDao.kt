package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmRatingEntity

@Dao
interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(ratingEntity: FilmRatingEntity)

    @Query(SELECT_SINGLE_BY_FILM_AND_USER_QUERY)
    suspend fun getFilmRating(filmId: Int, userId: Int): FilmRatingEntity?

    @Query(SELECT_ALL_BY_FILM_QUERY)
    suspend fun getAllFilmRating(filmId: Int): List<FilmRatingEntity>

    companion object {
        const val SELECT_SINGLE_BY_FILM_AND_USER_QUERY =
            "SELECT * FROM film_rating WHERE filmId = :filmId AND userId = :userId"
        const val SELECT_ALL_BY_FILM_QUERY = "SELECT * FROM film_rating WHERE filmId = :filmId"
    }
}
