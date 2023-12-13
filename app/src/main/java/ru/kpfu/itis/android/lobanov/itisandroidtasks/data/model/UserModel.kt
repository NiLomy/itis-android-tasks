package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model

import java.sql.Date

data class UserModel(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val deletionDate: Date?
)
