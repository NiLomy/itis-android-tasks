package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MIGRATION_7_8 : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            database.execSQL("DROP TABLE 'user_film'")
            database.execSQL("CREATE TABLE 'user_film' ('userId' INTEGER NOT NULL, 'filmId' INTEGER NOT NULL, PRIMARY KEY (userId, filmId), FOREIGN KEY (filmId) REFERENCES films(filmId) ON DELETE CASCADE, FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE)")
        } catch (ex: Exception) {
            Log.e("DB_MIGRATION", "Error occurred: $ex")
        }
    }
}