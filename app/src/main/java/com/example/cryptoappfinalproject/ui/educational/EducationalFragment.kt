package com.example.cryptoappfinalproject.ui.educational

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.databinding.FragmentNewsBinding
import com.example.cryptoappfinalproject.databinding.FragmentRegistrationBinding

class EducationalFragment : Fragment() {

    private var binding: FragmentNewsBinding? = null


    private lateinit var viewModel: EducationalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openDrawer()
    }



    private fun openDrawer() {
        binding!!.btnAuth.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer)
            drawer.openDrawer(Gravity.LEFT)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}