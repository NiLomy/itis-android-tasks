package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MIGRATION_6_7 : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            database.execSQL("ALTER TABLE 'users' ADD COLUMN 'deletion_date' INTEGER")
        } catch (ex: Exception) {
            Log.e("DB_MIGRATION", "Error occurred: $ex")
        }
    }
}