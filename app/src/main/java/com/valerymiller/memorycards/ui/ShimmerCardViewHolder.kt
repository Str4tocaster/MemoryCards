package com.valerymiller.memorycards.ui

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.valerymiller.memorycards.R
import kotlinx.android.synthetic.main.shimmer_card.view.*

interface ShimmerCardListener {
    fun onAnimationEnd()
}

class ShimmerCardViewHolder(
    private val context: Context,
    private val listener: ShimmerCardListener,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val cardView = itemView.cardView
    private val animationAlpha = initAnimationAlpha()

    fun animateCard(delay: Long) {
        animationAlpha.setTarget(cardView)
        animationAlpha.startDelay = delay
        animationAlpha.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                listener.onAnimationEnd()
            }
        })
        animationAlpha.start()
    }

    private fun initAnimationAlpha(): AnimatorSet =
        AnimatorInflater.loadAnimator(context, R.animator.animation_alpha) as AnimatorSet

}