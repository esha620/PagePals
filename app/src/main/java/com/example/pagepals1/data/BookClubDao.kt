package com.example.pagepals1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookClubDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBookClub(bookClub: BookClub)

    @Update
    suspend fun updateBookClub(bookClub: BookClub)

    @Delete
    suspend fun deleteBookClub(bookClub: BookClub)

    @Query("SELECT * FROM bookClubTable ORDER BY clubId ASC")
    fun readAllData(): LiveData<List<BookClub>>

}