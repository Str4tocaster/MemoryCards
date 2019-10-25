package com.valerymiller.memorycards.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valerymiller.memorycards.R
import com.valerymiller.memorycards.model.Card
import com.valerymiller.memorycards.model.Results
import com.valerymiller.memorycards.widget.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

interface MainView {
    fun updateScreen(cards: List<Card>, nickname: String)
    fun setActionCountText(actionCount: String)
    fun showProgress(show: Boolean)
    fun showWinFragment(results: Results)
    fun closeWinFragment()
    fun showSettingsFragment(cardNumber: Int, nickname: String)
    fun getPreferences(): SharedPreferences?
}

class MainActivity :
    AppCompatActivity(),
    MainView,
    View.OnClickListener,
    WinFragmentListener,
    SettingsFragmentListener
{

    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSettings.setOnClickListener(this)
        btnTop.setOnClickListener(this)
        btnRestart.setOnClickListener(this)

        presenter = MainPresenterImpl(this, this)
        presenter.onCreateGame()
    }

    override fun onClick(view: View?) {
        when(view) {
            btnSettings -> presenter.onSettingsOpen()
            btnTop -> Toast.makeText(this, "Top", Toast.LENGTH_SHORT).show()
            btnRestart -> presenter.onRestartGame()
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

    override fun showSettingsFragment(cardNumber: Int, nickname: String) {
        SettingsBottomSheetFragment.show(
            supportFragmentManager,
            cardNumber,
            nickname
        )
    }

    override fun getPreferences(): SharedPreferences? = getPreferences(Context.MODE_PRIVATE)

    override fun onNext() {
        presenter.onNextGame()
    }

    override fun onSettingsClosed(cardNumber: Int, nickname: String) {
        presenter.onSettingsClosed(cardNumber, nickname)
    }

    fun onCardFlipped() {
        // todo сделать через интерфейс
        presenter.onCardFlipped()
    }

    fun onWin() {
        // todo сделать через интерфейс
        presenter.onWinGame()
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
