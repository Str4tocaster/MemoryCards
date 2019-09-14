package com.valerymiller.memorycards

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import kotlinx.android.synthetic.main.layout_settings.view.*

class SettingsBottomSheetFragment : BottomSheetDialogFragment() {

    object constants {
        val NICKNAME = "nickname"
        val CARD_NUMBER = "card_number"
    }

    var cardNumber = 8
    var edtNickname: EditText? = null
    var seekBar: IndicatorSeekBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_settings, container)
        seekBar = view.seekBar
        seekBar?.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                cardNumber = seekParams.progress
            }
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        }
        edtNickname = view.edtNickname
        return view
    }

    override fun onResume() {
        super.onResume()
        loadSettings()
    }

    override fun onPause() {
        super.onPause()
        saveSettings(cardNumber, edtNickname?.text.toString())
        if (activity is MainActivity)
            (activity as MainActivity).onSettingsClosed()
    }

    private fun saveSettings(cardNumber: Int, nickname: String) {
        val sharedPreferences = activity?.getPreferences(MODE_PRIVATE)
        val ed = sharedPreferences?.edit()
        if (nickname.trim().isNotEmpty())
            ed?.putString(constants.NICKNAME, nickname)
        ed?.putInt(constants.CARD_NUMBER, cardNumber)
        Log.d("VALERA", "save: " + ed?.commit())
    }

    private fun loadSettings() {
        val sharedPreferences = activity?.getPreferences(MODE_PRIVATE)
        edtNickname?.setText(sharedPreferences?.getString(constants.NICKNAME, "Player")?:"Player")
        cardNumber = sharedPreferences?.getInt(constants.CARD_NUMBER, 8)?:8
        seekBar?.setProgress(cardNumber.toFloat())
    }
}