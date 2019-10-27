package com.valerymiller.memorycards.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.valerymiller.memorycards.R

private const val FRAGMENT_TAG = "scores_tag"

class ScoresBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager) {
            ScoresBottomSheetFragment().show(fragmentManager, FRAGMENT_TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.layout_scores, container)
        return view
    }
}