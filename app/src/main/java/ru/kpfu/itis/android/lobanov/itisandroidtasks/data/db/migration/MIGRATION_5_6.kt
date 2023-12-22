package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.LogStrings

class MIGRATION_5_6 : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            database.execSQL(DROP_TABLE_FILM_RATING_QUERY)
            database.execSQL(CREATE_TABLE_FILM_RATING_QUERY)
            database.execSQL(CREATE_UNIQUE_INDEX_ON_FILM_RATING_QUERY)
        } catch (ex: Exception) {
            Log.e(LogStrings.MIGRATION_LOG_TAG, LogStrings.MIGRATION_LOG_MESSAGE.format(ex))
        }
    }

    companion object {
        const val DROP_TABLE_FILM_RATING_QUERY = "DROP TABLE 'film_rating'"
        const val CREATE_TABLE_FILM_RATING_QUERY = "CREATE TABLE 'film_rating' ('film_rating_id' INTEGER PRIMARY KEY NOT NULL, 'filmId' INTEGER NOT NULL, 'userId' INTEGER NOT NULL, 'rating' DOUBLE NOT NULL, FOREIGN KEY (filmId) REFERENCES films(filmId) ON DELETE CASCADE, FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE)"
        const val CREATE_UNIQUE_INDEX_ON_FILM_RATING_QUERY = "CREATE UNIQUE INDEX IF NOT EXISTS index_film_rating_filmId_userId ON film_rating(filmId, userId)"
    }
}
