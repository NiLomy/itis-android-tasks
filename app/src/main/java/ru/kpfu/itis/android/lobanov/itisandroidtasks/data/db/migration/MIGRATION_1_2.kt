package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.LogStrings

class MIGRATION_1_2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            database.execSQL(CREATE_TABLE_USER_FILM_QUERY)
        } catch (ex: Exception) {
            Log.e(LogStrings.MIGRATION_LOG_TAG, LogStrings.MIGRATION_LOG_MESSAGE.format(ex))
        }
    }

    companion object {
        const val CREATE_TABLE_USER_FILM_QUERY = "CREATE TABLE 'user_film' ('userId' INTEGER NOT NULL, 'filmId' INTEGER NOT NULL, PRIMARY KEY (userId, filmId))"
    }
}
