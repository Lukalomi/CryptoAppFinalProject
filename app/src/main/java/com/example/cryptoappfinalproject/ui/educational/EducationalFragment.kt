package com.example.cryptoappfinalproject.ui.educational

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoappfinalproject.R

class EducationalFragment : Fragment() {

    companion object {
        fun newInstance() = EducationalFragment()
    }

    private lateinit var viewModel: EducationalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_educational, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EducationalViewModel::class.java)
        // TODO: Use the ViewModel
    }

}