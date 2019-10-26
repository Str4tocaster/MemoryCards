package com.valerymiller.memorycards.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.valerymiller.memorycards.R

class ShimmerCardsAdapter(
    private val context: Context,
    private val count: Int
) : RecyclerView.Adapter<ShimmerCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerCardViewHolder {
        return ShimmerCardViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.shimmer_card, parent, false))
    }

    override fun getItemCount(): Int = count

    override fun onBindViewHolder(holder: ShimmerCardViewHolder, position: Int) {

    }
}

class ShimmerCardViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView)