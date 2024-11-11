package com.example.pagepals1.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "bookClubTable")
data class BookClub (
    @PrimaryKey(autoGenerate = true) // lets room make the ids for us
    val clubId: Int,
    val clubName: String,
    val host: String,
    val city: String
    val hostId: String,
    val members: List<String>
): Parcelable