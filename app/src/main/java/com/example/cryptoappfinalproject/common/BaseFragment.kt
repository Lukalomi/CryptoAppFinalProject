package com.example.cryptoappfinalproject.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding


typealias Inflater<T> = (inflater: LayoutInflater, view: ViewGroup?, attach: Boolean) -> T

abstract class BaseFragment<VB : ViewBinding, VM: ViewModel> (
    private var inflater: Inflater<VB>,
    private var viewModelClass: Class <VM>
    ) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected val viewModel: VM by lazy {
        ViewModelProvider(this)[viewModelClass]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = this.inflater.invoke(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}