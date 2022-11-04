package com.example.cryptoappfinalproject.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoappfinalproject.databinding.LoadStateViewBinding


class CoinLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CoinLoadStateAdapter.LoadStateViewHolder>() {


    class LoadStateViewHolder( val binding: LoadStateViewBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

        val progress = holder.binding.loadStateProgress
        val btnRetry = holder.binding.loadStateRetry
        val txtErrorMessage = holder.binding.loadStateErrorMessage

        btnRetry.isVisible = loadState !is LoadState.Loading
        txtErrorMessage.isVisible = loadState !is LoadState.Loading
        progress.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error){
            txtErrorMessage.text = loadState.error.localizedMessage
        }

        btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LoadStateViewBinding.inflate(layoutInflater, parent, false)
        return LoadStateViewHolder(binding)
    }

}