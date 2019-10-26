package com.valerymiller.memorycards.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.valerymiller.memorycards.R

private const val DELAY_STEP = 30L

interface ShimmerCardsAdapterListener {
    fun onHideAnimationEnd()
}

class ShimmerCardsAdapter(
    private val context: Context,
    private val listener: ShimmerCardsAdapterListener,
    private val count: Int
) : RecyclerView.Adapter<ShimmerCardViewHolder>(),
    ShimmerCardListener {

    private val cards = mutableListOf<ShimmerCardViewHolder>()
    private var cardsHided: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerCardViewHolder {
        return ShimmerCardViewHolder(context, this, LayoutInflater.from(context)
            .inflate(R.layout.shimmer_card, parent, false)).apply {
            cards.add(this)
        }
    }

    override fun getItemCount(): Int = count

    override fun onBindViewHolder(holder: ShimmerCardViewHolder, position: Int) {}

    override fun onAnimationEnd() {
        cardsHided++
        if (cardsHided == count) {
            listener.onHideAnimationEnd()
        }
    }

    fun startHideAnimation() {
        var delay = 0L
        cards.map { card ->
            card.animateCard(delay)
            delay += DELAY_STEP
        }
    }
}