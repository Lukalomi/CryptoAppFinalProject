package com.example.cryptoappfinalproject.ui.converter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.ButtonUtil
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.databinding.FragmentConverterBinding
import com.example.cryptoappfinalproject.databinding.FragmentHomeBinding
import com.example.cryptoappfinalproject.domain.ButtonTypes
import com.example.cryptoappfinalproject.ui.adapters.ConverterAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ConverterFragment : Fragment() {

    private var binding: FragmentConverterBinding? = null

    private val converterAdapter = ConverterAdapter()

    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConverterBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        onClickListeners()
        convertCrypto()

    }


    private fun setupAdapter() {
        binding?.rvConverter?.adapter = converterAdapter
        converterAdapter.submitList(ButtonUtil.BUTTONS)
    }


    private fun onClickListeners() {
        converterAdapter.onNumericClickListener = {
            handleNumericButtonClick(it)
        }
        converterAdapter.onRemoveClickListener = {
            handleRemoveButtonClick()
        }
    }

    private fun handleNumericButtonClick(numeric: ButtonTypes.Numeric) {

    }



    private fun handleRemoveButtonClick() {

    }


    private fun convertCrypto() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.convertCrypto(
                    id = binding?.tvChooseCrypto.toString(),
                    currency = binding?.tvFiatCurrency.toString()
                )
                    .collect {
                    when (it) {
                        is Resource.Error -> Log.d("error", "${it.errorMsg}")

                        is Resource.Loader -> Log.d("loader", "loading")

                        is Resource.Success -> {
                            binding?.etResult?.setText( it.data.toString() )

                        }
                    }
                }
            }
        }
    }





}