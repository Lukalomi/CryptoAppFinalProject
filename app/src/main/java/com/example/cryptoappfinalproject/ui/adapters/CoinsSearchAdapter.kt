package com.example.cryptoappfinalproject.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.databinding.SingleCryptoBinding
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.CryptoSearchModel

class CoinsSearchAdapter(
    private val context: Context,
    var onClickListener: ((CryptoSearchModel.Coin) -> Unit)? = null,
    var onFavListener: ((CryptoSearchModel.Coin) -> Unit)? = null
): RecyclerView.Adapter<CoinsSearchAdapter.SearchViewHolder>() {

    var list: Resource.Success<MutableList<CryptoSearchModel.Coin>> = Resource.Success(mutableListOf())
    fun submitList(newList: Resource.Success<MutableList<CryptoSearchModel.Coin>>) {
        list = newList
    }

    inner class SearchViewHolder(val binding: SingleCryptoBinding):RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoinsSearchAdapter.SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleCryptoBinding.inflate(layoutInflater,parent,false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinsSearchAdapter.SearchViewHolder, position: Int) {
        holder.binding.apply {
            tvCryptoName.text = list.data[position].name
            tvCryptoRank.text = list.data[position].marketCapRank.toString()
            tvCryptoVolume.text = ""
            Glide.with(context)
                .load(list.data[position].thumb)
                .error(R.drawable.ic_launcher_background)
                .into(ivCrypto)

            ibCryptoFav.setOnClickListener {
                onClickListener?.invoke(list.data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.data.size
    }

}