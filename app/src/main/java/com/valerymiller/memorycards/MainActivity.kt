package com.valerymiller.memorycards

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val span = 4
    val size = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSettings.setOnClickListener(this)
        btnTop.setOnClickListener(this)
        btnRestart.setOnClickListener(this)

        recyclerView.layoutManager = GridLayoutManager(this, span)
        recyclerView.adapter = CardsAdapter(this, size)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(span, 20, true))
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

}
