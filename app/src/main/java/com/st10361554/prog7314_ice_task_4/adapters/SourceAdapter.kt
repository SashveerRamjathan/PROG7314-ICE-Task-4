package com.st10361554.prog7314_ice_task_4.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.st10361554.prog7314_ice_task_4.databinding.ItemSourceBinding
import com.st10361554.prog7314_ice_task_4.models.NewsSource
import java.util.*
import androidx.core.net.toUri

/**
 * RecyclerView adapter for displaying news sources in a list format.
 * Uses View Binding for efficient view management and handles click events for opening source websites.
 *
 * @param sources Mutable list of NewsSource objects to be displayed
 */
class SourceAdapter(private val sources: MutableList<NewsSource>) : RecyclerView.Adapter<SourceAdapter.SourceViewHolder>()
{

    /**
     * ViewHolder class that holds references to the views for each news source item.
     * Uses View Binding for type-safe access to UI components.
     *
     * @param binding The ItemSourceBinding object containing references to UI components
     */
    class SourceViewHolder(private val binding: ItemSourceBinding) : RecyclerView.ViewHolder(binding.root)
    {
        /**
         * Binds news source data to the UI components and sets up click listeners.
         * Formats text data with proper capitalization and prefixes for better readability.
         *
         * @param source The NewsSource object containing data to be displayed
         */
        @SuppressLint("SetTextI18n")
        fun bind(source: NewsSource) {
            // Set the news source name directly
            binding.tvSourceName.text = source.name

            // Set the description of the news source
            binding.tvSourceDescription.text = source.description

            // Format and display the category with proper title case
            // Capitalizes the first letter of the category name
            binding.tvSourceCategory.text =
                "Category: ${source.category.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}"

            // Display country code in uppercase with "Country:" prefix
            binding.tvSourceCountry.text = "Country: ${source.country.uppercase()}"

            // Display language code in uppercase with "Language:" prefix
            binding.tvSourceLanguage.text = "Language: ${source.language.uppercase()}"

            // Set up click listener to open the news source's website in a browser
            binding.root.setOnClickListener {
                // Create intent to open source URL in default browser app
                val intent = Intent(Intent.ACTION_VIEW, source.url.toUri())
                binding.root.context.startActivity(intent)
            }
        }
    }

    /**
     * Creates new ViewHolder instances when needed by the RecyclerView.
     * Inflates the source item layout using View Binding and returns a new SourceViewHolder.
     *
     * @param parent The parent ViewGroup
     * @param viewType The view type (not used in this simple adapter)
     * @return New SourceViewHolder instance with inflated binding
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder
    {
        // Inflate the source item layout using View Binding
        val binding = ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SourceViewHolder(binding)
    }

    /**
     * Binds data to an existing ViewHolder at the specified position.
     * Called by RecyclerView when it needs to display data at a specific position.
     *
     * @param holder The SourceViewHolder to bind data to
     * @param position The position of the item in the data set
     */
    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        // Bind the news source at the current position to the view holder
        holder.bind(sources[position])
    }

    /**
     * Returns the total number of items in the data set.
     * Used by RecyclerView to determine how many items to display.
     *
     * @return The size of the sources list
     */
    override fun getItemCount(): Int = sources.size

}