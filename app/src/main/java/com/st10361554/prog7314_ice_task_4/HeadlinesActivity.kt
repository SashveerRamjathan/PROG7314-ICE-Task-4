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
import com.st10361554.prog7314_ice_task_4.adapters.ArticleAdapter
import com.st10361554.prog7314_ice_task_4.databinding.ActivityHeadlinesBinding
import com.st10361554.prog7314_ice_task_4.models.Article
import com.st10361554.prog7314_ice_task_4.news_utils.NewsConstants
import com.st10361554.prog7314_ice_task_4.repository.NewsRepository
import kotlinx.coroutines.launch

class HeadlinesActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityHeadlinesBinding
    private var isCategoryExpanded = false
    private lateinit var newsRepository: NewsRepository
    private lateinit var articleAdapter: ArticleAdapter
    private var currentArticles = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display for modern Android UI
        enableEdgeToEdge()

        // Initialize ViewBinding for type-safe view access
        binding = ActivityHeadlinesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle system window insets for edge-to-edge display
        // This ensures proper padding around system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the repository for News API data operations
        newsRepository = NewsRepository()

        // Set up all UI components and functionality
        setupRecyclerView()
        setupCollapsibleCategory()
        setupBackButton()
        setupCategorySelection()

        // Configure the initial UI state with collapsed category selector
        setInitialCategoryState()

        // Load default headlines from General category on startup
        // This provides immediate content for users when the activity opens
        loadHeadlines(NewsConstants.Categories.GENERAL)
    }

    /**
     * Sets the initial state of the category selection interface.
     *
     * Configures the category selector to be collapsed by default, providing
     * a clean initial interface. The arrow icon is rotated to indicate the
     * collapsed state, and the content area is hidden until user interaction.
     */
    private fun setInitialCategoryState()
    {
        // Hide category selection options initially
        binding.categoryContent.visibility = View.GONE

        // Rotate expand icon to point down (indicating collapsed state)
        binding.iconExpand.rotation = 180f

        // Set collapsed state flag
        isCategoryExpanded = false
    }

    /**
     * Configures the RecyclerView for displaying headline articles.
     *
     * Sets up the adapter with the current articles list and applies a
     * LinearLayoutManager for vertical scrolling list display. Each headline
     * will be displayed as a separate list item showing title, description,
     * source information, and publication time.
     */
    private fun setupRecyclerView()
    {
        // Create adapter with the current articles list
        articleAdapter = ArticleAdapter(currentArticles)

        // Configure RecyclerView with adapter and layout manager
        binding.recyclerViewHeadlines.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(this@HeadlinesActivity)
        }
    }

    /**
     * Sets up the collapsible category selection interface with smooth animations.
     *
     * When the category header is clicked, the content area expands or collapses
     * with a smooth animation. The arrow icon rotates to indicate the current state:
     * - 0 degrees (pointing up) when expanded, showing category options
     * - 180 degrees (pointing down) when collapsed, hiding category options
     *
     * This design provides a clean interface while keeping category selection
     * easily accessible when needed.
     */
    private fun setupCollapsibleCategory()
    {
        binding.categoryHeader.setOnClickListener {
            // Toggle the expanded state
            isCategoryExpanded = !isCategoryExpanded

            if (isCategoryExpanded) {
                // Expand: show category options and rotate arrow up
                binding.categoryContent.visibility = View.VISIBLE
                binding.iconExpand.animate().rotation(0f).duration = 200
            } else {
                // Collapse: hide category options and rotate arrow down
                binding.categoryContent.visibility = View.GONE
                binding.iconExpand.animate().rotation(180f).duration = 200
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
     * Sets up the category selection functionality using radio buttons.
     *
     * When a category is selected:
     * 1. Determines which news category was chosen
     * 2. Updates the header text to reflect the selection
     * 3. Automatically collapses the category selection interface
     * 4. Loads fresh headlines for the selected category
     *
     * Available categories:
     * - Business: Corporate news, market updates, financial reports
     * - Entertainment: Movies, TV, music, celebrity news
     * - General: Politics, current events, miscellaneous news (default)
     * - Health: Medical research, healthcare, wellness
     * - Science: Research, discoveries, environmental science
     * - Sports: Game results, player news, tournaments
     * - Technology: Tech companies, product launches, innovation
     */
    private fun setupCategorySelection()
    {
        binding.radioGroupCategory.setOnCheckedChangeListener { _, checkedId ->
            // Determine which category was selected based on radio button ID
            val selectedCategory = when (checkedId)
            {
                R.id.radioBusiness -> NewsConstants.Categories.BUSINESS
                R.id.radioEntertainment -> NewsConstants.Categories.ENTERTAINMENT
                R.id.radioGeneral -> NewsConstants.Categories.GENERAL
                R.id.radioHealth -> NewsConstants.Categories.HEALTH
                R.id.radioScience -> NewsConstants.Categories.SCIENCE
                R.id.radioSports -> NewsConstants.Categories.SPORTS
                R.id.radioTechnology -> NewsConstants.Categories.TECHNOLOGY
                else -> NewsConstants.Categories.GENERAL // Fallback to general
            }

            // Update header text to show the selected category
            updateCategoryHeaderText(selectedCategory)

            // Auto-collapse the category section after selection for cleaner UI
            isCategoryExpanded = false
            binding.categoryContent.visibility = View.GONE
            binding.iconExpand.animate().rotation(180f).duration = 200

            // Load fresh headlines for the newly selected category
            loadHeadlines(selectedCategory)
        }
    }

    /**
     * Loads top headlines for the specified news category from the News API.
     *
     * This method handles the complete flow of fetching category-specific headlines:
     * 1. Shows loading indicator while hiding content
     * 2. Makes async API call using the repository
     * 3. Processes the response and updates UI with new headlines
     * 4. Handles various error scenarios with appropriate user feedback
     * 5. Ensures loading indicator is hidden regardless of success/failure
     *
     * The method uses the News API's /top-headlines endpoint with category filtering
     * to retrieve the most important and recent news stories from the specified category.
     *
     * @param category The news category constant (from NewsConstants.Categories)
     *                 to filter headlines by (e.g., "business", "technology", "sports")
     *
     * Error handling covers:
     * - Network connectivity issues
     * - API response errors (4xx, 5xx status codes)
     * - Empty responses from the API
     * - General exceptions during processing
     */
    private fun loadHeadlines(category: String)
    {
        // Show loading state: display progress bar, hide content
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewHeadlines.visibility = View.GONE

        // Launch coroutine in lifecycle scope for automatic cleanup
        lifecycleScope.launch {
            try {
                // Make API call to fetch top headlines for the specified category
                val response = newsRepository.getTopHeadlinesByCategory(category)

                // Check if the HTTP response was successful (2xx status codes)
                if (response.isSuccessful)
                {
                    // Process successful response
                    response.body()?.let { newsResponse ->
                        // Update UI with the received headlines
                        updateHeadlinesList(newsResponse.articles)
                    } ?: run {
                        // Handle case where response body is null
                        // Format category name for user-friendly display
                        showError("No headlines found for ${category.replaceFirstChar { it.uppercase() }}")
                    }
                } else {
                    // Handle HTTP error responses
                    showError("Failed to load headlines: ${response.message()}")
                }
            }
            catch (e: Exception)
            {
                // Handle any exceptions (network errors, parsing errors, etc.)
                showError("Error loading headlines: ${e.message}")
            }
            finally
            {
                // Always hide loading indicator and show content, regardless of success/failure
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewHeadlines.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Updates the headlines list and refreshes the RecyclerView display.
     *
     * This method clears the current headlines, adds the new articles data,
     * and notifies the adapter to refresh the entire list display. This ensures
     * that when users switch categories, they see fresh content immediately.
     *
     * @param articles List of Article objects containing the latest headlines
     *                 for the selected category
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun updateHeadlinesList(articles: List<Article>)
    {
        // Clear existing headlines and add new ones
        currentArticles.clear()
        currentArticles.addAll(articles)

        // Notify adapter to refresh the entire list
        // This triggers a complete redraw of all visible items
        articleAdapter.notifyDataSetChanged()
    }

    /**
     * Displays error messages to the user via Toast notifications.
     *
     * Provides user-friendly feedback when errors occur during headline loading
     * or other operations. Uses short duration to avoid blocking the UI while
     * still ensuring users are aware of any issues.
     *
     * @param message The error message to display to the user
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Updates the category header text to reflect the currently selected category.
     *
     * Converts the internal category constant to a user-friendly display name
     * and updates the header text to show the current selection. This provides
     * visual feedback about which category's headlines are currently being displayed.
     *
     * @param category The current category constant from NewsConstants.Categories
     */
    @SuppressLint("SetTextI18n")
    private fun updateCategoryHeaderText(category: String) {
        // Convert category constant to user-friendly display name
        val categoryDisplayName = when (category) {
            NewsConstants.Categories.BUSINESS -> "Business"
            NewsConstants.Categories.ENTERTAINMENT -> "Entertainment"
            NewsConstants.Categories.GENERAL -> "General"
            NewsConstants.Categories.HEALTH -> "Health"
            NewsConstants.Categories.SCIENCE -> "Science"
            NewsConstants.Categories.SPORTS -> "Sports"
            NewsConstants.Categories.TECHNOLOGY -> "Technology"
            else -> "General" // Fallback
        }

        // Update header text to show current category selection
        binding.categoryHeaderText.text = "Select Category - $categoryDisplayName"
    }
}