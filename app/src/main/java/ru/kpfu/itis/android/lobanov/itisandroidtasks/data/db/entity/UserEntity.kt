package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val name: String,
    val phone: String,
    val email: String,
    val password: String
)
