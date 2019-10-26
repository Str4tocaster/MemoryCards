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

    private val animationIn = initAnimationIn()
    private val animationOut = initAnimationOut()
    private val animationUp = initAnimationUp()
    private val animationDown = initAnimationDown()
    private val animationColor = initAnimationColor()
    private val cardFrontLayout = itemView.card_front
    private val cardBackLayout = itemView.card_back

    init {
        setCameraDistance()
    }

    fun flipCard() {
        open = !open
        animationOut.setTarget(if (open) cardFrontLayout else cardBackLayout)
        animationIn.setTarget(if (open) cardBackLayout else cardFrontLayout)
        animationOut.start()
        animationIn.start()
        if (open) listener.onCardOpen(this)
    }

    fun hideCard() {
        animationColor.setTarget(imageViewBack)
        animationUp.setTarget(cardBackLayout)
        animationDown.setTarget(cardBackLayout)
        animationColor.start()
        animationUp.start()
        animationDown.start()
    }

    private fun setCameraDistance() {
        val distance = 8000
        val scale = context.resources.displayMetrics.density * distance
        cardFrontLayout.setCameraDistance(scale)
        cardBackLayout.setCameraDistance(scale)
    }

    private fun initAnimationIn(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_in) as AnimatorSet

    private fun initAnimationOut(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_out) as AnimatorSet

    private fun initAnimationUp(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_up) as AnimatorSet

    private fun initAnimationDown(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_down) as AnimatorSet

    private fun initAnimationColor(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_color) as AnimatorSet
}