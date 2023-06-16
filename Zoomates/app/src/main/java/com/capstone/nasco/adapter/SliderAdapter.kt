package com.capstone.nasco.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.capstone.nasco.R
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter() : SliderViewAdapter<SliderAdapter.SliderViewHolder>() {
    private val sliderList: IntArray = intArrayOf(R.drawable.singa, R.drawable.monyet)

    class SliderViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {
        var imageView: ImageView? = itemView?.findViewById(R.id.iv_hero)
    }

    override fun getCount(): Int {
        return sliderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        val inflate: View = LayoutInflater.from(parent?.context).inflate(R.layout.items_layout, null)

        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        if (viewHolder != null) {
            viewHolder.imageView?.let {
                Glide.with(viewHolder.itemView)
                    .load(sliderList.get(position))
                    .into(it)
            }
        }
    }
}