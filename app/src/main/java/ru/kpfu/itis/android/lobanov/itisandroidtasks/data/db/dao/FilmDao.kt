package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation.FavouriteFilmUsersEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation.UserFavouriteFilmsEntity
import java.sql.Date

@Dao
interface FilmDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(filmEntity: FilmEntity)

    @Insert(onConflict = REPLACE)
    suspend fun save(filmEntity: List<FilmEntity>)

    @Delete
    suspend fun delete(filmEntity: FilmEntity)

    @Query("SELECT * FROM films WHERE filmId = :filmId")
    suspend fun get(filmId: Int): FilmEntity?

    @Query("SELECT * FROM films WHERE name=:name AND date=:date")
    suspend fun get(name: String, date: Date): FilmEntity?

    @Query("SELECT * FROM films ORDER BY date DESC")
    suspend fun getAllByDateDesc(): List<FilmEntity>?

    @Query("SELECT * FROM films ORDER BY date ASC")
    suspend fun getAllByDateAsc(): List<FilmEntity>?

    @Query("SELECT films.filmId, films.name, films.date, films.description, AVG(film_rating.rating) as average_rating FROM films LEFT JOIN film_rating ON films.filmId = film_rating.filmId GROUP BY films.filmId, films.date, films.description ORDER BY average_rating DESC")
    suspend fun getAllByRatingDesc(): List<FilmEntity>?

    @Query("SELECT films.filmId, films.name, films.date, films.description, AVG(film_rating.rating) as average_rating FROM films LEFT JOIN film_rating ON films.filmId = film_rating.filmId GROUP BY films.filmId, films.date, films.description ORDER BY average_rating ASC")
    suspend fun getAllByRatingAsc(): List<FilmEntity>?
}
