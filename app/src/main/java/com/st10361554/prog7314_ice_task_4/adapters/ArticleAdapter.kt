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

class ArticleAdapter(private val articles: MutableList<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>()
{
    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvSource: TextView = itemView.findViewById(R.id.tvSource)
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        private val tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
        private val ivArticleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)

        fun bind(article: Article) {
            tvTitle.text = article.title
            tvDescription.text = article.description ?: "No description available"
            tvSource.text = article.source.name
            tvAuthor.text = article.author ?: "Unknown author"
            tvPublishedAt.text = formatDate(article.publishedAt)

            // Load image with Glide
            if (!article.urlToImage.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.ic_default_image) // Add a placeholder
                    .error(R.drawable.ic_default_image) // Add an error image
                    .into(ivArticleImage)
            } else {
                // Set a placeholder or hide the image view
                ivArticleImage.setImageResource(R.drawable.ic_default_image)
            }

            // Click listener to open article in browser
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                itemView.context.startActivity(intent)
            }
        }

        private fun formatDate(dateString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                val date = inputFormat.parse(dateString)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                dateString
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    // Method to update articles list
    fun updateArticles(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }
}