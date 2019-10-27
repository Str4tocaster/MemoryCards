package com.valerymiller.memorycards.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.valerymiller.memorycards.model.Card
import com.valerymiller.memorycards.ui.ActionHandler
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

interface CardsInteractorListener {
    fun onCardsCreated(cards: List<Card>)
}

class CardsInteractor(
    private val listener: CardsInteractorListener,
    private val context: Context
) {
    private var cardNumber = 0
    private var images: MutableList<Bitmap> = mutableListOf()

    private val loadedHandler = ActionHandler(::onImageLoaded)

    fun createCards(count: Int) {
        cardNumber = count
        Thread(Runnable {
            val drawables = mutableListOf<Bitmap>()
            requestImage(drawables)
        }).start()
    }

    private fun onImageLoaded() {
        listener.onCardsCreated(generateCards(images))
        images.clear()
    }

    private fun requestImage(images: MutableList<Bitmap>) {
        if (images.size >= cardNumber / 2) {
            this.images = images
            loadedHandler.sendEmptyMessage(1)
            return
        }
        val service = LoremPicsum.create()
        service.randomImage().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // todo обрабатывать ошибку
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
}