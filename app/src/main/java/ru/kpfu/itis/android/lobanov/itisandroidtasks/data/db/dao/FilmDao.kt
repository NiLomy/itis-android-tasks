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

    @Query("SELECT * FROM films WHERE name=:name AND date=:date")
    suspend fun get(name: String, date: Date): FilmEntity?

    @Query("SELECT * FROM films ORDER BY date DESC")
    suspend fun getAll(): List<FilmEntity>?
}
