package com.valerymiller.memorycards.data

import com.valerymiller.memorycards.App
import com.valerymiller.memorycards.data.room.Score
import com.valerymiller.memorycards.model.Results
import com.valerymiller.memorycards.ui.ActionHandler
import com.valerymiller.memorycards.model.Score as ScoreModel

interface ScoreInteractorListener {
    fun onScoresLoaded(scores: List<ScoreModel>)
}

class ScoreInteractor(
    private val listener: ScoreInteractorListener?
) {

    private val database = App.instance.getDatabase()
    private val scoreDao = database.scoreDao()
    private val loadedHandler = ActionHandler(::onScoresLoaded)

    private val loadedScores = mutableListOf<ScoreModel>()

    fun saveScoresIfNeed(result: Results) {
        Thread(Runnable {
            val score = Score(result.nickname, result.scores)
            val existedResult = scoreDao.getByNickname(result.nickname)
            if (existedResult != null) {
                if (score.score > existedResult.score) {
                    scoreDao.update(score)
                }
            } else {
                scoreDao.insert(score)
            }
        }).start()
    }

    fun loadAllScores() {
        loadedScores.clear()
        Thread(Runnable {
            for ((i, score) in scoreDao.getAll().withIndex()) {
                loadedScores.add(ScoreModel(i + 1, score.nickname, score.score))
            }
            loadedHandler.sendEmptyMessage(1)
        }).start()
    }

    private fun onScoresLoaded() {
        listener?.onScoresLoaded(loadedScores)
    }

}



