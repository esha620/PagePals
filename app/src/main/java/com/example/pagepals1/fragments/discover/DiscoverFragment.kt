package com.example.pagepals1.fragments.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pagepals1.BookRVAdapter
import com.example.pagepals1.data.BookRVModal
import com.example.pagepals1.R

class DiscoverFragment : Fragment() {

    // Declare variables
    private lateinit var booksList: ArrayList<BookRVModal>
    private lateinit var loadingPB: ProgressBar
    private lateinit var searchEdt: EditText
    private lateinit var searchBtn: ImageButton
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var bookAdapter: BookRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        loadingPB = view.findViewById(R.id.idLoadingPB)
        searchEdt = view.findViewById(R.id.idEdtSearchBooks)
        searchBtn = view.findViewById(R.id.idBtnSearch)
        mRecyclerView = view.findViewById(R.id.idRVBooks)

        // Initialize the RecyclerView and adapter
        booksList = ArrayList()
        mRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        bookAdapter = BookRVAdapter(booksList, requireActivity())
        mRecyclerView.adapter = bookAdapter

        // Set up search button click listener
        searchBtn.setOnClickListener {
            loadingPB.visibility = View.VISIBLE
            if (searchEdt.text.isNullOrEmpty()) {
                searchEdt.error = "Please enter search query"
            } else {
                getBooksData(searchEdt.text.toString())
            }
        }
    }

    private fun getBooksData(searchQuery: String) {
        // Request queue
        val mRequestQueue = Volley.newRequestQueue(requireContext())
        mRequestQueue.cache.clear()

        val url = "https://www.googleapis.com/books/v1/volumes?q=$searchQuery"
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            loadingPB.visibility = View.GONE
            try {
                // Clear previous data
                booksList.clear()

                val itemsArray = response.getJSONArray("items")
                for (i in 0 until itemsArray.length()) {
                    val itemsObj = itemsArray.getJSONObject(i)
                    val volumeObj = itemsObj.getJSONObject("volumeInfo")

                    val title = volumeObj.optString("title")
                    val subtitle = volumeObj.optString("subtitle")
                    val authorsArray = volumeObj.optJSONArray("authors") ?: continue
                    val publisher = volumeObj.optString("publisher")
                    val publishedDate = volumeObj.optString("publishedDate")
                    val description = volumeObj.optString("description")
                    val pageCount = volumeObj.optInt("pageCount")
                    val imageLinks = volumeObj.optJSONObject("imageLinks")
                    val thumbnail = imageLinks?.optString("thumbnail") ?: ""
                    val previewLink = volumeObj.optString("previewLink")
                    val infoLink = volumeObj.optString("infoLink")
                    val saleInfoObj = itemsObj.optJSONObject("saleInfo")
                    val buyLink = saleInfoObj?.optString("buyLink") ?: ""

                    val authorsArrayList = ArrayList<String>()
                    for (j in 0 until authorsArray.length()) {
                        authorsArrayList.add(authorsArray.optString(j))
                    }

                    // Add book info to the list
                    val bookInfo = BookRVModal(
                        title, subtitle, authorsArrayList, publisher,
                        publishedDate, description, pageCount,
                        thumbnail, previewLink, infoLink, buyLink
                    )
                    booksList.add(bookInfo)
                }

                // Notify adapter of data changes
                bookAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, { error ->
            loadingPB.visibility = View.GONE
            Toast.makeText(requireContext(), "No books found.", Toast.LENGTH_SHORT).show()
        })

        // Add request to queue
        mRequestQueue.add(request)
    }
}
