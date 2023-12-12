package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MIGRATION_2_3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            database.execSQL("CREATE TABLE 'film_rating_user' ('user_id' INTEGER NOT NULL, 'film_id' INTEGER NOT NULL, PRIMARY KEY (user_id, film_id))")
            database.execSQL("CREATE TABLE 'film_rating' ('film_rating_id' INTEGER PRIMARY KEY NOT NULL, 'film_id' INTEGER NOT NULL, 'rating' INTEGER NOT NULL, FOREIGN KEY (film_id) REFERENCES films(filmId) ON DELETE CASCADE)")
        } catch (ex: Exception) {
            Log.e("DB_MIGRATION", "Error occurred: $ex")
        }
    }
}