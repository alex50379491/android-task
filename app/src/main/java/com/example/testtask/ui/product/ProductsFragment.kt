package com.example.testtask.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testtask.R
import com.example.testtask.data.model.Product
import com.example.testtask.databinding.FragmentProductsBinding
import com.example.testtask.ui.common.PagingLoadStateAdapter
import com.example.testtask.util.ARG_PRODUCT
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class ProductsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProductsViewModel::class.java]
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.list_divider_vertical)
            ?.let { decoration.setDrawable(it) }
        binding.recyclerView.addItemDecoration(decoration)
        val adapter = ProductsAdapter { openProductDetails(it) }.apply {
            addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Error)
                    Toast.makeText(
                        context,
                        R.string.message_error_getting_products,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
        binding.recyclerView.adapter =
            adapter.withLoadStateHeaderAndFooter(PagingLoadStateAdapter(), PagingLoadStateAdapter())
        val products = viewModel.getProducts()
        if (products == null)
            Toast.makeText(
                context,
                R.string.message_connection_error,
                Toast.LENGTH_SHORT
            ).show()
        else
            compositeDisposable.add(products.subscribe { adapter.submitData(lifecycle, it) })
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    private fun openProductDetails(product: Product) {
        val args = Bundle()
        args.putSerializable(ARG_PRODUCT, product)
        findNavController().navigate(R.id.action_nav_products_to_nav_product_details, args)
    }
}