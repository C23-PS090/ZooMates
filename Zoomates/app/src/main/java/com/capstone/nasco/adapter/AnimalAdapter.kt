package com.capstone.nasco.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.nasco.R
import com.capstone.nasco.model.AnimalResponseItem

class AnimalAdapter(private val listAnimal: List<AnimalResponseItem>?) : RecyclerView.Adapter<AnimalAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.items_animal, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listAnimal?.size ?: 0
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.tvTitle.text = listAnimal?.get(position)?.nama ?: "Fulan"

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listAnimal?.get(holder.adapterPosition))
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AnimalResponseItem?)
    }
}