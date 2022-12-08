package com.example.save_your_train.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Exercise(
    @PrimaryKey val name: String,
    val description: String?
)

