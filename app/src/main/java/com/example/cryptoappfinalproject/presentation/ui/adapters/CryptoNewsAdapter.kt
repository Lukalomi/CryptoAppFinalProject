package com.example.cryptoappfinalproject.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.databinding.SingleNewsItemBinding
import com.example.cryptoappfinalproject.domain.model.CryptoNewsModel

class CryptoNewsAdapter(
    private val context: Context,
    var onClickListener: ((CryptoNewsModel.Data) -> Unit)? = null,
) :
    PagingDataAdapter<CryptoNewsModel.Data, CryptoNewsAdapter.ItemViewHolder>(
        DiffUtilCallback()
    ) {

    inner class ItemViewHolder(val binding: SingleNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var model: CryptoNewsModel.Data? = null
        fun onBind() {

            model = getItem(bindingAdapterPosition)
            binding.apply {
                tvNewsTitle.text = model?.title
                tvNewsText.text = model?.text

                Glide.with(context)
                    .load(model?.imageUrl)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivNews)
                ivNews.setOnClickListener {
                    getItem(position = bindingAdapterPosition)?.let { it1 ->
                        onClickListener?.invoke(
                            it1
                        )
                    }
                }
            }

        }
    }


    override fun onBindViewHolder(holder: CryptoNewsAdapter.ItemViewHolder, position: Int) {
        holder.onBind()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CryptoNewsAdapter.ItemViewHolder {
        val binding =
            SingleNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }


    class DiffUtilCallback() : DiffUtil.ItemCallback<CryptoNewsModel.Data>() {
        override fun areItemsTheSame(
            oldItem: CryptoNewsModel.Data,
            newItem: CryptoNewsModel.Data
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: CryptoNewsModel.Data,
            newItem: CryptoNewsModel.Data
        ): Boolean {
            return oldItem == newItem
        }

    }
}