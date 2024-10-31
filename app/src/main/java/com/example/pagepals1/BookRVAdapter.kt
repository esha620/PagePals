package com.example.pagepals1
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.pagepals1.activities.BookDetailsActivity
import com.example.pagepals1.data.BookRVModal
import com.squareup.picasso.Picasso

class BookRVAdapter(
    // on below line we are passing variables
    // as course list and context
    private var bookList: ArrayList<BookRVModal>,
    private var ctx: Context
) : RecyclerView.Adapter<BookRVAdapter.BookViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookRVAdapter.BookViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.book_item,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return BookRVAdapter.BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookRVAdapter.BookViewHolder, position: Int) {
        val bookInfo = bookList[position]

        // Check if the thumbnail URL is empty or null
        val thumbnailUrl = bookInfo.thumbnail
        Log.d("BookThumbnail", "Thumbnail URL: ${bookInfo.thumbnail}")

        val fixedUrl = thumbnailUrl.replace("http://", "https://")
        Glide.with(ctx)
            .load(fixedUrl)
            .placeholder(R.drawable.baseline_question_mark_24)
            .error(R.drawable.baseline_question_mark_24)
            .into(holder.bookIV)

        holder.bookTitleTV.text = bookInfo.title
        holder.bookPagesTV.text = "Pages : " + bookInfo.pageCount

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener {
            // inside on click listener method we are calling a new activity
            // and passing all the data of that item in next intent.
            val i = Intent(ctx, BookDetailsActivity::class.java)
            i.putExtra("title", bookInfo.title)
            i.putExtra("subtitle", bookInfo.subtitle)
            i.putExtra("authors", bookInfo.authors)
            i.putExtra("publisher", bookInfo.publisher)
            i.putExtra("publishedDate", bookInfo.publishedDate)
            i.putExtra("description", bookInfo.description)
            i.putExtra("pageCount", bookInfo.pageCount)
            i.putExtra("thumbnail", fixedUrl)
            i.putExtra("previewLink", bookInfo.previewLink)
            i.putExtra("infoLink", bookInfo.infoLink)
            i.putExtra("buyLink", bookInfo.buyLink)

            // after passing that data we are
            // starting our new  intent.
            ctx.startActivity(i)
        }

    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    // Helper method to fetch cover ID from Open Library API
    private fun fetchOpenLibraryCoverId(title: String, onResult: (String?) -> Unit) {
        val queryUrl = "https://openlibrary.org/search.json?title=${Uri.encode(title)}"
        val request = JsonObjectRequest(
            Request.Method.GET, queryUrl, null,
            { response ->
                val docsArray = response.getJSONArray("docs")
                if (docsArray.length() > 0) {
                    val firstDoc = docsArray.getJSONObject(0)
                    val coverId = firstDoc.optString("cover_i")
                    onResult(coverId)
                } else {
                    onResult(null)
                }
            },
            { error ->
                Log.e("OpenLibraryError", "Error fetching cover ID: ${error.message}")
                onResult(null)
            }
        )

        // Add request to the Volley request queue
        Volley.newRequestQueue(ctx).add(request)
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our
        // course name text view and our image view.
        val bookTitleTV: TextView = itemView.findViewById(R.id.idTVBookName)
        val bookPagesTV: TextView = itemView.findViewById(R.id.idTVBookPages)
        val bookIV: ImageView = itemView.findViewById(R.id.idIVBook)
    }
}