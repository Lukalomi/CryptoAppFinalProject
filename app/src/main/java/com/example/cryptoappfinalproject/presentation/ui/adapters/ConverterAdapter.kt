package com.example.cryptoappfinalproject.presentation.ui.adapters

//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.example.cryptoappfinalproject.databinding.SingleNumericButtonBinding
//import com.example.cryptoappfinalproject.databinding.SingleRemoveButtonBinding
//import com.example.cryptoappfinalproject.domain.ButtonActions
//import com.example.cryptoappfinalproject.domain.ButtonTypes
//
//class ConverterAdapter : ListAdapter<ButtonTypes, RecyclerView.ViewHolder>(ButtonItemCallback) {
//
//    private companion object {
//        const val BTN_NUMERIC = 1
//        const val BTN_REMOVE = 2
//    }
//
//    var onNumericClickListener: ((ButtonTypes.Numeric) -> Unit)? = null
//
//    var onRemoveClickListener: (() -> Unit)? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            BTN_NUMERIC -> NumericViewHolder(
//                SingleNumericButtonBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//            else -> RemoveViewHolder(
//                SingleRemoveButtonBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is NumericViewHolder -> holder.bind()
//            is RemoveViewHolder -> holder.bind()
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return when (getItem(position)) {
//            is ButtonTypes.Numeric -> BTN_NUMERIC
//            else -> BTN_REMOVE
//        }
//    }
//
//    inner class NumericViewHolder(private val binding: SingleNumericButtonBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind() {
//            val numeric = getItem(absoluteAdapterPosition) as ButtonTypes.Numeric
//            binding.root.apply {
//                text = numeric.number.toString()
//                setOnClickListener {
//                    onNumericClickListener?.invoke(numeric)
//                }
//            }
//        }
//    }
//
//    inner class RemoveViewHolder(private val binding: SingleRemoveButtonBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind() {
//            val removeButton = getItem(absoluteAdapterPosition) as ButtonTypes.Remove
//            binding.root.apply {
//                when (removeButton.action) {
//                    ButtonActions.REMOVE -> {
//                        setImageResource(removeButton.icon)
//                        setOnClickListener {
//                            onRemoveClickListener?.invoke()
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private object ButtonItemCallback : DiffUtil.ItemCallback<ButtonTypes>() {
//        override fun areItemsTheSame(oldItem: ButtonTypes, newItem: ButtonTypes): Boolean {
//            return false
//        }
//
//        override fun areContentsTheSame(oldItem: ButtonTypes, newItem: ButtonTypes): Boolean {
//            return false
//        }
//    }
//
//}