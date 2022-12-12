package com.example.save_your_train.data

import android.content.Context
import androidx.room.*


@Database(entities = [Exercise::class, History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        var data: AppDatabase? = null

        fun setDatabase(context: Context) {
            data = synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase ::class.java,
                    "save_your_train"
                ).build()
                instance
            }
        }
    }
}

