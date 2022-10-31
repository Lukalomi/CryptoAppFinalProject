package com.example.cryptoappfinalproject.presentation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoappfinalproject.databinding.SingleUserBinding
import com.example.cryptoappfinalproject.domain.model.FirebaseUserModel

class UserAdapter(
    private val context: Context,
    var onClickListener: ((FirebaseUserModel) -> Unit)? = null
) : RecyclerView.Adapter<UserAdapter.ItemViewHolder>() {

    var list: MutableList<FirebaseUserModel> = mutableListOf()
    fun submitList(newList: MutableList<FirebaseUserModel>) {
        list = newList
    }

    inner class ItemViewHolder(val binding: SingleUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleUserBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        Glide.with(context)
//            .load(list.data[position].avatar)
//            .error(R.drawable.ic_launcher_background)
//            .into(holder.binding.ivUser)
        holder.binding.apply {
            tvUser.text = list[position].name
            singleUserLayout.setOnClickListener{
                onClickListener?.invoke(list[position])

            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
