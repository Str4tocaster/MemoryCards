package com.valerymiller.memorycards

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var minCardNumber = 12
    var defaultNickname = "Player"
    var cardNumber = minCardNumber
    var nickname = defaultNickname
    var actionCount = 0
    var time: Long = 0

    var timer = Timer(false)

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
        if (time == 0L) {
            val handler = Handler()
            timer = Timer(false)
            val timerTask = object : TimerTask() {
                override fun run() {
                    handler.post {
                        updateTime()
                    }
                }
            }
            timer.schedule(timerTask, 1, 1)
        }
    }

    fun onWin() {
        timer.cancel()
        Toast.makeText(this, "Congratulations!", Toast.LENGTH_LONG).show()
    }

    private fun updateTime() {
        setTimeText(time + 1)
    }

    private fun updateScreen() {
        timer.cancel()
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
        recyclerView.adapter = CardsAdapter(this, generateCards(cardNumber))
        try {
            recyclerView.removeItemDecorationAt(0)
        } catch (e: Exception) {}
        recyclerView.addItemDecoration(GridSpacingItemDecoration(span, spacing, true))
        tvNickname.text = nickname
        setActionCountText(0)
        setTimeText(0)
    }

    private fun setActionCountText(actionCount: Int) {
        this.actionCount = actionCount
        tvActionsCount.text = "actions: " + actionCount.toString()
    }

    private fun setTimeText(time: Long) {
        this.time = time
        val date = Date(time)
        val format = SimpleDateFormat("mm:ss.SS")
        tvTime.text = format.format(date)
    }

    private fun loadSettings() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        nickname = sharedPreferences?.getString(
            SettingsBottomSheetFragment.constants.NICKNAME, defaultNickname)?:defaultNickname
        cardNumber = sharedPreferences?.getInt(
            SettingsBottomSheetFragment.constants.CARD_NUMBER, minCardNumber)?:minCardNumber
    }

    fun generateCards(size: Int) : List<Card> {
        val items = mutableListOf<Card>()
        for (i in 1..size/2) {
            items.add(Card(i))
            items.add(Card(i))
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
