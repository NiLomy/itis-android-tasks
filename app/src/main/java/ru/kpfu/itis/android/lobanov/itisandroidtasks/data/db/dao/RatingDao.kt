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

    @Query("SELECT * FROM film_rating WHERE filmId = :filmId AND userId = :userId")
    suspend fun getFilmRating(filmId: Int, userId: Int): FilmRatingEntity?

    @Query("SELECT * FROM film_rating WHERE filmId = :filmId")
    suspend fun getAllFilmRating(filmId: Int): List<FilmRatingEntity>
}
