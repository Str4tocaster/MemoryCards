package com.valerymiller.memorycards.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.valerymiller.memorycards.R
import com.valerymiller.memorycards.model.Card
import kotlinx.android.synthetic.main.card.view.*
import kotlin.random.Random

interface CardsAdapterListener {
    fun onCardFlipped(cardId: Int)
}

class CardsAdapter(
    private val context: Context,
    private val listener: CardsAdapterListener,
    private val items: List<Card>
) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    private val openCards = mutableListOf<CardViewHolder>()

    // todo - передавать в конструкторе из presenter
    private val cardBack = when (Random.nextInt(0, 6)) {
        0 -> context.getDrawable(R.drawable.card_back_1)
        1 -> context.getDrawable(R.drawable.card_back_2)
        2 -> context.getDrawable(R.drawable.card_back_3)
        3 -> context.getDrawable(R.drawable.card_back_4)
        4 -> context.getDrawable(R.drawable.card_back_5)
        else -> context.getDrawable(R.drawable.card_back_6)
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

    fun closeCards() {
        openCards[0].flipCard()
        openCards[1].flipCard()
        openCards.clear()
    }

    fun hideCards() {
        openCards[0].hideCard()
        openCards[1].hideCard()
        openCards.clear()
    }

    private fun onCardOpen(holder: CardViewHolder) {
        openCards.add(holder)
        listener.onCardFlipped(holder.cardId)
    }

    private fun initAnimationIn(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_in) as AnimatorSet

    private fun initAnimationOut(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_out) as AnimatorSet

    inner class CardViewHolder(context: Context, itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val animationIn = initAnimationIn()
        val animationOut = initAnimationOut()
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

}