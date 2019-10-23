package com.valerymiller.memorycards

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

interface MainView {
    fun updateScreen(cards: List<Card>)
    fun showProgress(show: Boolean)
}

class MainPresenter(
    private val context: Context,
    private val view: MainView
) {

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            view.updateScreen(generateCards(images))
            view.showProgress(false)
        }
    }

    private var cardNumber = 12
    private var images: List<Bitmap> = listOf()

    fun updateGameField() {
        view.showProgress(true)
        Thread(Runnable {
            val drawables = mutableListOf<Bitmap>()
            requestImage(drawables)
        }).start()
    }

    private fun requestImage(images: MutableList<Bitmap>) {
        if (images.size >= cardNumber/2) {
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
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            images.add(resource)
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
                requestImage(images)
            }
        })
    }

    fun setCardNumber(cardNumber: Int) {
        this.cardNumber = cardNumber
    }

    fun getCardNumber(): Int = cardNumber

    private fun generateCards(images: List<Bitmap>) : List<Card> {
        val size = images.size*2
        val items = mutableListOf<Card>()
        for (i in 1..size/2) {
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