package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository

class DeleteUserWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        inputData.getString("userEmail")?.let { UserRepository.delete(it) }
        return Result.success()
    }
}