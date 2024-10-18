package com.example.pagepals1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookClubTable")
data class BookClub (
    @PrimaryKey(autoGenerate = true) // lets room make the ids for us
    val clubId: Int,
    val clubName: String,
    val host: String
)