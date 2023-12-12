package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.UserEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation.UserFavouriteFilmsEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation.UserFilmCrossRefEntity

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

    @Delete
    suspend fun deleteUserFilmCrossRef(crossRef: UserFilmCrossRefEntity)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun get(userId: Int): UserEntity?

    @Query("SELECT * FROM users WHERE phone=:phone")
    suspend fun getByPhone(phone: String): UserEntity?

    @Query("SELECT * FROM users WHERE email=:email")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE email=:email AND password=:password")
    suspend fun get(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getAllFavourites(email: String): UserFavouriteFilmsEntity

    @Query("UPDATE users SET phone = :phone WHERE userId = :id")
    suspend fun updatePhone(id: Int, phone: String)

    @Query("UPDATE users SET email = :email WHERE userId = :id")
    suspend fun updateEmail(id: Int, email: String)

    @Query("UPDATE users SET password = :password WHERE userId = :id")
    suspend fun updatePassword(id: Int, password: String)
}
