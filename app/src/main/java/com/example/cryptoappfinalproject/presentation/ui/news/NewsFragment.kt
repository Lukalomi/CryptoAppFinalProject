package com.example.cryptoappfinalproject.presentation.ui.news

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.BaseFragment
import com.example.cryptoappfinalproject.databinding.FragmentNewsBinding
import com.example.cryptoappfinalproject.presentation.ui.adapters.CryptoNewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>(
    FragmentNewsBinding::inflate,
    NewsViewModel::class.java
) {

    private lateinit var adapter: CryptoNewsAdapter


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
        binding.btnAuth.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer)
            drawer.openDrawer(Gravity.LEFT)

        }
    }

    private fun setNewsAdapter() {
        adapter = CryptoNewsAdapter(requireContext())
        binding.rvNews.layoutManager = GridLayoutManager(requireContext(),2)
        binding.rvNews.adapter = adapter
        adapter.onClickListener = {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                val uri:Uri = Uri.parse(it.newsUrl)
                startActivity(Intent(Intent.ACTION_VIEW,uri))
            }

            builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            builder.setMessage(getString(R.string.go_to_web))
            builder.create().show()


        }
    }



}