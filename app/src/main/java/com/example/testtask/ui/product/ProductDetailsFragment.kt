package com.example.testtask.ui.product

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import com.example.testtask.R
import com.example.testtask.data.model.Product
import com.example.testtask.databinding.FragmentProductDetailsBinding
import com.example.testtask.util.ARG_PRODUCT
import dagger.android.support.DaggerFragment

class ProductDetailsFragment : DaggerFragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arguments?.getSerializable(ARG_PRODUCT, Product::class.java)
        else
            arguments?.getSerializable(ARG_PRODUCT) as Product
        if (product == null) {
            Toast.makeText(context, R.string.message_product_is_null, Toast.LENGTH_SHORT).show()
            return
        }
        product.images?.stream()?.findFirst()?.ifPresent {
            binding.imageProduct.load(it)
        }
        binding.textViewDescription.text = product.description
    }
}