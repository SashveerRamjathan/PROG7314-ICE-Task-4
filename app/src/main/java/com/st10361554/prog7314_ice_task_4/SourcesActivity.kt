package com.st10361554.prog7314_ice_task_4

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.st10361554.prog7314_ice_task_4.adapters.SourceAdapter
import com.st10361554.prog7314_ice_task_4.databinding.ActivitySourcesBinding
import com.st10361554.prog7314_ice_task_4.models.NewsSource
import com.st10361554.prog7314_ice_task_4.repository.NewsRepository
import kotlinx.coroutines.launch

class SourcesActivity : AppCompatActivity()
{
    private lateinit var binding: ActivitySourcesBinding
    private lateinit var newsRepository: NewsRepository
    private lateinit var sourceAdapter: SourceAdapter
    private var currentSources = mutableListOf<NewsSource>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display for modern Android UI
        enableEdgeToEdge()

        // Initialize ViewBinding for type-safe view access
        binding = ActivitySourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle system window insets for edge-to-edge display
        // This ensures proper padding around system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the repository for data operations
        newsRepository = NewsRepository()

        // Set up UI components
        setupRecyclerView()
        setupBackButton()

        // Load initial sources data from the API
        loadSources()
    }

    /**
     * Configures the RecyclerView for displaying the list of news sources.
     *
     * Sets up the adapter with the current sources list and applies a
     * LinearLayoutManager for vertical scrolling list display.
     * The RecyclerView will show each news source as a separate list item.
     */
    private fun setupRecyclerView()
    {
        // Create adapter with the current sources list
        sourceAdapter = SourceAdapter(currentSources)

        // Configure RecyclerView with adapter and layout manager
        binding.recyclerViewSources.apply {
            adapter = sourceAdapter
            layoutManager = LinearLayoutManager(this@SourcesActivity)
        }
    }

    private fun setupBackButton()
    {
        binding.btnBack.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }
    }

    /**
     * Loads news sources from the News API using the repository.
     *
     * This method handles the complete flow of fetching sources data:
     * 1. Shows loading indicator (progress bar)
     * 2. Makes async API call using coroutines
     * 3. Processes the response and updates UI
     * 4. Handles various error scenarios with appropriate user feedback
     * 5. Hides loading indicator regardless of success/failure
     *
     * Error handling covers:
     * - Network connectivity issues
     * - API authentication errors (401)
     * - Rate limiting (429)
     * - Other HTTP errors
     * - Empty responses
     * - General exceptions
     */
    private fun loadSources()
    {
        // Show loading state: hide content, show progress bar
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewSources.visibility = View.GONE

        // Launch coroutine in lifecycle scope for automatic cleanup
        lifecycleScope.launch {
            try
            {
                // Make API call to fetch all available sources
                val response = newsRepository.getAllSources()

                // Check if the HTTP response was successful (2xx status codes)
                if (response.isSuccessful)
                {
                    // Process successful response
                    response.body()?.let { sourcesResponse ->
                        // Check if sources were actually returned
                        if (sourcesResponse.sources.isNotEmpty()) {
                            // Update UI with the received sources
                            updateSourcesList(sourcesResponse.sources)
                        }
                        else
                        {
                            // Handle case where API returns empty sources list
                            showError("No sources found")
                        }
                    } ?: run {
                        // Handle case where response body is null
                        showError("Empty response received")
                    }
                }
                else
                {
                    // Handle HTTP error responses with specific error messages
                    when (response.code()) {
                        401 -> showError("API Key error - Please check your News API key")
                        429 -> showError("Rate limit exceeded - Please try again later")
                        else -> showError("Failed to load sources: ${response.code()} - ${response.message()}")
                    }
                }
            }
            catch (e: Exception)
            {
                // Handle any exceptions (network errors, parsing errors, etc.)
                showError("Error loading sources: ${e.message}")
            }
            finally
            {
                // Always hide loading indicator and show content, regardless of success/failure
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewSources.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Updates the sources list and refreshes the RecyclerView display.
     *
     * This method clears the current sources, adds the new sources data,
     * and notifies the adapter to refresh the entire list display.
     *
     * @param sources List of NewsSource objects to display in the RecyclerView
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun updateSourcesList(sources: List<NewsSource>)
    {
        // Clear existing sources and add new ones
        currentSources.clear()
        currentSources.addAll(sources)

        // Notify adapter to refresh the entire list
        // This triggers a complete redraw of all visible items
        sourceAdapter.notifyDataSetChanged()
    }

    /**
     * Displays error messages to the user via Toast notifications.
     *
     * Provides user-friendly feedback when errors occur during data loading
     * or other operations. Uses short duration to avoid blocking the UI.
     *
     * @param message The error message to display to the user
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}