package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    @ColumnInfo(name = "deletion_date")
    val deletionDate: Date?
)
