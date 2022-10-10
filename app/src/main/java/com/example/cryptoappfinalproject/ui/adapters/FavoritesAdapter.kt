package com.example.cryptoappfinalproject.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.databinding.SingleCryptoBinding

class FavoritesAdapter(
    val context: Context
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    var onClickListener: ((Crypto) -> Unit)? = null

    var list: MutableList<Crypto> = mutableListOf()
    fun submitList(newList: MutableList<Crypto>) {
        list = newList
    }

    inner class FavoritesViewHolder(val binding: SingleCryptoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleCryptoBinding.inflate(layoutInflater, parent, false)
        return FavoritesViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.binding.ibCryptoFav.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_delete_fav
            )
        )
        Glide.with(context)
            .load(list[position].image)
            .error(R.drawable.ic_launcher_background)
            .into(holder.binding.ivCrypto)
        holder.binding.tvCryptoName.text = list[position].originalTitle
        holder.binding.tvCryptoRank.text = list[position].marketCapRank.toString()
        holder.binding.tvCryptoPrice.text = "$" + String.format("%.2f", list[position].currentPrice)
        if (list[position].priceChangePercentage24h.toString().contains("-")) {
            holder.binding.tvCryptoVolume.text =
                String.format("%.2f", list[position].priceChangePercentage24h) + " %"
            holder.binding.tvCryptoVolume.setTextColor(ContextCompat.getColor(context, R.color.red))

        } else {
            holder.binding.tvCryptoVolume.text =
                "+" + String.format("%.2f", list[position]?.priceChangePercentage24h) + " %"
            holder.binding.tvCryptoVolume.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.green
                )
            )
        }
        holder.binding.ibCryptoFav.setOnClickListener {
            onClickListener?.invoke(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size

    }
}