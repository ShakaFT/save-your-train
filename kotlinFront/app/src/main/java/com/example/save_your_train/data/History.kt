package com.example.save_your_train.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class History(
    @PrimaryKey val dateMs: Double,
    val name: String,
    val execution: String,
    val repetition: String,
    val rest: String,
    val series: String,
    val weight: String
)

fun History.toMap() = mapOf(
    "dateMs" to dateMs,
    "exerciseName" to name,
    "execution" to execution,
    "repetition" to repetition,
    "rest" to rest,
    "series" to series,
    "weight" to weight
)