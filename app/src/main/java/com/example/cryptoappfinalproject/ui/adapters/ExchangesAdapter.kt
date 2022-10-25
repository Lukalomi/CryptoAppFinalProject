package com.example.cryptoappfinalproject.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.local.Exchanges
import com.example.cryptoappfinalproject.databinding.SingleCrypto2Binding
import com.example.cryptoappfinalproject.databinding.SingleCryptoBinding
import com.example.cryptoappfinalproject.domain.CryptoExchangesModel
import com.example.cryptoappfinalproject.ui.favorites.favExTitle


class ExchangesAdapter(
    val context: Context,
    var onClickListener: ((CryptoExchangesModel.CryptoExchangesModelItem) -> Unit)? = null

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var list: Resource.Success<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>> =
        Resource.Success(mutableListOf())

    fun submitList(newList: Resource.Success<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>>) {
        list = newList
    }


    inner class ExchangesViewHolder(val binding: SingleCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val model = list.data[bindingAdapterPosition]
            binding.apply {
                Glide.with(context)
                    .load(model.image)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivCrypto)
                tvCryptoName.text = model.name
                tvCryptoRank.text = model.trustScoreRank.toString()
                tvCryptoVolume.text =
                    String.format("%.2f", model.tradeVolume24hBtcNormalized) + " BTC"
                ibCryptoFav.setImageResource(R.drawable.ic_heart_fav)
                ibCryptoFav.setOnClickListener {
                    onClickListener?.invoke(model)
                }
                cvItem.setOnClickListener {
                    cvItem.startAnimation(AnimationUtils.loadAnimation(context,
                        androidx.appcompat.R.anim.abc_shrink_fade_out_from_bottom))
                }
            }

        }

    }


    inner class ExchangesViewHolder2(val binding: SingleCrypto2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val model = list.data[bindingAdapterPosition]
            binding.apply {
                Glide.with(context)
                    .load(model.image)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivCrypto)
                tvCryptoName.text = model.name
                tvCryptoRank.text = model.trustScoreRank.toString()
                tvCryptoVolume.text =
                    String.format("%.2f", model.tradeVolume24hBtcNormalized) + " BTC"
                ibCryptoFav.setImageResource(R.drawable.ic_heart_checked)
                ibCryptoFav.setOnClickListener {
                    onClickListener?.invoke(model)
                }
                cvItem.setOnClickListener {
                    cvItem.startAnimation(
                        AnimationUtils.loadAnimation(context,
                        androidx.appcompat.R.anim.abc_shrink_fade_out_from_bottom))
                }
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            ExchangesViewHolder(
                SingleCryptoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else ExchangesViewHolder2(
            SingleCrypto2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as ExchangesViewHolder).bind()

        } else {
            (holder as ExchangesViewHolder2).bind()

        }
    }



    override fun getItemViewType(position: Int): Int {

        return if(favExTitle.contains(list.data[position].name)) 1
        else 0
    }

    override fun getItemCount(): Int {
        return list.data.size
    }
}