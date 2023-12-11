package ru.kpfu.itis.android.lobanov.itisandroidtasks

import android.app.Application
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator

class FilmGalleryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initData(ctx = this)
    }
}
