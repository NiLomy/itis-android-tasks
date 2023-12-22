package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.UserEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation.UserFavouriteFilmsEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation.UserFilmCrossRefEntity
import java.sql.Date

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(userEntity: UserEntity)

    @Insert(onConflict = REPLACE)
    suspend fun save(userEntity: List<UserEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun saveUserFilmCrossRef(crossRef: UserFilmCrossRefEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query(DELETE_BY_EMAIL_QUERY)
    suspend fun delete(email: String)

    @Delete
    suspend fun deleteUserFilmCrossRef(crossRef: UserFilmCrossRefEntity)

    @Query(UPDATE_DELETION_DATE_BY_EMAIL_QUERY)
    suspend fun setDeletionDate(email: String, deletionDate: Date?)

    @Query(SELECT_SINGLE_BY_ID_QUERY)
    suspend fun get(userId: Int): UserEntity?

    @Query(SELECT_SINGLE_BY_PHONE_QUERY)
    suspend fun getByPhone(phone: String): UserEntity?

    @Query(SELECT_SINGLE_BY_EMAIL_QUERY)
    suspend fun getByEmail(email: String): UserEntity?

    @Query(SELECT_SINGLE_BY_EMAIL_AND_PASSWORD_QUERY)
    suspend fun get(email: String, password: String): UserEntity?

    @Query(SELECT_FAVORITES_BY_EMAIL_QUERY)
    suspend fun getAllFavourites(email: String): UserFavouriteFilmsEntity

    @Query(UPDATE_PHONE_QUERY)
    suspend fun updatePhone(id: Int, phone: String)

    @Query(UPDATE_EMAIL_QUERY)
    suspend fun updateEmail(id: Int, email: String)

    @Query(UPDATE_PASSWORD_QUERY)
    suspend fun updatePassword(id: Int, password: String)

    companion object {
        const val DELETE_BY_EMAIL_QUERY = "DELETE FROM users WHERE email = :email"
        const val UPDATE_DELETION_DATE_BY_EMAIL_QUERY =
            "UPDATE users SET deletion_date = :deletionDate WHERE email = :email"
        const val SELECT_SINGLE_BY_ID_QUERY = "SELECT * FROM users WHERE userId = :userId"
        const val SELECT_SINGLE_BY_PHONE_QUERY = "SELECT * FROM users WHERE phone=:phone"
        const val SELECT_SINGLE_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email=:email"
        const val SELECT_SINGLE_BY_EMAIL_AND_PASSWORD_QUERY =
            "SELECT * FROM users WHERE email=:email AND password=:password"
        const val SELECT_FAVORITES_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = :email"
        const val UPDATE_PHONE_QUERY = "UPDATE users SET phone = :phone WHERE userId = :id"
        const val UPDATE_EMAIL_QUERY = "UPDATE users SET email = :email WHERE userId = :id"
        const val UPDATE_PASSWORD_QUERY = "UPDATE users SET password = :password WHERE userId = :id"
    }
}
