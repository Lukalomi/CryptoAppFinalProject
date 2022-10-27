package com.example.cryptoappfinalproject.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.databinding.SingleCryptoBinding
import com.example.cryptoappfinalproject.domain.model.CryptoExchangesModel

class ExchangesAdapter(
    val context: Context,
    var onClickListener: ((CryptoExchangesModel.CryptoExchangesModelItem) -> Unit)? = null

): RecyclerView.Adapter<ExchangesAdapter.ExchangesViewHolder>() {


    var list: Resource.Success<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>> = Resource.Success(mutableListOf())
    fun submitList(newList: Resource.Success<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>>) {
        list = newList
    }


    inner class ExchangesViewHolder(val binding: SingleCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExchangesAdapter.ExchangesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleCryptoBinding.inflate(layoutInflater, parent, false)
        return ExchangesViewHolder(binding)    }

    override fun onBindViewHolder(holder: ExchangesAdapter.ExchangesViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(context)
                .load(list.data[position].image)
                .error(R.drawable.ic_launcher_background)
                .into(ivCrypto)
            tvCryptoName.text = list.data[position].name
            tvCryptoRank.text = list.data[position].trustScoreRank.toString()
            tvCryptoVolume.text =String.format("%.2f",list.data[position]?.tradeVolume24hBtcNormalized) + " BTC"
            ibCryptoFav.setOnClickListener {
                onClickListener?.invoke(list.data[position])
            }
        }
    }
    override fun getItemCount(): Int {
        return list.data.size
    }
}