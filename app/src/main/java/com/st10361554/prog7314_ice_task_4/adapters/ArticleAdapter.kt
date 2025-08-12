package com.st10361554.prog7314_ice_task_4.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.st10361554.prog7314_ice_task_4.R
import com.st10361554.prog7314_ice_task_4.models.Article
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.net.toUri

/**
 * RecyclerView adapter for displaying news articles in a list format.
 * Handles the binding of article data to view holders and manages click events for opening articles.
 *
 * @param articles Mutable list of Article objects to be displayed
 */
class ArticleAdapter(private val articles: MutableList<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>()
{
    /**
     * ViewHolder class that holds references to the views for each article item.
     * Responsible for binding article data to the UI components.
     *
     * @param itemView The root view of the article item layout
     */
    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        // Initialize UI components by finding them in the item layout
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvSource: TextView = itemView.findViewById(R.id.tvSource)
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        private val tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
        private val ivArticleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)

        /**
         * Binds article data to the UI components and sets up click listeners.
         *
         * @param article The Article object containing data to be displayed
         */
        fun bind(article: Article) {
            // Set text data for each UI component, with fallbacks for null values
            tvTitle.text = article.title
            tvDescription.text = article.description ?: "No description available"
            tvSource.text = article.source.name
            tvAuthor.text = article.author ?: "Unknown author"
            tvPublishedAt.text = formatDate(article.publishedAt)

            // Load and display article image using Glide library
            if (!article.urlToImage.isNullOrEmpty()) {
                // Use Glide to load image from URL with placeholder and error handling
                Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.ic_default_image) // Show placeholder while loading
                    .error(R.drawable.ic_default_image) // Show error image if loading fails
                    .into(ivArticleImage)
            } else {
                // Set default placeholder image when no image URL is available
                ivArticleImage.setImageResource(R.drawable.ic_default_image)
            }

            // Set up click listener to open the full article in a web browser
            itemView.setOnClickListener {
                // Create intent to open article URL in default browser app
                val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                itemView.context.startActivity(intent)
            }
        }

        /**
         * Formats the date string from API format to a more readable format.
         * Converts from "yyyy-MM-dd'T'HH:mm:ss'Z'" to "MMM dd, yyyy HH:mm".
         *
         * @param dateString The date string in ISO format from the API
         * @return Formatted date string or original string if parsing fails
         */
        private fun formatDate(dateString: String): String {
            return try {
                // Define input format (ISO 8601 format from API)
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                // Define output format for display (more user-friendly)
                val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                // Parse the input date and format it for display
                val date = inputFormat.parse(dateString)
                outputFormat.format(date!!)
            } catch (_: Exception) {
                // Return original string if date parsing fails
                dateString
            }
        }
    }

    /**
     * Creates new ViewHolder instances when needed by the RecyclerView.
     * Inflates the article item layout and returns a new ArticleViewHolder.
     *
     * @param parent The parent ViewGroup
     * @param viewType The view type (not used in this simple adapter)
     * @return New ArticleViewHolder instance
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        // Inflate the article item layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    /**
     * Binds data to an existing ViewHolder at the specified position.
     * Called by RecyclerView when it needs to display data at a specific position.
     *
     * @param holder The ViewHolder to bind data to
     * @param position The position of the item in the data set
     */
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        // Bind the article at the current position to the view holder
        holder.bind(articles[position])
    }

    /**
     * Returns the total number of items in the data set.
     * Used by RecyclerView to determine how many items to display.
     *
     * @return The size of the articles list
     */
    override fun getItemCount(): Int = articles.size

}