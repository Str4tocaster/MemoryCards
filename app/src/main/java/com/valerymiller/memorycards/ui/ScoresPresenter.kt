package com.valerymiller.memorycards.ui

import com.valerymiller.memorycards.model.Score

interface ScoresPresenter {
    fun getScores(): List<Score>
}

class ScoresPresenterImpl : ScoresPresenter {

    override fun getScores(): List<Score> {
        return mutableListOf<Score>().apply {
            add(Score(1, "Valera", 49200))
            add(Score(2, "player", 37000))
            add(Score(3, "alalal fjrnfrfj rfjrnfb  rfjrjfnrbfbrf rfjrf", 34000))
            add(Score(4, "Str4tocaster", 23000))
            add(Score(5, "Иванов Иван", 12000))
        }
    }
}