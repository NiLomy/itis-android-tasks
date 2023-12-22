package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import java.sql.Date

@Dao
interface FilmDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(filmEntity: FilmEntity)

    @Insert(onConflict = REPLACE)
    suspend fun save(filmEntity: List<FilmEntity>)

    @Delete
    suspend fun delete(filmEntity: FilmEntity)

    @Query(SELECT_SINGLE_BY_ID_QUERY)
    suspend fun get(filmId: Int): FilmEntity?

    @Query(SELECT_SINGLE_BY_NAME_AND_DATE_QUERY)
    suspend fun get(name: String, date: Date): FilmEntity?

    @Query(SELECT_ALL_ORDER_DATE_DESC_QUERY)
    suspend fun getAllByDateDesc(): List<FilmEntity>?

    @Query(SELECT_ALL_ORDER_DATE_ASC_QUERY)
    suspend fun getAllByDateAsc(): List<FilmEntity>?

    @Query(SELECT_ALL_ORDER_RATING_DESC_QUERY)
    suspend fun getAllByRatingDesc(): List<FilmEntity>?

    @Query(SELECT_ALL_ORDER_RATING_ASC_QUERY)
    suspend fun getAllByRatingAsc(): List<FilmEntity>?

    companion object {
        const val SELECT_SINGLE_BY_ID_QUERY = "SELECT * FROM films WHERE filmId = :filmId"
        const val SELECT_SINGLE_BY_NAME_AND_DATE_QUERY =
            "SELECT * FROM films WHERE name=:name AND date=:date"
        const val SELECT_ALL_ORDER_DATE_DESC_QUERY = "SELECT * FROM films ORDER BY date DESC"
        const val SELECT_ALL_ORDER_DATE_ASC_QUERY = "SELECT * FROM films ORDER BY date ASC"
        const val SELECT_ALL_ORDER_RATING_DESC_QUERY =
            "SELECT films.filmId, films.name, films.date, films.description, AVG(film_rating.rating) as average_rating FROM films LEFT JOIN film_rating ON films.filmId = film_rating.filmId GROUP BY films.filmId, films.date, films.description ORDER BY average_rating DESC"
        const val SELECT_ALL_ORDER_RATING_ASC_QUERY =
            "SELECT films.filmId, films.name, films.date, films.description, AVG(film_rating.rating) as average_rating FROM films LEFT JOIN film_rating ON films.filmId = film_rating.filmId GROUP BY films.filmId, films.date, films.description ORDER BY average_rating ASC"
    }
}
