package com.valerymiller.memorycards.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Score(
    @PrimaryKey
    val nickname: String,
    val score: Int
)