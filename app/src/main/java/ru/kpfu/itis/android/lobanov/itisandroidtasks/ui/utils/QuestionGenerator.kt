package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.utils

import android.content.Context
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.Answer
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.Question
import kotlin.random.Random

object QuestionGenerator {
    fun getQuestions(ctx: Context, questionCount: Int): MutableList<Question> {
        val questionArray = ctx.resources.getStringArray(R.array.questions)
        val answerArray = ctx.resources.getStringArray(R.array.answers)

        val resultList = mutableListOf<Question>()

        repeat(questionCount) {
            val answers = mutableListOf<Answer>()
            for (index in answerArray.indices) {
                answers.add(Answer(answerArray[index]))
            }
            val newIndex = Random.nextInt(0, questionArray.size)
            resultList.add(Question(questionArray[newIndex], answers))
        }
        resultList.add(0, resultList[resultList.size - 1])
        resultList.add(resultList[1])
        return resultList
    }
}
