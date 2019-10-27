package com.valerymiller.memorycards.ui

import com.valerymiller.memorycards.data.ScoreInteractor
import com.valerymiller.memorycards.data.ScoreInteractorListener
import com.valerymiller.memorycards.model.Score

interface ScoresPresenter {
    fun loadScores()
}

class ScoresPresenterImpl(
    private val view: ScoresView
) : ScoresPresenter,
    ScoreInteractorListener
{
    private val scoreInteractor = ScoreInteractor(this)

    override fun loadScores() {
        scoreInteractor.loadAllScores()
    }

    override fun onScoresLoaded(scores: List<Score>) {
        view.updateView(scores)
    }
}