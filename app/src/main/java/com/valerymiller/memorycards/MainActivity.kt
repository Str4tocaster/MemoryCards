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
        presenter.loadSettings()
        presenter.updateGameField()
    }

    override fun onClick(view: View?) {
        when(view) {
            btnSettings -> {
                SettingsBottomSheetFragment().show(supportFragmentManager, "settings")
            }

            btnTop -> Toast.makeText(this, "Top", Toast.LENGTH_SHORT).show()
            btnRestart -> {
                presenter.updateGameField()
            }
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

    override fun getPreferences(): SharedPreferences? = getPreferences(Context.MODE_PRIVATE)

    fun onSettingsClosed() {
        presenter.loadSettings()
        presenter.updateGameField()
    }

    fun onCardFlipped() {
        presenter.onCardFlipped()
    }

    fun onGameStarted() {

    }

    fun onNext() {
        presenter.updateGameField()
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentByTag("results")
        if (fragment != null) {
            transaction.remove(fragment)
            transaction.commit()
        }
    }

    fun onWin() {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = WinFragment()
        val bundle = Bundle()
        bundle.putString(WinFragment.constants.NICKNAME, presenter.getNickname())
        bundle.putInt(WinFragment.constants.ACTION_COUNT, presenter.getActionCount())
        bundle.putInt(WinFragment.constants.SCORE, getScore(presenter.getCardNumber(), presenter.getActionCount()))
        fragment.arguments = bundle
        transaction.add(R.id.container, fragment, "results")
        transaction.commit()
    }

    private fun getScore(cardNumber: Int, actionCount: Int): Int {
        return cardNumber * actionCount
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
