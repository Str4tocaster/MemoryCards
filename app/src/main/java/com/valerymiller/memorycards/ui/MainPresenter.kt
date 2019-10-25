package com.valerymiller.memorycards.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.valerymiller.memorycards.R
import com.valerymiller.memorycards.data.LoremPicsum
import com.valerymiller.memorycards.model.Card
import com.valerymiller.memorycards.model.Results
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

private const val PREF_NICKNAME = "nickname"
private const val PREF_CARD_NUMBER = "card_number"

interface MainPresenter {
    fun onCreateGame()
    fun onRestartGame()
    fun onWinGame()
    fun onNextGame()
    fun onSettingsOpen()
    fun onSettingsClosed(cardNumber: Int, nickname: String)
    fun onCardFlipped()
}

class MainPresenterImpl (
    private val context: Context,
    private val view: MainView
) : MainPresenter {

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            view.updateScreen(generateCards(images), nickname)
            view.setActionCountText("0")
            view.showProgress(false)
        }
    }

    private var nickname = context.resources.getString(R.string.default_nickname)
    private var cardNumber = context.resources.getInteger(R.integer.card_number_min)
    private var images: List<Bitmap> = listOf()
    private var actionCount = 0

    override fun onCreateGame() {
        refresh()
    }

    override fun onRestartGame() {
        refresh()
    }

    override fun onWinGame() {
        view.showWinFragment(
            Results(
                nickname,
                actionCount,
                calculateScores(cardNumber, actionCount)
            )
        )
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

    override fun onCardFlipped() {
        actionCount++
        view.setActionCountText(actionCount.toString())
    }

    private fun refresh() {
        loadSettings()
        updateGameField()
    }

    private fun updateGameField() {
        view.showProgress(true)
        Thread(Runnable {
            val drawables = mutableListOf<Bitmap>()
            requestImage(drawables)
        }).start()
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

    private fun requestImage(images: MutableList<Bitmap>) {
        if (images.size >= cardNumber / 2) {
            this.images = images
            handler.sendEmptyMessage(1)
            return
        }
        val service = LoremPicsum.create()
        service.randomImage().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val url = response.raw().request().url().toString()
                Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            images.add(resource)
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
                requestImage(images)
            }
        })
    }

    private fun generateCards(images: List<Bitmap>): List<Card> {
        val size = images.size * 2
        val items = mutableListOf<Card>()
        for (i in 1..size / 2) {
            items.add(Card(i, images[i - 1]))
            items.add(Card(i, images[i - 1]))
        }
        val result = mutableListOf<Card>()
        for (i in 1..size) {
            val j = Random.nextInt(0, items.size)
            result.add(items[j])
            items.removeAt(j)
        }
        return result
    }

    private fun calculateScores(cardNumber: Int, actionCount: Int): Int {
        return cardNumber * actionCount
    }
}