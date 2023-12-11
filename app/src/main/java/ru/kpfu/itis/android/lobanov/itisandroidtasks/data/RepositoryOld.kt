package ru.kpfu.itis.android.lobanov.itisandroidtasks.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.AppDatabase
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.UserEntity

class RepositoryOld(context: Context) {

    private val db by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, REPOSITORY_NAME)
            .build()
    }

    private val userDao by lazy {
        db.getUserDao()
    }

    private val filmDao by lazy {
        db.getFilmDao()
    }

    suspend fun saveUser(userEntity: UserEntity) {
        userDao.save(userEntity)
    }

    suspend fun getAllUsers(): List<UserEntity> = withContext(Dispatchers.IO) {
        userDao.getAll()
    }

    companion object {
        private const val REPOSITORY_NAME = "itis.films.db"
    }
}