package com.ekochkov.intervallearning.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration


@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    companion object {
        private val DB_NAME = "words_db.sqlite"
        private val DB_PATH = "databases/words_db.sqlite"
    }



    object DatabaseProvider {

        private lateinit var INSTANCE : WordDatabase

        public fun getInstance(context: Context): WordDatabase {
            if (!::INSTANCE.isInitialized) {
                buildWordDatabase(context)
            } else if (!INSTANCE.isOpen) {
                buildWordDatabase(context)
            }
            return INSTANCE
        }

        private fun buildWordDatabase(context: Context) {
            INSTANCE = Room.databaseBuilder(
                context,
                WordDatabase::class.java, DB_NAME)
                .createFromAsset(DB_PATH)
                .build()
        }
    }




}