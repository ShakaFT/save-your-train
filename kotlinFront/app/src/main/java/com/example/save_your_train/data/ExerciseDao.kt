package com.example.save_your_train.data

import androidx.room.*


@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise ORDER BY name")
    fun getAll(): MutableList<Exercise>

    @Query("SELECT * FROM exercise WHERE name == :name")
    fun findByName(name: String): Exercise

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg exercise: Exercise)

    @Delete
    fun delete(exercise: Exercise)

    @Query("DELETE FROM exercise")
    fun deleteAll()
}