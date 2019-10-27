package com.valerymiller.memorycards.ui

import android.content.Context
import android.graphics.drawable.Drawable
import com.valerymiller.memorycards.R
import com.valerymiller.memorycards.data.CardsInteractor
import com.valerymiller.memorycards.data.CardsInteractorListener
import com.valerymiller.memorycards.model.Card
import com.valerymiller.memorycards.model.Results
import kotlin.random.Random

private const val PREF_NICKNAME = "nickname"
private const val PREF_CARD_NUMBER = "card_number"
private const val CLOSE_CARD_DELAY = 1000L
private const val HIDE_CARD_DELAY = 600L

interface MainPresenter {
    fun onCreateGame()
    fun onRestartGame()
    fun onNextGame()
    fun onSettingsOpen()
    fun onSettingsClosed(cardNumber: Int, nickname: String)
    fun onCardFlipped(cardId: Int)
    fun onWinAnimationEnd()
}

class MainPresenterImpl (
    private val context: Context,
    private val view: MainView
) : MainPresenter,
    CardsInteractorListener
{
    private val cardsInteractor = CardsInteractor(this, context)

    private val closeHandler = ActionHandler(::closeCards)
    private val hideHandler = ActionHandler(::hideCards)

    private var nickname = context.resources.getString(R.string.default_nickname)
    private var cardNumber = context.resources.getInteger(R.integer.card_number_min)
    private var actionCount = 0
    private val openCards = mutableListOf<Int>()
    private var hidedCards = 0

    override fun onCreateGame() {
        refresh()
    }

    override fun onRestartGame() {
        refresh()
    }

    override fun onNextGame() {
        refresh()
        view.closeWinFragment()
    }

    override fun onSettingsOpen() {
        view.showSettingsFragment(cardNumber, nickname)
    }

    override fun onSettingsClosed(cardNumber: Int, nickname: String) {
        if (cardNumber != this.cardNumber
            || (nickname != this.nickname && nickname.trim().isNotEmpty())
        ) {
            saveSettings(cardNumber, nickname)
            refresh()
        }
    }

    override fun onCardFlipped(cardId: Int) {
        openCards.add(cardId)
        if (openCards.size > 1) {
            if (openCards[0] == openCards[1])
                startHideTimer()
            else startCloseTimer()
            actionCount++
            view.setActionCountText(actionCount.toString())
        }
    }

    override fun onWinAnimationEnd() {
        view.showWinFragment(
            Results(
                nickname,
                actionCount,
                calculateScores(cardNumber, actionCount)
            )
        )
    }

    override fun onCardsCreated(cards: List<Card>) {
        updateScreen(cards)
    }

    private fun updateScreen(cards: List<Card>) {
        view.updateScreen(cards, getRandomCardBack(), nickname)
        view.setActionCountText("0")
        view.hideProgress()
    }

    private fun closeCards() {
        view.closeCards()
        openCards.clear()
    }

    private fun hideCards() {
        view.hideCards()
        openCards.clear()
        hidedCards += 2
        if (hidedCards == cardNumber) {
            view.showWinAnimation()
        }
    }

    private fun refresh() {
        actionCount = 0
        hidedCards = 0
        loadSettings()
        view.setActionCountText(actionCount.toString())
        updateGameField()
    }

    private fun updateGameField() {
        view.showProgress(cardNumber)
        cardsInteractor.createCards(cardNumber)
    }

    private fun loadSettings() {
        view.getPreferences()?.let { preferences ->
            nickname = preferences.getString(PREF_NICKNAME, nickname) ?: nickname
            cardNumber = preferences.getInt(PREF_CARD_NUMBER, cardNumber)
        }
    }

    private fun saveSettings(cardNumber: Int, nickname: String) {
        view.getPreferences()?.let { preferences ->
            preferences.edit().apply {
                putString(PREF_NICKNAME, nickname)
                putInt(PREF_CARD_NUMBER, cardNumber)
                commit()
            }
        }
    }

    private fun calculateScores(cardNumber: Int, actionCount: Int): Int {
        return cardNumber * actionCount
    }

    private fun getRandomCardBack(): Drawable? =
        when (Random.nextInt(0, 6)) {
            0 -> context.getDrawable(R.drawable.card_back_1)
            1 -> context.getDrawable(R.drawable.card_back_2)
            2 -> context.getDrawable(R.drawable.card_back_3)
            3 -> context.getDrawable(R.drawable.card_back_4)
            4 -> context.getDrawable(R.drawable.card_back_5)
            else -> context.getDrawable(R.drawable.card_back_6)
        }

    private fun startCloseTimer() {
        Thread(Runnable {
            Thread.sleep(CLOSE_CARD_DELAY)
            closeHandler.sendEmptyMessage(1)
        }).start()
    }

    private fun startHideTimer() {
        Thread(Runnable {
            Thread.sleep(HIDE_CARD_DELAY)
            hideHandler.sendEmptyMessage(1)
        }).start()
    }
}