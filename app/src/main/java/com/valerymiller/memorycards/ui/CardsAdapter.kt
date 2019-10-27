package com.valerymiller.memorycards.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.valerymiller.memorycards.R
import com.valerymiller.memorycards.model.Card

private const val DELAY_STEP = 30L

interface CardsAdapterListener {
    fun onCardFlipped(cardId: Int)
}

class CardsAdapter(
    private val context: Context,
    private val listener: CardsAdapterListener,
    private val cardBack: Drawable?,
    private val items: List<Card>
) : RecyclerView.Adapter<CardViewHolder>(),
    CardListener
{
    private val cards = mutableListOf<CardViewHolder>()
    private val openCards = mutableListOf<CardViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(context, this, LayoutInflater.from(context)
            .inflate(R.layout.card, parent, false)).apply {
            cards.add(this)
        }
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

    override fun onCardOpen(card: CardViewHolder) {
        openCards.add(card)
        listener.onCardFlipped(card.cardId)
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

    fun startUpAndDownAnimation() {
        var delay = 0L
        cards.map { card ->
            card.upAndDownCard(delay)
            delay += DELAY_STEP
        }
    }
}