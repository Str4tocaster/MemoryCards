package com.valerymiller.memorycards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.layout_result.view.*

class WinFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_result, container, false)
        view.btnNext.setOnClickListener {
            if (activity is MainActivity)
                (activity as MainActivity).onNext()
        }
        return view
    }
}