package com.example.save_your_train.data

import androidx.room.*


@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY dateMs")
    fun getAll(): MutableList<History>

    @Query("SELECT * FROM history WHERE dateMs == :dateMs")
    fun findByDate(dateMs: String): History

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg history: History)

    @Delete
    fun delete(history: History)
}