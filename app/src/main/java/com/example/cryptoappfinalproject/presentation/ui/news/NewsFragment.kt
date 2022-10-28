package com.example.cryptoappfinalproject.presentation.ui.news

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.databinding.FragmentNewsBinding
import com.example.cryptoappfinalproject.presentation.ui.adapters.CryptoNewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NewsFragment : Fragment() {


    private var binding: FragmentNewsBinding? = null
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter: CryptoNewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllNews()
        openDrawer()
    }



    private fun getAllNews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newsPager.collect{
                setNewsAdapter()
                adapter.submitData(it)
            }
        }

        }

    private fun openDrawer() {
        binding!!.btnAuth.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer)
            drawer.openDrawer(Gravity.LEFT)

        }
    }

    private fun setNewsAdapter() {
        adapter = CryptoNewsAdapter(requireContext())
        binding!!.rvNews.layoutManager = GridLayoutManager(requireContext(),2)
        binding!!.rvNews.adapter = adapter
        adapter.onClickListener = {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                val uri:Uri = Uri.parse(it.newsUrl)
                startActivity(Intent(Intent.ACTION_VIEW,uri))
            }

            builder.setNegativeButton("No") { _, _ -> }
            builder.setMessage("this action will redirect you to web, do you want to proceed?")
            builder.create().show()


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}