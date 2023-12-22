package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.LogStrings

class MIGRATION_3_4 : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            database.execSQL(DROP_TABLE_FILM_RATING_QUERY)
            database.execSQL(CREATE_TABLE_FILM_RATING_QUERY)
            database.execSQL(DROP_TABLE_FILM_RATING_USER_QUERY)
        } catch (ex: Exception) {
            Log.e(LogStrings.MIGRATION_LOG_TAG, LogStrings.MIGRATION_LOG_MESSAGE.format(ex))
        }
    }

    companion object {
        const val DROP_TABLE_FILM_RATING_QUERY = "DROP TABLE 'film_rating'"
        const val CREATE_TABLE_FILM_RATING_QUERY = "CREATE TABLE 'film_rating' ('film_rating_id' INTEGER PRIMARY KEY NOT NULL, 'filmId' INTEGER NOT NULL, 'userId' INTEGER NOT NULL, 'rating' INTEGER NOT NULL, FOREIGN KEY (filmId) REFERENCES films(filmId) ON DELETE CASCADE, FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE)"
        const val DROP_TABLE_FILM_RATING_USER_QUERY = "DROP TABLE 'film_rating_user'"
    }
}
