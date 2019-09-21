package com.valerymiller.memorycards

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var minCardNumber = 12
    var defaultNickname = "Player"
    var cardNumber = minCardNumber
    var nickname = defaultNickname
    var actionCount = 0
    var images: List<Bitmap> = listOf()

    private val updateHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            endUpdateScreen(images)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        minCardNumber = resources.getInteger(R.integer.card_number_min)
        defaultNickname = resources.getString(R.string.default_nickname)

        btnSettings.setOnClickListener(this)
        btnTop.setOnClickListener(this)
        btnRestart.setOnClickListener(this)

        loadSettings()
        updateScreen()
    }

    override fun onClick(view: View?) {
        when(view) {
            btnSettings -> {
                SettingsBottomSheetFragment().show(supportFragmentManager, "settings")
            }

            btnTop -> Toast.makeText(this, "Top", Toast.LENGTH_SHORT).show()
            btnRestart -> {
                updateScreen()
            }
        }
    }

    fun onSettingsClosed() {
        loadSettings()
        updateScreen()
    }

    fun onActionCounterIncreased() {
        setActionCountText(actionCount + 1)
    }

    fun onGameStarted() {

    }

    fun onNext() {
        updateScreen()
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentByTag("results")
        if (fragment != null) {
            transaction.remove(fragment)
            transaction.commit()
        }
    }

    fun onWin() {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = WinFragment()
        val bundle = Bundle()
        bundle.putString(WinFragment.constants.NICKNAME, nickname)
        bundle.putInt(WinFragment.constants.ACTION_COUNT, actionCount)
        bundle.putInt(WinFragment.constants.SCORE, getScore(cardNumber, actionCount))
        fragment.arguments = bundle
        transaction.add(R.id.container, fragment, "results")
        transaction.commit()
    }

    private fun updateScreen() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        Thread(Runnable {
            val drawables = mutableListOf<Bitmap>()
            requestImage(drawables)
        }).start()
    }

    private fun endUpdateScreen(images: List<Bitmap>) {
        val span = when(cardNumber) {
            12 -> 3
            else -> 4
        }
        val spacing = when(cardNumber) {
            12 -> 20
            16 -> 16
            20 -> 12
            else -> 8
        }
        recyclerView.layoutManager = GridLayoutManager(this, span)
        recyclerView.adapter = CardsAdapter(this, generateCards(images))
        try {
            recyclerView.removeItemDecorationAt(0)
        } catch (e: Exception) {}
        recyclerView.addItemDecoration(GridSpacingItemDecoration(span, spacing, true))
        tvNickname.text = nickname
        setActionCountText(0)

        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun setActionCountText(actionCount: Int) {
        this.actionCount = actionCount
        tvActionsCount.text = actionCount.toString()
    }

    private fun loadSettings() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        nickname = sharedPreferences?.getString(
            SettingsBottomSheetFragment.constants.NICKNAME, defaultNickname)?:defaultNickname
        cardNumber = sharedPreferences?.getInt(
            SettingsBottomSheetFragment.constants.CARD_NUMBER, minCardNumber)?:minCardNumber
    }

    fun generateCards(images: List<Bitmap>) : List<Card> {
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

    private fun requestImage(images: MutableList<Bitmap>) {
        if (images.size >= cardNumber/2) {
            this.images = images
            updateHandler.sendEmptyMessage(1)
            return
        }
        val service = LoremPicsum.create()
        service.randomImage().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val url = response.raw().request().url().toString()
                Glide.with(this@MainActivity)
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

    private fun getScore(cardNumber: Int, actionCount: Int): Int {
        return cardNumber * actionCount
    }

}
