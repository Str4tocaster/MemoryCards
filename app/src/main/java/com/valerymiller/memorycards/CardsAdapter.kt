package com.valerymiller.memorycards

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card.view.*


class CardsAdapter(val context: Context, val size: Int)
    : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(context, LayoutInflater.from(context)
            .inflate(R.layout.card, parent, false))
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.startFlipAnimation.setTarget(holder.cardContainer)
        holder.endFlipAnimation.setTarget(holder.cardContainer)
        holder.cardContainer.setOnClickListener {
            if (holder.open) return@setOnClickListener
            else holder.flipCard()
        }
    }

    class CardViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startFlipAnimation = AnimatorInflater.loadAnimator(context,
            R.animator.card_flip) as AnimatorSet
        val endFlipAnimation = AnimatorInflater.loadAnimator(context,
            R.animator.card_flip_half) as AnimatorSet

        val cardContainer = itemView.cardContainer
        val imageView = itemView.imageView
        var open = false

        fun flipCard() {
            open = !open
            endFlipAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    imageView.setImageResource(
                        if (open) R.drawable.ic_launcher_background_back
                        else R.drawable.ic_launcher_background
                    )
                }
            })
            startFlipAnimation.start()
            endFlipAnimation.start()
        }
    }

}