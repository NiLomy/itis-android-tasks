package ru.kpfu.itis.android.lobanov.itisandroidtasks.model

import java.io.Serializable

data class Question(
    val questionCondition: String,
    val answers: MutableList<Answer>,
) : Serializable
