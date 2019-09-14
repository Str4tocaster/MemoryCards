package com.valerymiller.memorycards

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var minCardNumber = 12
    var defaultNickname = "Player"
    var spacing = 20
    var cardNumber = minCardNumber
    var nickname = defaultNickname

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        minCardNumber = resources.getInteger(R.integer.card_number_min)
        defaultNickname = resources.getString(R.string.default_nickname)
        spacing = resources.getInteger(R.integer.cards_spacing)

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
            btnRestart -> Toast.makeText(this, "Restart", Toast.LENGTH_SHORT).show()
        }
    }

    fun onSettingsClosed() {
        loadSettings()
        updateScreen()
    }

    private fun updateScreen() {
        val span = when(cardNumber) {
            12 -> 3
            else -> 4
        }
        recyclerView.layoutManager = GridLayoutManager(this, span)
        recyclerView.adapter = CardsAdapter(this, cardNumber)
        try {
            recyclerView.removeItemDecorationAt(0)
        } catch (e: Exception) {}
        recyclerView.addItemDecoration(GridSpacingItemDecoration(span, spacing, true))
        tvNickname.text = nickname
    }

    private fun loadSettings() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        nickname = sharedPreferences?.getString(
            SettingsBottomSheetFragment.constants.NICKNAME, defaultNickname)?:defaultNickname
        cardNumber = sharedPreferences?.getInt(
            SettingsBottomSheetFragment.constants.CARD_NUMBER, minCardNumber)?:minCardNumber
    }

}
