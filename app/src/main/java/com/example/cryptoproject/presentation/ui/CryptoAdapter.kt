package com.example.cryptoproject.presentation.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoproject.databinding.CryptoItemViewBinding
import com.example.cryptoproject.domain.model.CryptoItem

class CryptoAdapter(): RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {
    var list: List<CryptoItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CryptoItemViewBinding.inflate(inflater, parent, false)
        return CryptoViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(updatedList: List<CryptoItem>) {
        list = updatedList
    }

    inner class CryptoViewHolder(val binding: CryptoItemViewBinding, val parentContext: Context)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CryptoItem, index: Int) {
            binding.number.text = (index+1).toString()
            binding.name.text = item.name
            binding.updatedValue.text = ("%.2f".format(item.changePercent24Hr.toFloat())) + "%"
            binding.price.text = "$" + ("%.2f".format(item.priceUsd.toFloat()))

            var isLoss = false
            if (item.changePercent24Hr[0] == '-') isLoss = true
            if (isLoss) {
                binding.updatedValue.setTextColor(Color.parseColor("#FF0000"))
                binding.price.setTextColor(Color.parseColor("#FF0000"))
            } else {
                binding.updatedValue.setTextColor(Color.parseColor("#00E600"))
                binding.price.setTextColor(Color.parseColor("#00E600"))
            }
        }
    }
}

