package com.valerymiller.memorycards.data.room

import androidx.room.*

@Dao
interface ScoreDao {

    @Query("SELECT * FROM score")
    fun getAll(): List<Score>

    @Query("SELECT * FROM score WHERE nickname = :nickname")
    fun getByNickname(nickname: String): Score

    @Insert
    fun insert(score: Score)

    @Update
    fun update(score: Score)

    @Delete
    fun delete(score: Score)
}