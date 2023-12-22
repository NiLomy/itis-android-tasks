package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.LogStrings

class MIGRATION_7_8 : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            database.execSQL(DROP_TABLE_USER_FILM_QUERY)
            database.execSQL(CREATE_TABLE_USER_FILM_QUERY)
        } catch (ex: Exception) {
            Log.e(LogStrings.MIGRATION_LOG_TAG, LogStrings.MIGRATION_LOG_MESSAGE.format(ex))
        }
    }

    companion object {
        const val DROP_TABLE_USER_FILM_QUERY = "DROP TABLE 'user_film'"
        const val CREATE_TABLE_USER_FILM_QUERY =  "CREATE TABLE 'user_film' ('userId' INTEGER NOT NULL, 'filmId' INTEGER NOT NULL, PRIMARY KEY (userId, filmId), FOREIGN KEY (filmId) REFERENCES films(filmId) ON DELETE CASCADE, FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE)"
    }
}
