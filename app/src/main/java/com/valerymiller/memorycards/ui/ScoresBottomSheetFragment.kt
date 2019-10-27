package com.valerymiller.memorycards.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.valerymiller.memorycards.R
import kotlinx.android.synthetic.main.layout_scores.view.*

private const val FRAGMENT_TAG = "scores_tag"

class ScoresBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var presenter: ScoresPresenter

    companion object {
        fun show(fragmentManager: FragmentManager) {
            ScoresBottomSheetFragment().show(fragmentManager, FRAGMENT_TAG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ScoresPresenterImpl()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.layout_scores, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rvScores.adapter = ScoresAdapter(view.context, presenter.getScores())
        view.rvScores.layoutManager = LinearLayoutManager(view.context)
    }
}