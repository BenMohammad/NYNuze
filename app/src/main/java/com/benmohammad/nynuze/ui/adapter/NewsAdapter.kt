package com.benmohammad.nynuze.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.benmohammad.nynuze.R
import com.benmohammad.nynuze.data.entity.News
import com.benmohammad.nynuze.inflate
import com.benmohammad.nynuze.ui.details.DetailsActivity
import com.benmohammad.nynuze.utils.RoundTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class NewsAdapter(private val context: Context,
                 private val newsType: String): ListAdapter<News, NewsItemViewHolder>(NewsItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        return NewsItemViewHolder(parent.inflate(R.layout.list_item))
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val news = currentList[position]
        holder.itemView.tv_author.text = news.author
        holder.itemView.tv_title.text = news.title
        if(!news.thumbnail.isNullOrBlank()) {
            Picasso.get()
                    .load(news.thumbnail)
                    .transform(RoundTransformation(4f))
                    .into(holder.itemView.image)
        }
        holder.itemView.setOnClickListener {
            context
                    .startActivity(DetailsActivity.getNewIntent(
                            news.id,
                            context,
                            newsType
                    ))
        }
    }
}