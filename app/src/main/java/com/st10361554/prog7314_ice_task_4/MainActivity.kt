package com.st10361554.prog7314_ice_task_4

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.st10361554.prog7314_ice_task_4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewBinding for type-safe view access
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            // Get the insets for system bars (status bar, navigation bar)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply padding to prevent content from being hidden behind system bars
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            // Return the insets to allow other views to handle them if needed
            insets
        }

        // Set up navigation functionality for all main menu buttons
        setupClickListeners()
    }

    /**
     * Configures click listeners for all navigation buttons in the main interface.
     *
     * Each button leads to a different core functionality of the news application:
     *
     * 1. Search Button - Opens SearchActivity for advanced article searching
     *    Features: keyword search, date filtering, result sorting, comprehensive filtering options
     *
     * 2. Headlines Button - Opens HeadlinesActivity for browsing top headlines
     *    Features: category-based headlines, real-time top stories, organized by news topics
     *
     * 3. Sources Button - Opens SourcesActivity for exploring news sources
     *    Features: complete source listings, source metadata, publisher information
     *
     * All navigation uses explicit intents for type-safe activity transitions
     * and follows Android's standard navigation patterns for consistent user experience.
     */
    private fun setupClickListeners()
    {
        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnHeadlines.setOnClickListener {
            val intent = Intent(this, HeadlinesActivity::class.java)
            startActivity(intent)
        }

        binding.btnSources.setOnClickListener {
            val intent = Intent(this, SourcesActivity::class.java)
            startActivity(intent)
        }
    }
}