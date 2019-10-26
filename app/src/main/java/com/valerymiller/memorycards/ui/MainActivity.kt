package com.valerymiller.memorycards.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
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
    fun updateScreen(cards: List<Card>, cardBack: Drawable?, nickname: String)
    fun setActionCountText(actionCount: String)
    fun showProgress(size: Int)
    fun hideProgress()
    fun showWinFragment(results: Results)
    fun closeWinFragment()
    fun showSettingsFragment(cardNumber: Int, nickname: String)
    fun getPreferences(): SharedPreferences?
    fun closeCards()
    fun hideCards()
}

class MainActivity :
    AppCompatActivity(),
    MainView,
    View.OnClickListener,
    WinFragmentListener,
    SettingsFragmentListener,
    CardsAdapterListener
{

    lateinit var presenter: MainPresenter
    lateinit var adapter: CardsAdapter

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

    override fun updateScreen(cards: List<Card>, cardBack: Drawable?, nickname: String) {
        recyclerView.setCards(cardBack, cards)
        tvNickname.text = nickname
    }

    override fun setActionCountText(actionCount: String) {
        tvActionsCount.text = actionCount
    }

    override fun showProgress(size: Int) {
        shimmerRecyclerView.setShimmerCards(size)
        shimmerView.startShimmer()
        shimmerView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun hideProgress() {
        shimmerView.stopShimmer()
        shimmerView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
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

    override fun closeCards() {
        adapter.closeCards()
    }

    override fun hideCards() {
        adapter.hideCards()
    }

    override fun onNext() {
        presenter.onNextGame()
    }

    override fun onSettingsClosed(cardNumber: Int, nickname: String) {
        presenter.onSettingsClosed(cardNumber, nickname)
    }

    override fun onCardFlipped(cardId: Int) {
        presenter.onCardFlipped(cardId)
    }

    private fun RecyclerView.setCards(cardBack: Drawable?, cards: List<Card>) {
        layoutManager = GridLayoutManager(this@MainActivity, determineSpan(cards.size))
        this@MainActivity.adapter = CardsAdapter(this@MainActivity, this@MainActivity, cardBack, cards)
        adapter = this@MainActivity.adapter
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

    private fun RecyclerView.setShimmerCards(count: Int) {
        layoutManager = GridLayoutManager(this@MainActivity, determineSpan(count))
        adapter = ShimmerCardsAdapter(this@MainActivity, count)
        if (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
        addItemDecoration(
            GridSpacingItemDecoration(
                determineSpan(count),
                determineSpacing(count),
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
