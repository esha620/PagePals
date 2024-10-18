package com.example.pagepals1.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookClubViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<BookClub>>
    private val repository: BookClubRepository

    init {
        val bookClubDao = BookClubDatabase.getDatabase(application).bookClubDao()
        repository = BookClubRepository(bookClubDao)
        readAllData = repository.readAllData
    }

    fun addBookClub(bookClub: BookClub) {
        // launch used for coroutines
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBookClub(bookClub)
        }
    }

    fun updateBookClub(bookClub: BookClub) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBookClub(bookClub)
        }
    }

    fun deleteBookClub(bookClub: BookClub) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBookClub(bookClub)
        }
    }

}