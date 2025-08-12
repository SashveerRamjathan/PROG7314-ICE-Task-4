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

class SourceAdapter(private val sources: MutableList<NewsSource>) : RecyclerView.Adapter<SourceAdapter.SourceViewHolder>()
{

    class SourceViewHolder(private val binding: ItemSourceBinding) : RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("SetTextI18n")
        fun bind(source: NewsSource) {
            binding.tvSourceName.text = source.name
            binding.tvSourceDescription.text = source.description
            binding.tvSourceCategory.text =
                "Category: ${source.category.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}"
            binding.tvSourceCountry.text = "Country: ${source.country.uppercase()}"
            binding.tvSourceLanguage.text = "Language: ${source.language.uppercase()}"

            // Click listener to open source website
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, source.url.toUri())
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder
    {
        val binding = ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.bind(sources[position])
    }

    override fun getItemCount(): Int = sources.size

}