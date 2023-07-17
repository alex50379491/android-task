package com.example.testtask.ui.quotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testtask.R
import com.example.testtask.databinding.FragmentQuotesBinding
import com.example.testtask.ui.common.PagingLoadStateAdapter
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class QuotesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentQuotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[QuotesViewModel::class.java]
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuotesBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
        val adapter = QuotesAdapter().apply {
            addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Error)
                    Toast.makeText(
                        context,
                        R.string.message_error_getting_quotes,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
        binding.recyclerView.adapter =
            adapter.withLoadStateHeaderAndFooter(PagingLoadStateAdapter(), PagingLoadStateAdapter())
        val quotes = viewModel.getQuotes()
        if (quotes == null)
            Toast.makeText(
                context,
                R.string.message_connection_error,
                Toast.LENGTH_SHORT
            ).show()
        else
            compositeDisposable.add(quotes.subscribe { adapter.submitData(lifecycle, it) })
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}