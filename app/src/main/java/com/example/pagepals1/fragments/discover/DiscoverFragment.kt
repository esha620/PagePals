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
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pagepals1.BookRVAdapter
import com.example.pagepals1.NetworkMonitor
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
    private lateinit var mRequestQueue: RequestQueue


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRequestQueue = Volley.newRequestQueue(requireContext())

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
            val networkMonitor = NetworkMonitor.getInstance()
            if (!networkMonitor.isConnected()) {
                Toast.makeText(requireContext(), "No network connection", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loadingPB.visibility = View.VISIBLE
            if (searchEdt.text.isNullOrEmpty()) {
                searchEdt.error = "Please enter search query"
            } else {
                getBooksData(searchEdt.text.toString())
            }
        }
    }

    private fun getBooksData(searchQuery: String) {
        val url = "https://www.googleapis.com/books/v1/volumes?q=$searchQuery&fields=items(volumeInfo(title,subtitle,authors,publisher,publishedDate,description,pageCount,imageLinks,previewLink,infoLink),saleInfo(buyLink))"

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            loadingPB.visibility = View.GONE
            try {
                // Handle empty response early
                if (!response.has("items") || response.getJSONArray("items").length() == 0) {
                    Toast.makeText(requireContext(), "No books found.", Toast.LENGTH_SHORT).show()
                    return@JsonObjectRequest
                }

                val itemsArray = response.getJSONArray("items")
                val newBooksList = ArrayList<BookRVModal>()

                for (i in 0 until itemsArray.length()) {
                    val itemsObj = itemsArray.getJSONObject(i)
                    val volumeObj = itemsObj.getJSONObject("volumeInfo")

                    val authorsArray = volumeObj.optJSONArray("authors") ?: continue
                    val authorsArrayList = ArrayList<String>().apply {
                        for (j in 0 until authorsArray.length()) {
                            add(authorsArray.optString(j))
                        }
                    }

                    val bookInfo = BookRVModal(
                        title = volumeObj.optString("title"),
                        subtitle = volumeObj.optString("subtitle"),
                        authors = authorsArrayList,
                        publisher = volumeObj.optString("publisher"),
                        publishedDate = volumeObj.optString("publishedDate"),
                        description = volumeObj.optString("description"),
                        pageCount = volumeObj.optInt("pageCount"),
                        thumbnail = volumeObj.optJSONObject("imageLinks")?.optString("thumbnail") ?: "",
                        previewLink = volumeObj.optString("previewLink"),
                        infoLink = volumeObj.optString("infoLink"),
                        buyLink = itemsObj.optJSONObject("saleInfo")?.optString("buyLink") ?: ""
                    )
                    newBooksList.add(bookInfo)
                }

                booksList.clear()
                booksList.addAll(newBooksList)
                bookAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, { error ->
            loadingPB.visibility = View.GONE
            Toast.makeText(requireContext(), "No books found.", Toast.LENGTH_SHORT).show()
        })

        mRequestQueue.add(request)
    }

}
