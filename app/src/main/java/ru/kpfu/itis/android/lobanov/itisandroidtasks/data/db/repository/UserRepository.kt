package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository

import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.UserEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation.UserFavouriteFilmsEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.relation.UserFilmCrossRefEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.UserModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsConstants
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.PasswordEncrypter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.toFilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.toUserEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.toUserModel
import java.sql.Date

object UserRepository {
    suspend fun save(user: UserModel) {
        ServiceLocator.getDbInstance().getUserDao().save(user.toUserEntity())
    }

    suspend fun saveUserFilmCrossRef(email: String, film: FilmModel) {
        val filmEntity = ServiceLocator.getDbInstance().getFilmDao().get(film.name, film.date)
        getUserByEmail(email)?.let { userEntity ->
            filmEntity?.let { filmEntity ->
                ServiceLocator.getDbInstance().getUserDao().saveUserFilmCrossRef(
                    UserFilmCrossRefEntity(userEntity.id, filmEntity.filmId)
                )
            }
        }
    }

    suspend fun delete(email: String) {
        ServiceLocator.getDbInstance().getUserDao().delete(email)
    }

    suspend fun deleteUserFilmCrossRef(email: String, film: FilmModel) {
        val filmEntity = ServiceLocator.getDbInstance().getFilmDao().get(film.name, film.date)
        getUserByEmail(email)?.let { userEntity ->
            filmEntity?.let { filmEntity ->
                ServiceLocator.getDbInstance().getUserDao().deleteUserFilmCrossRef(
                    UserFilmCrossRefEntity(userEntity.id, filmEntity.filmId)
                )
            }
        }
    }

    suspend fun setDeletionDate(email: String, deletionDate: Date?) {
        ServiceLocator.getDbInstance().getUserDao().setDeletionDate(email, deletionDate)
    }

    suspend fun getUserByPhone(phone: String): UserModel? {
        val userEntity: UserEntity? = ServiceLocator.getDbInstance().getUserDao().getByPhone(phone)
        return userEntity?.toUserModel()
    }

    suspend fun getUserByEmail(email: String): UserModel? {
        val userEntity: UserEntity? = ServiceLocator.getDbInstance().getUserDao().getByEmail(email)
        return userEntity?.toUserModel()
    }

    suspend fun getUser(email: String, password: String): UserModel? {
        val userEntity: UserEntity? = ServiceLocator.getDbInstance().getUserDao()
            .get(email, password)
        return userEntity?.toUserModel()
    }

    suspend fun getAllFavourites(email: String): List<FilmModel> {
        val userFavouriteFilms: UserFavouriteFilmsEntity =
            ServiceLocator.getDbInstance().getUserDao().getAllFavourites(email)
        return userFavouriteFilms.films.map { filmEntity -> filmEntity.toFilmModel() }
    }

    suspend fun isFilmFavourite(email: String, filmName: String, filmDate: Date): Boolean {
        val userFavouriteFilms: UserFavouriteFilmsEntity =
            ServiceLocator.getDbInstance().getUserDao().getAllFavourites(email)
        return userFavouriteFilms.films.any { filmEntity ->
            filmEntity.name == filmName && filmEntity.date == filmDate
        }
    }

    suspend fun updatePhone(user: UserModel, phone: String) {
        val id = getUser(user.email, user.password)?.id
        id?.let { ServiceLocator.getDbInstance().getUserDao().updatePhone(it, phone) }
    }

    suspend fun updateEmail(user: UserModel, email: String) {
        val id = getUser(user.email, user.password)?.id
        id?.let { ServiceLocator.getDbInstance().getUserDao().updateEmail(it, email) }
    }

    suspend fun updatePassword(user: UserModel, password: String) {
        val id = getUser(user.email, user.password)?.id
        id?.let {
            ServiceLocator.getDbInstance().getUserDao().updatePassword(
                it,
                password
            )
        }
    }
}
