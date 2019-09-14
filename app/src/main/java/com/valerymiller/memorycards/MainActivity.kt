package com.valerymiller.memorycards

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var cardNumber = 8
    var nickname = "Player"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            8 -> 2
            12 -> 3
            else -> 4
        }
        recyclerView.layoutManager = GridLayoutManager(this, span)
        recyclerView.adapter = CardsAdapter(this, cardNumber)
        try {
            recyclerView.removeItemDecorationAt(0)
        } catch (e: Exception) {}
        recyclerView.addItemDecoration(GridSpacingItemDecoration(span, 20, true))
        tvNickname.text = nickname
    }

    private fun loadSettings() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        nickname = sharedPreferences?.getString(
            SettingsBottomSheetFragment.constants.NICKNAME, "Player")?:"Player"
        cardNumber = sharedPreferences?.getInt(
            SettingsBottomSheetFragment.constants.CARD_NUMBER, 8)?:8
        Log.d("VALERA", nickname + " " + cardNumber)
    }

}
