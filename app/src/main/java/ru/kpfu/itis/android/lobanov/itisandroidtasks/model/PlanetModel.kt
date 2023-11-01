package ru.kpfu.itis.android.lobanov.itisandroidtasks.model

import androidx.annotation.DrawableRes

data class PlanetModel(
    val planetId: Int,
    val planetName: String,
    val planetDetails: String? = null,
    @DrawableRes val planetImage: Int? = null,
    var isFavoured: Boolean = false,
    var isSelected: Boolean = false
) : DataModel()
