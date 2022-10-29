package com.example.cryptoappfinalproject.presentation.ui.converter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.databinding.FragmentConverterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConverterFragment : Fragment() {

    private var binding: FragmentConverterBinding? = null

//    private val converterAdapter = ConverterAdapter()

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

        setUpConverter()
        convertCrypto()
        activateConvertButton()
        backBtn()
//        setupButtonsAdapter()
//        onClickListeners()



    }


    private fun setUpConverter() {
        setDropDownItems()
//        setAmountInput()

    }

    private fun backBtn() {
        binding!!.ivBackConverter.setOnClickListener {
            findNavController().navigate(ConverterFragmentDirections.actionConverterFragmentToHomeFragment())
        }
    }

    private fun setDropDownItems(){
        val cryptoCurrencies = resources.getStringArray(R.array.CryptoCurrencies)
        val cryptoAdapter = ArrayAdapter(requireContext(), R.layout.currency_dropdown_items, cryptoCurrencies)
        binding?.tvChooseCrypto?.setAdapter(cryptoAdapter)

        val fiatCurrencies = resources.getStringArray(R.array.FiatCurrencies)
        val fiatAdapter = ArrayAdapter(requireContext(), R.layout.currency_dropdown_items, fiatCurrencies)
        binding?.tvFiatCurrency?.setAdapter(fiatAdapter)
    }

    private fun activateConvertButton() {
        binding?.etAmount?.doAfterTextChanged { text ->
            if (text != null) {
                binding?.btnConvert?.isEnabled = text.isNotEmpty()
            }
        }
    }

    private fun convertCrypto() {

        binding?.btnConvert?.setOnClickListener {
            val amount = binding?.etAmount?.text.toString()
            val fromCrypto = binding?.tvChooseCrypto?.text.toString()
            val toCurrency = binding?.tvFiatCurrency?.text.toString()


            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.convertCrypto(from = fromCrypto, to = toCurrency, amount = amount).collect {
                    when (it) {
                        is Resource.Error -> Log.d("error", "${it.errorMsg}")

                        is Resource.Loader -> Log.d("loader", "loading")

                        is Resource.Success -> {

                            binding?.tvResult?.text = it.data.result.toString().dropLast(2)

                            Log.d("convert",  "${it.data.result.toString()}")

                        }
                    }
                }
            }
        }


    }


    //    private fun setAmountInput(){
//        binding?.etAmount?.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//            override fun afterTextChanged(p0: Editable?) {}
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (binding?.etAmount?.text.toString().isNotEmpty()) {
//                    convertCrypto()
//                } else {
//                    binding?.tvResult?.setText("")
//                }
//            }
//        })
//    }


//
//    private fun setupButtonsAdapter() {
//        binding?.rvConverter?.adapter = converterAdapter
//        converterAdapter.submitList(ButtonUtil.BUTTONS)
//    }
//
//    private fun onClickListeners() {
//        converterAdapter.onNumericClickListener = {
//            handleNumericButtonClick(it)
//        }
//        converterAdapter.onRemoveClickListener = {
//            handleRemoveButtonClick()
//        }
//    }
//
//
//
//    private fun handleNumericButtonClick(numeric: ButtonTypes.Numeric) {
//
//    }
//
//
//
//    private fun handleRemoveButtonClick() {
//        var amount = binding?.etAmount?.text.toString().toInt()
//
//        if (amount != null) {
//            var changedAmount = amount.minus(1)
//            amount = changedAmount
//        }
//    }




}