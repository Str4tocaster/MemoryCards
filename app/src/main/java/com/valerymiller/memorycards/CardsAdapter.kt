package com.valerymiller.memorycards

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card.view.*
import kotlin.random.Random

class CardsAdapter(val context: Context, val items: List<Card>)
    : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    private val openCards = mutableListOf<CardViewHolder>()
    private var hidedCards = 0
    private val cardBack = when (Random.nextInt(0, 6)) {
        0 -> context.getDrawable(R.drawable.card_back_1)
        1 -> context.getDrawable(R.drawable.card_back_2)
        2 -> context.getDrawable(R.drawable.card_back_3)
        3 -> context.getDrawable(R.drawable.card_back_4)
        4 -> context.getDrawable(R.drawable.card_back_5)
        else -> context.getDrawable(R.drawable.card_back_6)
    }

    private val closeHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            closeCards()
        }
    }

    private val hideHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            hideCards()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(context, LayoutInflater.from(context)
            .inflate(R.layout.card, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.cardId = items[position].id
        holder.cardContainer.setOnClickListener {
            if (holder.open || openCards.size > 1) return@setOnClickListener
            else holder.flipCard()
        }
        holder.imageViewBack.setImageBitmap(items[position].image)
        holder.imageView.setImageDrawable(cardBack)
    }

    inner class CardViewHolder(context: Context, itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val animationIn = AnimatorInflater.loadAnimator(context, R.animator.animation_in) as AnimatorSet
        val animationOut = AnimatorInflater.loadAnimator(context, R.animator.animation_out) as AnimatorSet
        val cardFrontLayout = itemView.card_front
        val cardBackLayout = itemView.card_back

        val cardContainer = itemView.cardContainer
        val imageView = itemView.imageView
        val imageViewBack = itemView.imageViewBack
        var open = false
        var cardId = -1

        init {
            val distance = 8000
            val scale = context.resources.displayMetrics.density * distance
            cardFrontLayout.setCameraDistance(scale)
            cardBackLayout.setCameraDistance(scale)
        }

        fun flipCard() {
            if (!open) {
                animationOut.setTarget(cardFrontLayout)
                animationIn.setTarget(cardBackLayout)
                animationOut.start()
                animationIn.start()
            } else {
                animationOut.setTarget(cardBackLayout)
                animationIn.setTarget(cardFrontLayout)
                animationOut.start()
                animationIn.start()
            }
            open = !open
            if (open) this@CardsAdapter.onCardOpen(this@CardViewHolder)
        }

        fun hideCard() {
            cardContainer.visibility = View.GONE
        }
    }

    fun onCardOpen(holder: CardViewHolder) {
        openCards.add(holder)
        if (openCards.size > 1) {
            if (openCards[0].cardId == openCards[1].cardId)
                startHideTimer()
            else startCloseTimer()
            if (context is MainActivity) context.onActionCounterIncreased()
        }
        if (openCards.size == 1 && context is MainActivity)
            context.onGameStarted()
    }

    private fun startCloseTimer() {
        Thread(Runnable {
            Thread.sleep(1000)
            closeHandler.sendEmptyMessage(1)
        }).start()
    }

    private fun startHideTimer() {
        Thread(Runnable {
            Thread.sleep(1000)
            hideHandler.sendEmptyMessage(1)
        }).start()
    }

    private fun closeCards() {
        openCards[0].flipCard()
        openCards[1].flipCard()
        openCards.clear()
    }

    private fun hideCards() {
        openCards[0].hideCard()
        openCards[1].hideCard()
        openCards.clear()
        hidedCards += 2
        if (hidedCards == items.size && context is MainActivity)
            context.onWin()
    }

}