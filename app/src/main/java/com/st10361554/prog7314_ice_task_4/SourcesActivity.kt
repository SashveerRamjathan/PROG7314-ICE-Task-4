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
        enableEdgeToEdge()

        binding = ActivitySourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        newsRepository = NewsRepository()

        setupRecyclerView()
        setupBackButton()

        // Load all sources on startup
        loadSources()
    }

    private fun setupRecyclerView()
    {
        sourceAdapter = SourceAdapter(currentSources)
        binding.recyclerViewSources.apply {
            adapter = sourceAdapter
            layoutManager = LinearLayoutManager(this@SourcesActivity)
        }
    }

    private fun setupBackButton()
    {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadSources()
    {
        // Show progress bar
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewSources.visibility = View.GONE

        // Launch coroutine to call the repository method
        lifecycleScope.launch {
            try
            {
                val response = newsRepository.getAllSources()

                if (response.isSuccessful)
                {
                    response.body()?.let { sourcesResponse ->
                        if (sourcesResponse.sources.isNotEmpty()) {
                            updateSourcesList(sourcesResponse.sources)
                        }
                        else
                        {
                            showError("No sources found")
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
                        else -> showError("Failed to load sources: ${response.code()} - ${response.message()}")
                    }
                }
            }
            catch (e: Exception)
            {
                showError("Error loading sources: ${e.message}")
            }
            finally
            {
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewSources.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateSourcesList(sources: List<NewsSource>)
    {
        currentSources.clear()
        currentSources.addAll(sources)
        sourceAdapter.notifyDataSetChanged()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}