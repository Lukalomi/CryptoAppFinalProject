package com.example.cryptoappfinalproject.presentation.ui.converter

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.BaseFragment
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.databinding.FragmentConverterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.RoundingMode


@AndroidEntryPoint
class ConverterFragment : BaseFragment<FragmentConverterBinding, ConverterViewModel>(
    FragmentConverterBinding::inflate,
    ConverterViewModel::class.java
) {

    private var bool: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backBtnListener()
        swipeBtnListener()
        setDropDownItems()
        activateConvertButton()

    }


    private fun backBtnListener() {
        binding.ivBackConverter.setOnClickListener {
            findNavController().navigate(ConverterFragmentDirections.actionConverterFragmentToHomeFragment())
        }
    }


    private fun swipeBtnListener() {
        binding.btnSwipe.setOnClickListener {
            if (bool) {
                bool = false
                swipeInputFields()
                activateSwipedConvertButton()
            } else {
                bool = true
                setDropDownItems()
                activateConvertButton()
                swipeBtnListener()
            }
        }
    }


    private fun setDropDownItems() {
        val autoCompleteTvCrypto = binding.tvChooseCrypto
        autoCompleteTvCrypto.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.teal_200))
        )

        val autoCompleteTvCurrency = binding.tvFiatCurrency
        autoCompleteTvCurrency.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.teal_200))
        )

        val cryptoCurrencies = resources.getStringArray(R.array.CryptoCurrencies)
        val cryptoAdapter =
            ArrayAdapter(requireContext(), R.layout.currency_dropdown_items, cryptoCurrencies)
        binding.tvChooseCrypto.setAdapter(cryptoAdapter)

        val fiatCurrencies = resources.getStringArray(R.array.FiatCurrencies)
        val fiatAdapter =
            ArrayAdapter(requireContext(), R.layout.currency_dropdown_items, fiatCurrencies)
        binding.tvFiatCurrency.setAdapter(fiatAdapter)
    }

    private fun activateConvertButton() {
        binding.btnConvert.setOnClickListener {
            val amount = binding.etAmount.text.toString()
            val fromCrypto = binding.tvChooseCrypto.text.toString()
            val toCurrency = binding.tvFiatCurrency.text.toString()

            if (amount.isNotEmpty() &&
                fromCrypto.isNotEmpty() &&
                toCurrency.isNotEmpty()
            ) {
                convertCrypto()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun convertCrypto() {

        val amount = binding.etAmount.text.toString()
        val fromCrypto = binding.tvChooseCrypto.text.toString()
        val toCurrency = binding.tvFiatCurrency.text.toString()

        viewModel.convertCrypto(fromCrypto, toCurrency, amount)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is Resource.Error -> Log.d("error", it.errorMsg)

                    is Resource.Loader -> Log.d("loader", "loading")

                    is Resource.Success -> {

                        binding.tvResult.text =
                            it.data.result?.toBigDecimal()?.setScale(2, RoundingMode.UP).toString()

                        currencySignListener()

                    }
                }
            }
        }
    }


    private fun currencySignListener() {
        val toCurrency = binding.tvFiatCurrency.text.toString()
        when (toCurrency) {
            "USD" -> {
                binding.tvCurrencySign.text = "$"
            }
            "EUR" -> {
                binding.tvCurrencySign.text = "€"
            }
            "GEL" -> {
                binding.tvCurrencySign.text = "₾"
            }
            else -> {
                binding.tvCurrencySign.text = "£"
            }
        }
    }



    private fun swipeInputFields() {
        val cryptoCurrencies = resources.getStringArray(R.array.CryptoCurrencies)
        val cryptoAdapter =
            ArrayAdapter(requireContext(), R.layout.currency_dropdown_items, cryptoCurrencies)
        binding.tvFiatCurrency.setAdapter(cryptoAdapter)

        val fiatCurrencies = resources.getStringArray(R.array.FiatCurrencies)
        val fiatAdapter =
            ArrayAdapter(requireContext(), R.layout.currency_dropdown_items, fiatCurrencies)
        binding.tvChooseCrypto.setAdapter(fiatAdapter)
    }


    private fun activateSwipedConvertButton() {
        binding.btnConvert.setOnClickListener {
            val amount = binding.etAmount.text.toString()
            val fromCrypto = binding.tvChooseCrypto.text.toString()
            val toCurrency = binding.tvFiatCurrency.text.toString()

            if (amount.isNotEmpty() &&
                fromCrypto.isNotEmpty() &&
                toCurrency.isNotEmpty()
            ) {
                convertSwipedCrypto()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun convertSwipedCrypto() {

        val amount = binding.etAmount.text.toString()
        val fromCrypto = binding.tvChooseCrypto.text.toString()
        val toCurrency = binding.tvFiatCurrency.text.toString()

        viewModel.convertCrypto(fromCrypto, toCurrency, amount)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is Resource.Error -> Log.d("error", it.errorMsg)

                    is Resource.Loader -> Log.d("loader", "loading")

                    is Resource.Success -> {

                        binding.tvResult.text =
                            it.data.result?.toBigDecimal()?.setScale(2, RoundingMode.UP).toString()

                        swipedCurrencySignListener()

                        Log.d("convert", it.data.toString())

                    }
                }
            }
        }

    }


    private fun swipedCurrencySignListener() {
        val currency = binding.tvChooseCrypto.text.toString()
        when (currency) {
            "USD" -> {
                binding.tvCurrencySign.text = "$"
            }
            "EUR" -> {
                binding.tvCurrencySign.text = "€"
            }
            "GEL" -> {
                binding.tvCurrencySign.text = "₾"
            }
            else -> {
                binding.tvCurrencySign.text = "£"
            }
        }
    }


}