package com.example.cryptoappfinalproject.presentation.ui.adapters

import android.content.Context
<<<<<<< HEAD:app/src/main/java/com/example/cryptoappfinalproject/presentation/ui/adapters/CoinsHomeAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
=======
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
>>>>>>> 8ac69df4bc609d982b9370e492c377fe69238b5a:app/src/main/java/com/example/cryptoappfinalproject/ui/adapters/CoinsHomeAdapter.kt
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.R
<<<<<<< HEAD:app/src/main/java/com/example/cryptoappfinalproject/presentation/ui/adapters/CoinsHomeAdapter.kt
import com.example.cryptoappfinalproject.databinding.SingleCryptoBinding
import com.example.cryptoappfinalproject.domain.model.CryptoCoinsModel
=======
import com.example.cryptoappfinalproject.databinding.SingleCrypto2Binding
import com.example.cryptoappfinalproject.databinding.SingleCryptoBinding
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.ui.favorites.favCoinTitle
import com.example.cryptoappfinalproject.ui.favorites.favExTitle
import com.example.cryptoappfinalproject.ui.favorites.favList
>>>>>>> 8ac69df4bc609d982b9370e492c377fe69238b5a:app/src/main/java/com/example/cryptoappfinalproject/ui/adapters/CoinsHomeAdapter.kt

class CoinsHomeAdapter(
    private val context: Context,
    var onFavListener: ((CryptoCoinsModel.CryptoCoinsModelItem) -> Unit)? = null,

    ) : PagingDataAdapter<CryptoCoinsModel.CryptoCoinsModelItem, RecyclerView.ViewHolder>(
    DiffUtilCallback()
) {


    inner class CoinsViewHolder(val binding: SingleCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var model: CryptoCoinsModel.CryptoCoinsModelItem? = null

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind() {
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

                cvItem.setOnClickListener {
                    cvItem.startAnimation(AnimationUtils.loadAnimation(context,
                        androidx.appcompat.R.anim.abc_shrink_fade_out_from_bottom))
                }

                ibCryptoFav.setImageResource(R.drawable.ic_heart_fav)


            }


        }


    }


    inner class CoinsViewHolder2(val binding: SingleCrypto2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        var model: CryptoCoinsModel.CryptoCoinsModelItem? = null
        fun bind() {
            @RequiresApi(Build.VERSION_CODES.M)
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
                cvItem.setOnClickListener {
                    cvItem.startAnimation(AnimationUtils.loadAnimation(context,
                        androidx.appcompat.R.anim.abc_shrink_fade_out_from_bottom))
                }
                ibCryptoFav.setImageResource(R.drawable.ic_heart_checked)


            }


        }


    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as CoinsHomeAdapter.CoinsViewHolder).bind()

        } else {
            (holder as CoinsHomeAdapter.CoinsViewHolder2).bind()

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            CoinsViewHolder(
                SingleCryptoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else CoinsViewHolder2(
            SingleCrypto2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun getItemViewType(position: Int): Int {
        var model: CryptoCoinsModel.CryptoCoinsModelItem? = getItem(position)
        return if(favCoinTitle.contains(model?.name)) 1
        else 0
    }


    class DiffUtilCallback() : DiffUtil.ItemCallback<CryptoCoinsModel.CryptoCoinsModelItem>() {
        override fun areItemsTheSame(
            oldItem: CryptoCoinsModel.CryptoCoinsModelItem,
            newItem: CryptoCoinsModel.CryptoCoinsModelItem
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CryptoCoinsModel.CryptoCoinsModelItem,
            newItem: CryptoCoinsModel.CryptoCoinsModelItem
        ): Boolean {
            return oldItem == newItem
        }

    }

}