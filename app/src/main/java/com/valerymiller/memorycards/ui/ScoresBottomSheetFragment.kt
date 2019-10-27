package com.valerymiller.memorycards.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.valerymiller.memorycards.R
import com.valerymiller.memorycards.model.Score
import kotlinx.android.synthetic.main.layout_scores.view.*

private const val FRAGMENT_TAG = "scores_tag"

interface ScoresView {
    fun updateView(items: List<Score>)
}

class ScoresBottomSheetFragment :
    BottomSheetDialogFragment(),
    ScoresView
{
    private lateinit var presenter: ScoresPresenter
    private lateinit var recyclerView: RecyclerView

    companion object {
        fun show(fragmentManager: FragmentManager) {
            ScoresBottomSheetFragment().show(fragmentManager, FRAGMENT_TAG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ScoresPresenterImpl(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.layout_scores, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.rvScores
        presenter.loadScores()
    }

    override fun updateView(items: List<Score>) {
        context?.let {context ->
            recyclerView.adapter = ScoresAdapter(context, items)
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }
}