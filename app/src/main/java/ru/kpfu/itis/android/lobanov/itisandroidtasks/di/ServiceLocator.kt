package ru.kpfu.itis.android.lobanov.itisandroidtasks.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.AppDatabase

object ServiceLocator {
    private var dbInstance: AppDatabase? = null
    private var filmPref: SharedPreferences? = null

    fun initData(ctx: Context) {
        dbInstance = Room.databaseBuilder(ctx, AppDatabase::class.java, "film_gallery.db")
            .build()
        filmPref = ctx.getSharedPreferences("film_gallery_pref", Context.MODE_PRIVATE)
    }

    fun getDbInstance(): AppDatabase {
        return dbInstance ?: throw IllegalStateException("Db was not initialized yet.")
    }

    fun getSharedPreferences(): SharedPreferences {
        return filmPref ?: throw IllegalStateException("Shared preferences were not initialized yet.")
    }
}
