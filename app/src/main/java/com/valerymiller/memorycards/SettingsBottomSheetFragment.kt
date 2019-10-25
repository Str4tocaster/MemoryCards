package com.valerymiller.memorycards

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import kotlinx.android.synthetic.main.layout_settings.view.*

private const val NICKNAME = "nickname"
private const val CARD_NUMBER = "card_number"
private const val FRAGMENT_TAG = "settings_tag"

interface SettingsFragmentListener {
    fun onSettingsClosed(cardNumber: Int, nickname: String)
}

class SettingsBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager, cardNumber: Int, nickname: String) {
            SettingsBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(NICKNAME, nickname)
                    putInt(CARD_NUMBER, cardNumber)
                }
                show(fragmentManager, FRAGMENT_TAG)
            }
        }
    }

    private lateinit var edtNickname: EditText
    private lateinit var seekBar: IndicatorSeekBar

    private lateinit var nickname: String
    private var cardNumber: Int = 0

    private var listener: SettingsFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nickname = arguments?.getString(NICKNAME) ?: resources.getString(R.string.default_nickname)
        cardNumber = arguments?.getInt(CARD_NUMBER) ?: resources.getInteger(R.integer.card_number_min)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.layout_settings, container)
        edtNickname = view.edtNickname
        seekBar = view.seekBar
        seekBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                cardNumber = seekParams.progress
            }
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtNickname.setText(nickname)
        seekBar.setProgress(cardNumber.toFloat())
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (activity is SettingsFragmentListener) {
            listener = activity
        }
    }

    override fun onDetach() {
        listener?.onSettingsClosed(cardNumber, edtNickname.text.toString())
        super.onDetach()
        listener = null
    }
}