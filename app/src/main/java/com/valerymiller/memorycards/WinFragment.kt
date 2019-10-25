package com.valerymiller.memorycards

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.layout_result.view.*

private const val NICKNAME = "nickname"
private const val ACTION_COUNT = "action_count"
private const val SCORE = "score"
private const val FRAGMENT_TAG = "results_tag"

interface WinFragmentListener {
    fun onNext()
}

class WinFragment : Fragment() {

    companion object {
        fun show(fragmentManager: FragmentManager, results: Results) {
            val transaction = fragmentManager.beginTransaction()
            val fragment = WinFragment().apply {
                arguments = Bundle().apply {
                    putString(NICKNAME, results.nickname)
                    putInt(ACTION_COUNT, results.actionCount)
                    putInt(SCORE, results.scores)
                }
            }
            transaction.add(R.id.container, fragment, FRAGMENT_TAG)
            transaction.commit()
        }

        fun close(fragmentManager: FragmentManager) {
            val transaction = fragmentManager.beginTransaction()
            fragmentManager.findFragmentByTag(FRAGMENT_TAG)?.let { fragment ->
                transaction.remove(fragment)
                transaction.commit()
            }
        }
    }

    private lateinit var nickname: String
    private var actionCount = 0
    private var score = 0

    private var listener: WinFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nickname = arguments?.getString(NICKNAME) ?: ""
        actionCount = arguments?.getInt(ACTION_COUNT) ?: 0
        score = arguments?.getInt(SCORE) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_result, container, false)

        //view.tvNickname.text = nickname
        view.tvScore.text = score.toString()
        view.tvActionsCount.text = actionCount.toString()

        view.btnNext.setOnClickListener {
            listener?.onNext()
        }
        return view
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (activity is WinFragmentListener) {
            listener = activity
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}