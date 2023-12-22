package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "film_rating",
    foreignKeys = [ForeignKey(
        entity = FilmEntity::class,
        parentColumns = ["filmId"],
        childColumns = ["filmId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["filmId", "userId"], unique = true)]
)
data class FilmRatingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "film_rating_id")
    val ratingId: Int,
    val filmId: Int,
    val userId: Int,
    val rating: Double
)
