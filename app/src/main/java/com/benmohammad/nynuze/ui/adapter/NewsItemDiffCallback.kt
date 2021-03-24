package com.benmohammad.nynuze.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.benmohammad.nynuze.data.entity.News

class NewsItemDiffCallback: DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }
}