package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.LogStrings

class MIGRATION_6_7 : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            database.execSQL(ALTER_TABLE_USERS_ADD_COLUMN_DELETION_DATE_QUERY)
        } catch (ex: Exception) {
            Log.e(LogStrings.MIGRATION_LOG_TAG, LogStrings.MIGRATION_LOG_MESSAGE.format(ex))
        }
    }

    companion object {
        const val ALTER_TABLE_USERS_ADD_COLUMN_DELETION_DATE_QUERY = "ALTER TABLE 'users' ADD COLUMN 'deletion_date' INTEGER"
    }
}
