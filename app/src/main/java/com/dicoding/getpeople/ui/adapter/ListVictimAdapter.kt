package com.dicoding.getpeople.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.getpeople.data.remote.response.KorbanItem
import com.dicoding.getpeople.databinding.RowVictimBinding

class ListVictimAdapter(private val listKorban : List<KorbanItem>) :
    RecyclerView.Adapter<ListVictimAdapter.ListVictimHolder>() {

    private lateinit var onItemClickCallback : OnItemClickCallback

    inner class ListVictimHolder(var binding : RowVictimBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListVictimHolder {
        val binding = RowVictimBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListVictimHolder(binding)
    }

    override fun onBindViewHolder(holder: ListVictimHolder, position: Int) {
        val korban = listKorban[position]
        Glide.with(holder.itemView.context)
            .load(korban.photoUrl)
            .into(holder.binding.imageviewKorban)
        holder.apply {
            binding.apply {
                valueNama.text = korban.name
                valueLokasi.text = korban.posko
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(
                    listKorban[holder.adapterPosition]
                )
            }
        }
    }

    override fun getItemCount(): Int = listKorban.size

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(korban: KorbanItem)
    }
}