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
import com.example.cryptoappfinalproject.databinding.ReceivedDmBinding
import com.example.cryptoappfinalproject.databinding.SentDmBinding
import com.example.cryptoappfinalproject.databinding.SingleCrypto2Binding
import com.example.cryptoappfinalproject.databinding.SingleCryptoBinding
import com.example.cryptoappfinalproject.domain.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.MessageModel
import com.example.cryptoappfinalproject.ui.favorites.favExTitle
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter(

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var list: MutableList<MessageModel> = mutableListOf()

    fun submitList(newList: MutableList<MessageModel>) {
        list = newList
    }


    private val sent = 0
    private val received = 1

    inner class SenderViewHolder(val binding: SentDmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val model = list[bindingAdapterPosition]
            binding.apply {
                tvSentDM.text = model.message
            }

        }
    }


    inner class ReceiverViewHolder(val binding: ReceivedDmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val model = list[bindingAdapterPosition]
            binding.apply {
                tvSentDM.text = model.message
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            SenderViewHolder(
                SentDmBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else ReceiverViewHolder(
            ReceivedDmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as SenderViewHolder).bind()

        }
        if (getItemViewType(position) == 1) {
            (holder as ReceiverViewHolder).bind()


        }


    }


    override fun getItemViewType(position: Int): Int {

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(list[position].senderId)) {
            return sent
        } else {
            return received
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
