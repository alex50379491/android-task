package com.example.testtask.ui.quotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.data.model.Quote
import com.example.testtask.databinding.ListItemQuoteBinding

class QuotesAdapter : PagingDataAdapter<Quote, QuoteViewHolder>(QuotesDiffCallback()) {
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            ListItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class QuotesDiffCallback : DiffUtil.ItemCallback<Quote>() {
    override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem == newItem
    }
}

class QuoteViewHolder(private val binding: ListItemQuoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Quote?) {
        with(binding) {
            textViewQuote.text = item?.quote
            textViewAuthor.text = item?.author
        }
    }
}