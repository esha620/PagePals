package com.example.pagepals1.data

import androidx.lifecycle.LiveData

class BookClubRepository(private val bookClubDao: BookClubDao) {

    val readAllData: LiveData<List<BookClub>> = bookClubDao.readAllData()

    suspend fun addBookClub(bookClub: BookClub) {
        bookClubDao.addBookClub(bookClub)
    }

    suspend fun updateBookClub(bookClub: BookClub) {
        bookClubDao.updateBookClub(bookClub)
    }

    suspend fun deleteBookClub(bookClub: BookClub) {
        bookClubDao.deleteBookClub(bookClub)
    }
}