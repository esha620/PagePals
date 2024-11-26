import com.example.pagepals1.data.BookClub
import com.example.pagepals1.fragments.location.LocListAdapter
import junit.framework.TestCase
import org.junit.Test

class LocationFragmentTest {

    @Test
    fun testLocationAdapter() {
        val locationAdapter = LocListAdapter()

        val testClub1 = BookClub(1, "Club 1", "Columbus", "", emptyList())
        val testClub2 = BookClub(2, "Club 2", "Mountain View", "", emptyList())
        val testClubs = listOf(testClub1, testClub2)
        locationAdapter.setData2(testClub1)
        locationAdapter.setData2(testClub2)

        TestCase.assertEquals(testClubs, locationAdapter.clubList)
    }

    @Test
    fun testNoDuplicatesInAdapter() {
        val locationAdapter = LocListAdapter()
        val testClub1 = BookClub(1, "Club 1", "Columbus", "", emptyList())
        val testClub2 = BookClub(2, "Club 2", "Mountain View", "", emptyList())
        val testClub3 = BookClub(1, "Club 1", "Columbus", "", emptyList())
        val testClubs = listOf(testClub1, testClub2)
        locationAdapter.setData2(testClub1)
        locationAdapter.setData2(testClub2)
        locationAdapter.setData2(testClub3)

        TestCase.assertEquals(testClubs, locationAdapter.clubList)

    }

}
