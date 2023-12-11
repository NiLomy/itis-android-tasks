package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.converter.DateTypeConverter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.dao.FilmDao
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.dao.UserDao
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.UserEntity

@Database(entities = [UserEntity::class, FilmEntity::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getFilmDao(): FilmDao
}
