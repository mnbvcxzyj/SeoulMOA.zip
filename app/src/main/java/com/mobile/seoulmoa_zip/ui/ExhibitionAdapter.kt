package com.mobile.seoulmoa_zip.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.seoulmoa_zip.R
import com.mobile.seoulmoa_zip.data.Exhibition
import com.mobile.seoulmoa_zip.databinding.ListItemBinding

class ExhibitionAdapter : RecyclerView.Adapter<ExhibitionAdapter.ExhibitionHolder>() {
    var exhibitions: List<Exhibition>? = null

    override fun getItemCount(): Int {
        return exhibitions?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitionHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExhibitionHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ExhibitionHolder, position: Int) {
        val context = holder.itemView.context
        val exhibition = exhibitions?.get(position)
        if (exhibition != null) {
            holder.itemBinding.tvTitle.text = exhibition.name
            holder.itemBinding.tvText.text = exhibition.info

            Glide.with(context)
                .load(exhibition.mainImage)
                .into(holder.itemBinding.imageView)
        }
    }


    class ExhibitionHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}

//
//    interface OnItemClickListner {
//        fun onItemClick(view: View, position: Int)
//    }
//    var clickListener: OnItemClickListner? = null
//
//    fun setOnItemClickListener(listener: OnItemClickListner) {
//        this.clickListener = listener
//    }
//}

