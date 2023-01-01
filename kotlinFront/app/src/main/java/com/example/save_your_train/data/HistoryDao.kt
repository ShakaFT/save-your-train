package com.example.save_your_train.data

import androidx.room.*


@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY dateMs DESC")
    fun getAll(): MutableList<History>

    @Query("SELECT * FROM history WHERE name == :name ORDER BY dateMs DESC")
    fun findByName(name: String): MutableList<History>

    @Query("SELECT * FROM history WHERE dateMs == :dateMs")
    fun findByDate(dateMs: String): History

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg history: History)

    @Delete
    fun delete(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()
}