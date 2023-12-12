package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MIGRATION_4_5 : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            database.execSQL("DROP TABLE 'film_rating'")
            database.execSQL("CREATE TABLE 'film_rating' ('film_rating_id' INTEGER PRIMARY KEY NOT NULL, 'filmId' INTEGER NOT NULL, 'userId' INTEGER NOT NULL, 'rating' DOUBLE NOT NULL, FOREIGN KEY (filmId) REFERENCES films(filmId) ON DELETE CASCADE, FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE)")
        } catch (ex: Exception) {
            Log.e("DB_MIGRATION", "Error occurred: $ex")
        }
    }
}