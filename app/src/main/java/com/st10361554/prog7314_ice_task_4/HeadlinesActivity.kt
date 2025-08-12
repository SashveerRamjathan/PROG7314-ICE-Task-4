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
        enableEdgeToEdge()

        binding = ActivityHeadlinesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        newsRepository = NewsRepository()

        setupRecyclerView()
        setupCollapsibleCategory()
        setupBackButton()
        setupCategorySelection()

        // Set initial collapsed state
        setInitialCategoryState()

        // Load default category (General) on startup
        loadHeadlines(NewsConstants.Categories.GENERAL)
    }

    private fun setInitialCategoryState()
    {
        // Set category content to collapsed initially
        binding.categoryContent.visibility = View.GONE
        binding.iconExpand.rotation = 180f // Rotate icon to indicate collapsed state
        isCategoryExpanded = false
    }

    private fun setupRecyclerView()
    {
        articleAdapter = ArticleAdapter(currentArticles)

        binding.recyclerViewHeadlines.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(this@HeadlinesActivity)
        }
    }

    private fun setupCollapsibleCategory()
    {
        binding.categoryHeader.setOnClickListener {
            isCategoryExpanded = !isCategoryExpanded

            if (isCategoryExpanded) {
                binding.categoryContent.visibility = View.VISIBLE
                binding.iconExpand.animate().rotation(0f).duration = 200
            } else {
                binding.categoryContent.visibility = View.GONE
                binding.iconExpand.animate().rotation(180f).duration = 200
            }
        }
    }

    private fun setupBackButton()
    {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupCategorySelection()
    {
        binding.radioGroupCategory.setOnCheckedChangeListener { _, checkedId ->

            val selectedCategory = when (checkedId)
            {
                R.id.radioBusiness -> NewsConstants.Categories.BUSINESS
                R.id.radioEntertainment -> NewsConstants.Categories.ENTERTAINMENT
                R.id.radioGeneral -> NewsConstants.Categories.GENERAL
                R.id.radioHealth -> NewsConstants.Categories.HEALTH
                R.id.radioScience -> NewsConstants.Categories.SCIENCE
                R.id.radioSports -> NewsConstants.Categories.SPORTS
                R.id.radioTechnology -> NewsConstants.Categories.TECHNOLOGY
                else -> NewsConstants.Categories.GENERAL
            }

            // Update header text to show selected category
            updateCategoryHeaderText(selectedCategory)

            // Collapse category section after selection
            isCategoryExpanded = false
            binding.categoryContent.visibility = View.GONE
            binding.iconExpand.animate().rotation(180f).duration = 200

            // Load headlines for the selected category
            loadHeadlines(selectedCategory)
        }
    }

    private fun loadHeadlines(category: String)
    {
        // Show progress bar
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewHeadlines.visibility = View.GONE

        // Launch coroutine to call the repository method
        lifecycleScope.launch {
            try {
                val response = newsRepository.getTopHeadlinesByCategory(category)

                if (response.isSuccessful)
                {
                    response.body()?.let { newsResponse ->
                        // Update UI with the articles
                        updateHeadlinesList(newsResponse.articles)
                    } ?: run {
                        // Handle empty response
                        showError("No headlines found for ${category.replaceFirstChar { it.uppercase() }}")
                    }
                } else {
                    // Handle API error
                    showError("Failed to load headlines: ${response.message()}")
                }
            }
            catch (e: Exception)
            {
                // Handle network or other exceptions
                showError("Error loading headlines: ${e.message}")
            }
            finally
            {
                // Hide progress bar
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewHeadlines.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateHeadlinesList(articles: List<Article>)
    {
        currentArticles.clear()
        currentArticles.addAll(articles)
        articleAdapter.notifyDataSetChanged()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateCategoryHeaderText(category: String) {
        val categoryDisplayName = when (category) {
            NewsConstants.Categories.BUSINESS -> "Business"
            NewsConstants.Categories.ENTERTAINMENT -> "Entertainment"
            NewsConstants.Categories.GENERAL -> "General"
            NewsConstants.Categories.HEALTH -> "Health"
            NewsConstants.Categories.SCIENCE -> "Science"
            NewsConstants.Categories.SPORTS -> "Sports"
            NewsConstants.Categories.TECHNOLOGY -> "Technology"
            else -> "General"
        }

        binding.categoryHeaderText.text =
            "Select Category - $categoryDisplayName"
    }
}