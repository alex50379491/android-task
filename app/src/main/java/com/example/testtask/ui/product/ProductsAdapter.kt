package com.example.testtask.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testtask.data.model.Product
import com.example.testtask.databinding.ListItemProductBinding
import com.example.testtask.util.PRICE_FORMAT
import java.text.DecimalFormat

class ProductsAdapter(private val listener: ProductSelectionListener) :
    PagingDataAdapter<Product, ProductViewHolder>(ProductsDiffCallback()) {

    private val priceFormat = DecimalFormat(PRICE_FORMAT)

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), listener, priceFormat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ListItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class ProductsDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}

class ProductViewHolder(private val binding: ListItemProductBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Product?, listener: ProductSelectionListener, priceFormat: DecimalFormat) {
        with(binding) {
            if (item?.thumbnail?.isNotBlank() == true) {
                imageThumbnail.load(item.thumbnail)
            }
            textViewTitle.text = item?.title
            textViewPrice.text =
                if (item?.price != null) priceFormat.format(item.price) else null
            root.setOnClickListener { item?.let { p -> listener.onProductSelected(p) } }
        }
    }
}