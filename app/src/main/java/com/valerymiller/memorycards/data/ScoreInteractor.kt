package com.valerymiller.memorycards.data

import com.valerymiller.memorycards.App
import com.valerymiller.memorycards.data.room.Score
import com.valerymiller.memorycards.model.Score as ScoreModel
import com.valerymiller.memorycards.model.Results

interface ScoreInteractorListener {
    fun onScoreSaved()
    fun onScoresLoaded(scores: List<ScoreModel>)
}

class ScoreInteractor {

    private val database = App.instance.getDatabase()
    private val scoreDao = database.scoreDao()

    fun saveScores(result: Results) {
        val score = Score(result.nickname, result.scores)
        scoreDao.insert(score)
    }

}



