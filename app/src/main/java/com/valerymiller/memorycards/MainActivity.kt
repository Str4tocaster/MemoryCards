package com.valerymiller.memorycards

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView, View.OnClickListener {

    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSettings.setOnClickListener(this)
        btnTop.setOnClickListener(this)
        btnRestart.setOnClickListener(this)

        presenter = MainPresenter(this, this)
        presenter.refresh()
    }

    override fun onClick(view: View?) {
        when(view) {
            btnSettings -> SettingsBottomSheetFragment().show(supportFragmentManager, "settings")
            btnTop -> Toast.makeText(this, "Top", Toast.LENGTH_SHORT).show()
            btnRestart -> presenter.refresh()
        }
    }

    override fun updateScreen(cards: List<Card>, nickname: String) {
        recyclerView.setCards(cards)
        tvNickname.text = nickname
    }

    override fun setActionCountText(actionCount: String) {
        tvActionsCount.text = actionCount
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun showWinFragment(results: Results) {
        WinFragment.show(supportFragmentManager, results)
    }

    override fun closeWinFragment() {
        WinFragment.close(supportFragmentManager)
    }

    override fun getPreferences(): SharedPreferences? = getPreferences(Context.MODE_PRIVATE)

    fun onSettingsClosed() {
        presenter.refresh()
    }

    fun onCardFlipped() {
        presenter.onCardFlipped()
    }

    fun onNext() {
        presenter.onNextGame()
    }

    fun onWin() {
        presenter.onWin()
    }

    private fun RecyclerView.setCards(cards: List<Card>) {
        layoutManager = GridLayoutManager(this@MainActivity, determineSpan(cards.size))
        adapter = CardsAdapter(this@MainActivity, cards)
        if (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
        addItemDecoration(
            GridSpacingItemDecoration(
                determineSpan(cards.size),
                determineSpacing(cards.size),
                true
            )
        )
    }

    private fun determineSpan(cardNumber: Int) =
        when(cardNumber) {
            12 -> 3
            else -> 4
        }

    private fun determineSpacing(cardNumber: Int) =
        when(cardNumber) {
            12 -> 20
            16 -> 16
            20 -> 12
            else -> 8
        }
}
