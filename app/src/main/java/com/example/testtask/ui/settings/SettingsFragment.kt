package com.example.testtask.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.testtask.R
import com.example.testtask.databinding.FragmentSettingsBinding
import com.example.testtask.util.Result
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        lifecycleScope.launch {
            viewModel.isUrlValidData.onEach {
                binding.btnSave.isEnabled = it
            }.collect()
        }
        lifecycleScope.launch {
            viewModel.testConnectionResultData.onEach {
                disableViews(it is Result.Loading)
                when (it) {
                    is Result.Success -> Toast.makeText(
                        requireActivity(),
                        R.string.message_connection_success,
                        Toast.LENGTH_SHORT
                    ).show()

                    is Result.Failure -> Toast.makeText(
                        requireActivity(),
                        R.string.message_connection_error,
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> {}
                }
            }.collect()
        }
        viewModel.urlData.observe(viewLifecycleOwner) {
            binding.editTextUrl.editText?.setText(it)
        }
        binding.btnTestConnection.setOnClickListener {
            viewModel.testConnection(binding.editTextUrl.editText?.text.toString())
        }
        binding.btnSave.setOnClickListener {
            viewModel.saveUrl(binding.editTextUrl.editText?.text.toString())
        }
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (_binding != null)
            viewModel.updateUrl(_binding!!.editTextUrl.editText?.text.toString())
    }

    private fun disableViews(disable: Boolean) {
        binding.progress.isVisible = disable
        binding.btnTestConnection.isEnabled = !disable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}