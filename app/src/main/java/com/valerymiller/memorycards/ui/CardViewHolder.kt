package com.valerymiller.memorycards.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.valerymiller.memorycards.R
import kotlinx.android.synthetic.main.card.view.*

interface CardListener {
    fun onCardOpen(card: CardViewHolder)
}

class CardViewHolder(
    private val context: Context,
    private val listener: CardListener,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    var cardId = -1
    var open = false

    val cardContainer = itemView.cardContainer
    val imageView = itemView.imageView
    val imageViewBack = itemView.imageViewBack

    private val cardFrontLayout = itemView.card_front
    private val cardBackLayout = itemView.card_back

    private val animationFlipIn = initAnimationFlipIn()
    private val animationFlipOut = initAnimationFlipOut()
    private val animationUpAndDown = initAnimationUpAndDown()
    private val animationAlpha = initAnimationAlpha()

    init {
        setCameraDistance()
    }

    fun flipCard() {
        open = !open
        animationFlipOut.setTarget(if (open) cardFrontLayout else cardBackLayout)
        animationFlipIn.setTarget(if (open) cardBackLayout else cardFrontLayout)
        animationFlipOut.start()
        animationFlipIn.start()
        if (open) listener.onCardOpen(this)
    }

    fun hideCard() {
        animationAlpha.setTarget(imageViewBack)
        animationUpAndDown.setTarget(cardBackLayout)
        animationAlpha.start()
        animationUpAndDown.start()
    }

    private fun setCameraDistance() {
        val distance = 8000
        val scale = context.resources.displayMetrics.density * distance
        cardFrontLayout.setCameraDistance(scale)
        cardBackLayout.setCameraDistance(scale)
    }

    private fun initAnimationFlipIn(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_flip_in) as AnimatorSet

    private fun initAnimationFlipOut(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_flip_out) as AnimatorSet

    private fun initAnimationUpAndDown(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_up_and_down) as AnimatorSet

    private fun initAnimationAlpha(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_alpha) as AnimatorSet
}