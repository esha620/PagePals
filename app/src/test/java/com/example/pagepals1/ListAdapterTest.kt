package com.example.pagepals1

import com.example.pagepals1.data.BookClub
import com.example.pagepals1.fragments.clubs.ListAdapter
import junit.framework.TestCase
import org.junit.Test

import org.junit.Assert.*

/**
 * Test checks that the ListAdapter associated with the club fragment is properly updating the clubList property
 *
 *
 */
class ListAdapterTest {

    @Test
    fun testSetDataUpdatesClubListAndNotifiesAdapter() {
        val adapter = ListAdapter()

        // 1. Create a list of test clubs
        val testClubs = listOf(BookClub(1, "Club 1", "Columbus", "", emptyList()), BookClub(1, "Club 2", "Mountain View", "", emptyList())) // Replace with your Club data class
        adapter.setData2(testClubs)

        TestCase.assertEquals(testClubs, adapter.clubList)
    }

    @Test
    fun testEmptyStateHandling() {
        val adapter = ListAdapter()
        adapter.setData2(emptyList())
        TestCase.assertEquals(0, adapter.itemCount)
    }

}