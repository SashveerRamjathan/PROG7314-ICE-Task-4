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
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        newsRepository = NewsRepository()

        setupRecyclerView()
        setupCollapsibleSortBy()
        setupCollapsibleDateFilter()
        setupBackButton()
        setupSearchFunctionality()
        setupSortBySelection()
        setupDateFilterSelection()

        // Set initial collapsed states
        setInitialStates()
    }

    private fun setInitialStates()
    {
        // Set sort by content to collapsed initially
        binding.sortByContent.visibility = View.GONE
        binding.iconExpandSortBy.rotation = 180f
        isSortByExpanded = false

        // Set date filter content to collapsed initially
        binding.dateFilterContent.visibility = View.GONE
        binding.iconExpandDateFilter.rotation = 180f
        isDateFilterExpanded = false

        // Show empty state initially
        binding.layoutEmptyState.visibility = View.VISIBLE
        binding.recyclerViewSearchResults.visibility = View.GONE
    }

    private fun setupRecyclerView()
    {
        articleAdapter = ArticleAdapter(currentArticles)
        binding.recyclerViewSearchResults.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    private fun setupCollapsibleSortBy()
    {
        binding.sortByHeader.setOnClickListener {

            isSortByExpanded = !isSortByExpanded

            if (isSortByExpanded)
            {
                binding.sortByContent.visibility = View.VISIBLE
                binding.iconExpandSortBy.animate().rotation(0f).duration = 200
            }
            else
            {
                binding.sortByContent.visibility = View.GONE
                binding.iconExpandSortBy.animate().rotation(180f).duration = 200
            }
        }
    }

    private fun setupCollapsibleDateFilter()
    {
        binding.dateFilterHeader.setOnClickListener {

            isDateFilterExpanded = !isDateFilterExpanded

            if (isDateFilterExpanded)
            {
                binding.dateFilterContent.visibility = View.VISIBLE
                binding.iconExpandDateFilter.animate().rotation(0f).duration = 200
            }
            else
            {
                binding.dateFilterContent.visibility = View.GONE
                binding.iconExpandDateFilter.animate().rotation(180f).duration = 200
            }
        }
    }

    private fun setupBackButton()
    {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupSearchFunctionality()
    {
        // Search button click
        binding.btnSearch.setOnClickListener {
            performSearch()
        }

        // Search on enter key press
        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            }
            else
            {
                false
            }
        }
    }

    private fun setupSortBySelection()
    {
        binding.chipGroupSortBy.setOnCheckedStateChangeListener { _, checkedIds ->

            if (checkedIds.isNotEmpty())
            {
                val selectedSortBy = when (checkedIds[0]) {
                    R.id.chipPublishedAt -> SearchConstants.SortBy.PUBLISHED_AT
                    R.id.chipRelevancy -> SearchConstants.SortBy.RELEVANCY
                    R.id.chipPopularity -> SearchConstants.SortBy.POPULARITY
                    else -> SearchConstants.SortBy.PUBLISHED_AT
                }

                currentSortBy = selectedSortBy
                updateSortByHeaderText(selectedSortBy)

                // Collapse sort by section after selection
                isSortByExpanded = false
                binding.sortByContent.visibility = View.GONE
                binding.iconExpandSortBy.animate().rotation(180f).duration = 200

                // Re-search if there's already a query
                if (binding.editTextSearch.text?.isNotBlank() == true) {
                    performSearch()
                }
            }
        }
    }

    private fun setupDateFilterSelection()
    {
        binding.chipGroupDateFilter.setOnCheckedStateChangeListener { _, checkedIds ->

            if (checkedIds.isNotEmpty())
            {
                when (checkedIds[0])
                {
                    R.id.chipAllTime -> {
                        currentFromDate = null
                        currentToDate = null
                        updateDateFilterHeaderText("All Time")
                    }
                    R.id.chipToday -> {
                        currentFromDate = DateUtils.getTodayDate()
                        currentToDate = DateUtils.getTodayDate()
                        updateDateFilterHeaderText("Today")
                    }
                    R.id.chipWeek -> {
                        currentFromDate = DateUtils.getWeekAgo()
                        currentToDate = DateUtils.getTodayDate()
                        updateDateFilterHeaderText("This Week")
                    }
                    R.id.chipMonth -> {
                        currentFromDate = DateUtils.getMonthAgo()
                        currentToDate = DateUtils.getTodayDate()
                        updateDateFilterHeaderText("This Month")
                    }
                }

                // Collapse date filter section after selection
                isDateFilterExpanded = false
                binding.dateFilterContent.visibility = View.GONE
                binding.iconExpandDateFilter.animate().rotation(180f).duration = 200

                // Re-search if there's already a query
                if (binding.editTextSearch.text?.isNotBlank() == true) {
                    performSearch()
                }
            }
        }
    }

    private fun performSearch()
    {
        val query = binding.editTextSearch.text?.toString()?.trim()

        if (query.isNullOrBlank()) {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            return
        }

        searchArticles(query)
    }

    private fun searchArticles(query: String)
    {
        // Show progress bar and hide other views
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewSearchResults.visibility = View.GONE
        binding.layoutEmptyState.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = newsRepository.searchEverything(
                    query = query,
                    fromDate = currentFromDate,
                    toDate = currentToDate,
                    sortBy = currentSortBy
                )

                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        if (newsResponse.articles.isNotEmpty()) {
                            updateSearchResults(newsResponse.articles)
                        } else {
                            showNoResults("No articles found for \"$query\"")
                        }
                    } ?: run {
                        showError("Empty response received")
                    }
                }
                else
                {
                    when (response.code()) {
                        401 -> showError("API Key error - Please check your News API key")
                        429 -> showError("Rate limit exceeded - Please try again later")
                        else -> showError("Failed to search articles: ${response.code()} - ${response.message()}")
                    }
                }
            }
            catch (e: Exception)
            {
                showError("Error searching articles: ${e.message}")
            }
            finally
            {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateSearchResults(articles: List<Article>)
    {
        currentArticles.clear()
        currentArticles.addAll(articles)
        articleAdapter.notifyDataSetChanged()

        binding.layoutEmptyState.visibility = View.GONE
        binding.recyclerViewSearchResults.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun showNoResults(message: String)
    {
        binding.tvEmptyStateTitle.text = "No Results Found"
        binding.tvEmptyStateMessage.text = message
        binding.layoutEmptyState.visibility = View.VISIBLE
        binding.recyclerViewSearchResults.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun showError(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        // Keep current state if there are existing results, otherwise show empty state

        if (currentArticles.isEmpty())
        {
            binding.tvEmptyStateTitle.text = "Search for Articles"
            binding.tvEmptyStateMessage.text = "Enter keywords to find relevant news articles"
            binding.layoutEmptyState.visibility = View.VISIBLE
            binding.recyclerViewSearchResults.visibility = View.GONE
        }
        else
        {
            binding.recyclerViewSearchResults.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateSortByHeaderText(sortBy: String)
    {
        val sortByDisplayName = when (sortBy) {
            SearchConstants.SortBy.PUBLISHED_AT -> "Newest First"
            SearchConstants.SortBy.RELEVANCY -> "Relevancy"
            SearchConstants.SortBy.POPULARITY -> "Popularity"
            else -> "Newest First"
        }
        binding.sortByHeaderText.text = "Sort By - $sortByDisplayName"
    }

    @SuppressLint("SetTextI18n")
    private fun updateDateFilterHeaderText(dateFilter: String)
    {
        binding.dateFilterHeaderText.text = "Date Filter - $dateFilter"
    }
}