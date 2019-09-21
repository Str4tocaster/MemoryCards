package com.valerymiller.memorycards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.layout_result.view.*

class WinFragment : Fragment() {

    object constants {
        val NICKNAME = "nickname"
        val ACTION_COUNT = "action_count"
        val SCORE = "score"
    }

    var nickname = ""
    var actionCount = 0
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nickname = arguments?.getString(constants.NICKNAME)?:""
        actionCount = arguments?.getInt(constants.ACTION_COUNT)?:0
        score = arguments?.getInt(constants.SCORE)?:0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_result, container, false)

        //view.tvNickname.text = nickname
        view.tvScore.text = score.toString()
        view.tvActionsCount.text = actionCount.toString()

        view.btnNext.setOnClickListener {
            if (activity is MainActivity)
                (activity as MainActivity).onNext()
        }
        return view
    }
}