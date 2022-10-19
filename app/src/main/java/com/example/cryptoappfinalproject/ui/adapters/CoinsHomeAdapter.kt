package com.example.cryptoappfinalproject.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.paging.DifferCallback
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.databinding.SingleCryptoBinding
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.CryptoSearchModel
import com.example.cryptoappfinalproject.favList
import com.example.cryptoappfinalproject.ui.favorites.FavoritesViewModel

class CoinsHomeAdapter(
    private val context: Context,
    var onFavListener: ((CryptoCoinsModel.CryptoCoinsModelItem) -> Unit)? = null,
) : PagingDataAdapter<CryptoCoinsModel.CryptoCoinsModelItem, CoinsHomeAdapter.ItemViewHolder>(
    DiffUtilCallback()
) {


    inner class ItemViewHolder(val binding: SingleCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var model: CryptoCoinsModel.CryptoCoinsModelItem? = null
        fun onBind() {
            model = getItem(bindingAdapterPosition)
            binding.apply {
                tvCryptoName.text = model?.name
                tvCryptoRank.text = model?.marketCapRank.toString()
                if (model?.priceChangePercentage24h.toString().contains("-")) {
                    tvCryptoVolume.text =
                        String.format("%.2f", model?.priceChangePercentage24h) + " %"
                    tvCryptoVolume.setTextColor(ContextCompat.getColor(context, R.color.red))

                } else {
                    tvCryptoVolume.text =
                        "+" + String.format("%.2f", model?.priceChangePercentage24h) + " %"
                    tvCryptoVolume.setTextColor(ContextCompat.getColor(context, R.color.green))

                }
                tvCryptoPrice.text = "$" + String.format("%.2f", model?.currentPrice)
                Glide.with(context)
                    .load(model?.image)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivCrypto)

                ibCryptoFav.setOnClickListener {
                    getItem(position = bindingAdapterPosition)?.let { it1 ->
                        onFavListener?.invoke(
                            it1
                        )
                    }
                }


            }
        }


    }


    override fun onBindViewHolder(holder: CoinsHomeAdapter.ItemViewHolder, position: Int) {
        holder.onBind()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoinsHomeAdapter.ItemViewHolder {
        val binding =
            SingleCryptoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }


    class DiffUtilCallback() : DiffUtil.ItemCallback<CryptoCoinsModel.CryptoCoinsModelItem>() {
        override fun areItemsTheSame(
            oldItem: CryptoCoinsModel.CryptoCoinsModelItem,
            newItem: CryptoCoinsModel.CryptoCoinsModelItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CryptoCoinsModel.CryptoCoinsModelItem,
            newItem: CryptoCoinsModel.CryptoCoinsModelItem
        ): Boolean {
            return oldItem == newItem
        }

    }

}