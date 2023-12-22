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
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.LogStrings
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsConstants

object ServiceLocator {
    private var dbInstance: AppDatabase? = null
    private var filmPref: SharedPreferences? = null

    fun initData(ctx: Context) {
        dbInstance = Room.databaseBuilder(ctx, AppDatabase::class.java, ParamsConstants.DATABASE_NAME)
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
        filmPref = ctx.getSharedPreferences(ParamsConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getDbInstance(): AppDatabase {
        return dbInstance ?: throw IllegalStateException(LogStrings.DB_INITIALIZATION_EXCEPTION)
    }

    fun getSharedPreferences(): SharedPreferences {
        return filmPref
            ?: throw IllegalStateException(LogStrings.SHARED_PREFERENCES_INITIALIZATION_EXCEPTION)
    }
}
