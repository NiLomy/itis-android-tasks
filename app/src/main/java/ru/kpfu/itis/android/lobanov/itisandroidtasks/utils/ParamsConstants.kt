package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

object ParamsConstants {
    const val DATABASE_NAME = "film_gallery.db"
    const val SHARED_PREFERENCES_NAME = "film_gallery_pref"
    const val ENCRYPTING_ALGORITHM = "MD5"
    const val USERNAME_SP_TAG = "username"
    const val PHONE_SP_TAG = "userPhone"
    const val EMAIL_SP_TAG = "userEmail"
    const val PASSWORD_SP_TAG = "userPassword"
    const val DELETE_USER_WORK_TAG = "DELETE_USER_WORK_TAG"
    const val DELETE_USER_WORK_NAME = "delayedDeleteUser"
    const val PHONE_REGEX = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}\$"
    const val EMAIL_REGEX = "^\\S+@\\S+\\.\\S+\$"
}
