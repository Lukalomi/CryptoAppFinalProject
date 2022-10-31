package com.example.cryptoappfinalproject.presentation.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoappfinalproject.databinding.ReceivedDmBinding
import com.example.cryptoappfinalproject.databinding.SentDmBinding
import com.example.cryptoappfinalproject.domain.model.MessageModel
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
