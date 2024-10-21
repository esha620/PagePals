package com.example.pagepals1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookClub::class], version = 1)
abstract class BookClubDatabase: RoomDatabase() {

    abstract fun bookClubDao(): BookClubDao

    companion object {
        @Volatile
        private var INSTANCE: BookClubDatabase? = null

        fun getDatabase(context: Context): BookClubDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookClubDatabase::class.java,
                    "bookClubDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}