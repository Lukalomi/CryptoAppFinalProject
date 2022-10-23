package com.example.cryptoappfinalproject.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.databinding.SingleCryptoBinding
import com.example.cryptoappfinalproject.databinding.SingleVideoItemBinding
import com.example.cryptoappfinalproject.domain.CryptoSearchModel
import com.example.cryptoappfinalproject.domain.VideoTitleModel

class EducationVideosAdapter(
    private val context: Context,
    var onClickListener: ((VideoTitleModel) -> Unit)? = null,
): RecyclerView.Adapter<EducationVideosAdapter.SearchViewHolder>() {

    var list: MutableList<VideoTitleModel> = mutableListOf()
    fun submitList(newList: MutableList<VideoTitleModel>) {
        list = newList
    }

    inner class SearchViewHolder(val binding: SingleVideoItemBinding): RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EducationVideosAdapter.SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleVideoItemBinding.inflate(layoutInflater,parent,false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EducationVideosAdapter.SearchViewHolder, position: Int) {
        holder.binding.apply {
        tvVideoTitle.text = list[position].title

            Glide.with(context)
                .load(list[position].thumbnailUrl)
                .error(R.drawable.ic_launcher_background)
                .into(ivVideo)

            ivVideo.setOnClickListener {
                onClickListener?.invoke(list[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}