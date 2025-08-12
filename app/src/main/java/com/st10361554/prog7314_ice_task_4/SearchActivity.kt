package com.st10361554.prog7314_ice_task_4

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.st10361554.prog7314_ice_task_4.adapters.ArticleAdapter
import com.st10361554.prog7314_ice_task_4.databinding.ActivitySearchBinding
import com.st10361554.prog7314_ice_task_4.models.Article
import com.st10361554.prog7314_ice_task_4.news_utils.DateUtils
import com.st10361554.prog7314_ice_task_4.news_utils.SearchConstants
import com.st10361554.prog7314_ice_task_4.repository.NewsRepository
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var isSortByExpanded = false
    private var isDateFilterExpanded = false
    private lateinit var newsRepository: NewsRepository
    private lateinit var articleAdapter: ArticleAdapter
    private var currentArticles = mutableListOf<Article>()

    // Current filter states
    private var currentSortBy = SearchConstants.SortBy.PUBLISHED_AT
    private var currentFromDate: String? = null
    private var currentToDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display for modern Android UI
        enableEdgeToEdge()

        // Initialize ViewBinding for type-safe view access
        binding = ActivitySearchBinding.inflate(layoutInflater)
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

        // Set up all UI components and functionality
        setupRecyclerView()
        setupCollapsibleSortBy()
        setupCollapsibleDateFilter()
        setupBackButton()
        setupSearchFunctionality()
        setupSortBySelection()
        setupDateFilterSelection()

        // Configure the initial UI state (collapsed filters, empty state)
        setInitialStates()
    }

    /**
     * Sets the initial state of the UI components when the activity is created.
     *
     * Configures the filter sections to be collapsed by default and shows
     * the empty state with instructions for the user. This provides a clean
     * initial experience and guides users on how to use the search functionality.
     */
    private fun setInitialStates()
    {
        // Collapse sort by section initially
        binding.sortByContent.visibility = View.GONE
        binding.iconExpandSortBy.rotation = 180f // Arrow pointing down
        isSortByExpanded = false

        // Collapse date filter section initially
        binding.dateFilterContent.visibility = View.GONE
        binding.iconExpandDateFilter.rotation = 180f // Arrow pointing down
        isDateFilterExpanded = false

        // Show empty state with search instructions
        binding.layoutEmptyState.visibility = View.VISIBLE
        binding.recyclerViewSearchResults.visibility = View.GONE
    }

    /**
     * Configures the RecyclerView for displaying search result articles.
     *
     * Sets up the adapter with the current articles list and applies a
     * LinearLayoutManager for vertical scrolling list display.
     * Each article will be displayed as a separate list item with
     * title, description, source, and publication date information.
     */
    private fun setupRecyclerView()
    {
        // Create adapter with the current articles list
        articleAdapter = ArticleAdapter(currentArticles)

        // Configure RecyclerView with adapter and layout manager
        binding.recyclerViewSearchResults.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    /**
     * Sets up the collapsible sort by filter section with smooth animations.
     *
     * When the sort by header is clicked, the content area expands or collapses
     * with a smooth animation. The arrow icon rotates to indicate the current state:
     * - 0 degrees (pointing up) when expanded
     * - 180 degrees (pointing down) when collapsed
     */
    private fun setupCollapsibleSortBy()
    {
        binding.sortByHeader.setOnClickListener {
            // Toggle the expanded state
            isSortByExpanded = !isSortByExpanded

            if (isSortByExpanded)
            {
                // Expand: show content and rotate arrow up
                binding.sortByContent.visibility = View.VISIBLE
                binding.iconExpandSortBy.animate().rotation(0f).duration = 200
            }
            else
            {
                // Collapse: hide content and rotate arrow down
                binding.sortByContent.visibility = View.GONE
                binding.iconExpandSortBy.animate().rotation(180f).duration = 200
            }
        }
    }

    /**
     * Sets up the collapsible date filter section with smooth animations.
     *
     * When the date filter header is clicked, the content area expands or collapses
     * with a smooth animation. The arrow icon rotates to indicate the current state:
     * - 0 degrees (pointing up) when expanded
     * - 180 degrees (pointing down) when collapsed
     */
    private fun setupCollapsibleDateFilter()
    {
        binding.dateFilterHeader.setOnClickListener {
            // Toggle the expanded state
            isDateFilterExpanded = !isDateFilterExpanded

            if (isDateFilterExpanded)
            {
                // Expand: show content and rotate arrow up
                binding.dateFilterContent.visibility = View.VISIBLE
                binding.iconExpandDateFilter.animate().rotation(0f).duration = 200
            }
            else
            {
                // Collapse: hide content and rotate arrow down
                binding.dateFilterContent.visibility = View.GONE
                binding.iconExpandDateFilter.animate().rotation(180f).duration = 200
            }
        }
    }

    private fun setupBackButton()
    {
        binding.btnBack.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }
    }

    /**
     * Configures search functionality including button clicks and keyboard actions.
     *
     * Sets up two ways to trigger a search:
     * 1. Clicking the search button
     * 2. Pressing the search/enter key on the keyboard while in the search field
     *
     * Both methods validate the input and call performSearch() if valid.
     */
    private fun setupSearchFunctionality()
    {
        // Handle search button click
        binding.btnSearch.setOnClickListener {
            performSearch()
        }

        // Handle search action from keyboard (enter/search key)
        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true // Consume the action
            }
            else
            {
                false // Don't consume other actions
            }
        }
    }

    /**
     * Sets up the sort by selection functionality using chip groups.
     *
     * When a sort option is selected:
     * 1. Updates the current sorting method
     * 2. Updates the header text to reflect the selection
     * 3. Automatically collapses the sort section
     * 4. Re-runs the search if there's an active query
     *
     * Available sort options:
     * - Published At (newest first) - default
     * - Relevancy (most relevant to search query)
     * - Popularity (most engaging articles)
     */
    private fun setupSortBySelection()
    {
        binding.chipGroupSortBy.setOnCheckedStateChangeListener { _, checkedIds ->
            // Ensure at least one chip is selected
            if (checkedIds.isNotEmpty())
            {
                // Determine which sort method was selected
                val selectedSortBy = when (checkedIds[0]) {
                    R.id.chipPublishedAt -> SearchConstants.SortBy.PUBLISHED_AT
                    R.id.chipRelevancy -> SearchConstants.SortBy.RELEVANCY
                    R.id.chipPopularity -> SearchConstants.SortBy.POPULARITY
                    else -> SearchConstants.SortBy.PUBLISHED_AT // Fallback
                }

                // Update current sort method and UI
                currentSortBy = selectedSortBy
                updateSortByHeaderText(selectedSortBy)

                // Auto-collapse the sort section after selection
                isSortByExpanded = false
                binding.sortByContent.visibility = View.GONE
                binding.iconExpandSortBy.animate().rotation(180f).duration = 200

                // Re-search with new sort order if there's an active query
                if (binding.editTextSearch.text?.isNotBlank() == true) {
                    performSearch()
                }
            }
        }
    }

    /**
     * Sets up the date filter selection functionality using chip groups.
     *
     * When a date filter is selected:
     * 1. Updates the current date range filters
     * 2. Updates the header text to reflect the selection
     * 3. Automatically collapses the date filter section
     * 4. Re-runs the search if there's an active query
     *
     * Available date filter options:
     * - All Time (no date restrictions)
     * - Today (articles from today only)
     * - This Week (articles from the past 7 days)
     * - This Month (articles from the past 30 days)
     */
    private fun setupDateFilterSelection()
    {
        binding.chipGroupDateFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            // Ensure at least one chip is selected
            if (checkedIds.isNotEmpty())
            {
                // Determine which date filter was selected and set appropriate date range
                when (checkedIds[0])
                {
                    R.id.chipAllTime -> {
                        // No date restrictions
                        currentFromDate = null
                        currentToDate = null
                        updateDateFilterHeaderText("All Time")
                    }
                    R.id.chipToday -> {
                        // Articles from today only
                        currentFromDate = DateUtils.getTodayDate()
                        currentToDate = DateUtils.getTodayDate()
                        updateDateFilterHeaderText("Today")
                    }
                    R.id.chipWeek -> {
                        // Articles from the past week
                        currentFromDate = DateUtils.getWeekAgo()
                        currentToDate = DateUtils.getTodayDate()
                        updateDateFilterHeaderText("This Week")
                    }
                    R.id.chipMonth -> {
                        // Articles from the past month
                        currentFromDate = DateUtils.getMonthAgo()
                        currentToDate = DateUtils.getTodayDate()
                        updateDateFilterHeaderText("This Month")
                    }
                }

                // Auto-collapse the date filter section after selection
                isDateFilterExpanded = false
                binding.dateFilterContent.visibility = View.GONE
                binding.iconExpandDateFilter.animate().rotation(180f).duration = 200

                // Re-search with new date filter if there's an active query
                if (binding.editTextSearch.text?.isNotBlank() == true) {
                    performSearch()
                }
            }
        }
    }

    /**
     * Validates the search input and initiates the search process.
     *
     * Checks if the user has entered a valid search query and shows
     * an error message if the input is empty or contains only whitespace.
     * If the input is valid, calls searchArticles() to perform the API call.
     */
    private fun performSearch()
    {
        // Get and validate the search query
        val query = binding.editTextSearch.text?.toString()?.trim()

        if (query.isNullOrBlank()) {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            return
        }

        // Proceed with the search
        searchArticles(query)
    }

    /**
     * Performs the actual search API call with current filter settings.
     *
     * This method handles the complete search flow:
     * 1. Shows loading indicator and hides other UI elements
     * 2. Makes async API call using the repository with current filters
     * 3. Processes the response and updates UI accordingly
     * 4. Handles various error scenarios with appropriate user feedback
     * 5. Hides loading indicator regardless of success/failure
     *
     * The search uses all current filter settings including sort method,
     * date range, and the provided query string.
     *
     * @param query The search query string entered by the user
     */
    private fun searchArticles(query: String)
    {
        // Show loading state: hide content and empty state, show progress bar
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewSearchResults.visibility = View.GONE
        binding.layoutEmptyState.visibility = View.GONE

        // Launch coroutine in lifecycle scope for automatic cleanup
        lifecycleScope.launch {
            try {
                // Make API call with current query and filter settings
                val response = newsRepository.searchEverything(
                    query = query,
                    fromDate = currentFromDate,
                    toDate = currentToDate,
                    sortBy = currentSortBy
                )

                // Check if the HTTP response was successful (2xx status codes)
                if (response.isSuccessful) {
                    // Process successful response
                    response.body()?.let { newsResponse ->
                        // Check if articles were actually returned
                        if (newsResponse.articles.isNotEmpty()) {
                            // Update UI with the search results
                            updateSearchResults(newsResponse.articles)
                        } else {
                            // Show no results found message with the search query
                            showNoResults("No articles found for \"$query\"")
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
                        else -> showError("Failed to search articles: ${response.code()} - ${response.message()}")
                    }
                }
            }
            catch (e: Exception)
            {
                // Handle any exceptions (network errors, parsing errors, etc.)
                showError("Error searching articles: ${e.message}")
            }
            finally
            {
                // Always hide loading indicator, regardless of success/failure
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    /**
     * Updates the search results list and refreshes the RecyclerView display.
     *
     * This method clears the current articles, adds the new search results,
     * and notifies the adapter to refresh the entire list display.
     * Also handles the UI state transition from empty/loading to showing results.
     *
     * @param articles List of Article objects from the search API response
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun updateSearchResults(articles: List<Article>)
    {
        // Clear existing articles and add new search results
        currentArticles.clear()
        currentArticles.addAll(articles)

        // Notify adapter to refresh the entire list
        articleAdapter.notifyDataSetChanged()

        // Show results and hide empty state
        binding.layoutEmptyState.visibility = View.GONE
        binding.recyclerViewSearchResults.visibility = View.VISIBLE
    }

    /**
     * Displays a "no results found" state with a custom message.
     *
     * Shows the empty state layout with specific messaging indicating
     * that no articles were found for the search query. This provides
     * clear feedback to users when their search doesn't return any results.
     *
     * @param message The specific no results message to display to the user
     */
    @SuppressLint("SetTextI18n")
    private fun showNoResults(message: String)
    {
        // Update empty state with no results messaging
        binding.tvEmptyStateTitle.text = "No Results Found"
        binding.tvEmptyStateMessage.text = message

        // Show empty state and hide results
        binding.layoutEmptyState.visibility = View.VISIBLE
        binding.recyclerViewSearchResults.visibility = View.GONE
    }

    /**
     * Displays error messages and manages UI state during error conditions.
     *
     * Shows error messages via Toast and handles the UI state appropriately:
     * - If there are existing search results, keeps them visible
     * - If no results exist, shows the default empty state with search instructions
     *
     * This approach provides good user experience by not clearing successful
     * results when a subsequent search fails.
     *
     * @param message The error message to display to the user
     */
    @SuppressLint("SetTextI18n")
    private fun showError(message: String)
    {
        // Show error message to user
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        // Handle UI state based on whether there are existing results
        if (currentArticles.isEmpty())
        {
            // No existing results: show default empty state with instructions
            binding.tvEmptyStateTitle.text = "Search for Articles"
            binding.tvEmptyStateMessage.text = "Enter keywords to find relevant news articles"
            binding.layoutEmptyState.visibility = View.VISIBLE
            binding.recyclerViewSearchResults.visibility = View.GONE
        }
        else
        {
            // Keep existing results visible after error
            binding.recyclerViewSearchResults.visibility = View.VISIBLE
        }
    }

    /**
     * Updates the sort by header text to reflect the currently selected sorting method.
     *
     * Converts the internal sort constant to a user-friendly display name
     * and updates the header text to show the current selection.
     *
     * @param sortBy The current sort method constant from SearchConstants.SortBy
     */
    @SuppressLint("SetTextI18n")
    private fun updateSortByHeaderText(sortBy: String)
    {
        // Convert sort constant to user-friendly display name
        val sortByDisplayName = when (sortBy) {
            SearchConstants.SortBy.PUBLISHED_AT -> "Newest First"
            SearchConstants.SortBy.RELEVANCY -> "Relevancy"
            SearchConstants.SortBy.POPULARITY -> "Popularity"
            else -> "Newest First" // Fallback
        }

        // Update header text to show current selection
        binding.sortByHeaderText.text = "Sort By - $sortByDisplayName"
    }

    /**
     * Updates the date filter header text to reflect the currently selected date range.
     *
     * Updates the header text to show which date filter option is currently active,
     * providing visual feedback about the current filtering state.
     *
     * @param dateFilter The user-friendly name of the current date filter (e.g., "Today", "This Week")
     */
    @SuppressLint("SetTextI18n")
    private fun updateDateFilterHeaderText(dateFilter: String)
    {
        // Update header text to show current date filter selection
        binding.dateFilterHeaderText.text = "Date Filter - $dateFilter"
    }
}