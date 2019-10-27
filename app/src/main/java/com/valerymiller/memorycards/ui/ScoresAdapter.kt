package com.valerymiller.memorycards.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.valerymiller.memorycards.R
import com.valerymiller.memorycards.model.Score
import kotlinx.android.synthetic.main.scoreboard_item.view.*

class ScoresAdapter(
    private val context: Context,
    private val items: List<Score>
) : RecyclerView.Adapter<ScoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        return ScoreViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.scoreboard_item, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val result = items[position]
        holder.apply {
            tvNumber.text = "${result.position}. "
            tvNickname.text = result.nickname
            tvScore.text = result.scores.toString()
        }
    }
}

class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvNumber = itemView.tvNumber
    val tvNickname = itemView.tvNickname
    val tvScore = itemView.tvScore
}