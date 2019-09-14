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


class CardsAdapter(val context: Context, val size: Int) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.card, parent, false))
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val startFlipAnimation = AnimatorInflater.loadAnimator(context,
            R.animator.card_flip) as AnimatorSet
        val endFlipAnimation = AnimatorInflater.loadAnimator(context,
            R.animator.card_flip_half) as AnimatorSet
        startFlipAnimation.setTarget(holder.cardContainer)
        endFlipAnimation.setTarget(holder.cardContainer)

        holder.cardContainer.setOnClickListener {
            holder.state = !holder.state
            endFlipAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    holder.imageView.setImageResource(
                        if (holder.state) R.drawable.ic_launcher_background
                        else R.drawable.ic_launcher_background_back
                    )
                }
            })
            startFlipAnimation.start()
            endFlipAnimation.start()
        }
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardContainer = itemView.cardContainer
        val imageView = itemView.imageView
        var state = true
    }

}