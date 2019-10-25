package com.valerymiller.memorycards.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.valerymiller.memorycards.R
import com.valerymiller.memorycards.model.Card
import kotlin.random.Random

interface CardsAdapterListener {
    fun onCardFlipped(cardId: Int)
}

class CardsAdapter(
    private val context: Context,
    private val listener: CardsAdapterListener,
    private val items: List<Card>
) : RecyclerView.Adapter<CardViewHolder>(),
    CardListener
{

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
        return CardViewHolder(context, this, LayoutInflater.from(context)
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
}