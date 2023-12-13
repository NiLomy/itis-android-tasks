package ru.kpfu.itis.android.lobanov.itisandroidtasks.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.AppDatabase
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration.MIGRATION_1_2
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration.MIGRATION_2_3
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration.MIGRATION_3_4
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration.MIGRATION_4_5
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration.MIGRATION_5_6
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration.MIGRATION_6_7
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration.MIGRATION_7_8

object ServiceLocator {
    private var dbInstance: AppDatabase? = null
    private var filmPref: SharedPreferences? = null

    fun initData(ctx: Context) {
        dbInstance = Room.databaseBuilder(ctx, AppDatabase::class.java, "film_gallery.db")
            .addMigrations(
                MIGRATION_1_2(),
                MIGRATION_2_3(),
                MIGRATION_3_4(),
                MIGRATION_4_5(),
                MIGRATION_5_6(),
                MIGRATION_6_7(),
                MIGRATION_7_8()
            )
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
