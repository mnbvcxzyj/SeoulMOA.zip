package com.mobile.seoulmoa_zip.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.seoulmoa_zip.R
import com.mobile.seoulmoa_zip.data.Exhibition
import com.mobile.seoulmoa_zip.data.ExhibitionEntity
import com.mobile.seoulmoa_zip.databinding.ListItemBinding
import com.mobile.seoulmoa_zip.databinding.LikelistItemBinding
import com.mobile.seoulmoa_zip.databinding.VisitedlistItemBinding


class ExhibitionAdapter(private val layoutId: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var exhibitions: List<Exhibition>? = null
    var myExhibitions : List<ExhibitionEntity>? = null
    private var clickListener: OnItemClickListener? = null

    companion object {
        val TYPE_MAIN = R.layout.list_item
        val TYPE_LIKE = R.layout.likelist_item
        val TYPE_VISITED = R.layout.visitedlist_item
    }

    override fun getItemCount(): Int {
        return when (layoutId) {
            TYPE_MAIN -> exhibitions?.size ?: 0
            TYPE_LIKE, TYPE_VISITED -> myExhibitions?.size ?: 0
            else -> 0
        }
    }
    override fun getItemViewType(position: Int): Int = layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MAIN -> {
                val itemBinding =
                    ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ExhibitionMainHolder(itemBinding, clickListener)
            }

            TYPE_LIKE -> {
                val likeItemBinding =
                    LikelistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ExhibitionLikeHolder(likeItemBinding, clickListener)
            }

            TYPE_VISITED -> {
                val visitedItemBinding = VisitedlistItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ExhibitionVisitedHolder(visitedItemBinding, clickListener)
            }

            else -> throw IllegalArgumentException("해당하는 뷰가 없습니다!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExhibitionMainHolder -> holder.bind(exhibitions?.get(position), clickListener)
            is ExhibitionLikeHolder -> holder.bind(myExhibitions?.get(position), clickListener)
            is ExhibitionVisitedHolder -> holder.bind(myExhibitions?.get(position), clickListener)
        }
    }
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

}

class ExhibitionMainHolder(
    private val itemBinding: ListItemBinding,
    private val clickListener: ExhibitionAdapter.OnItemClickListener?
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(exhibition: Exhibition?, clickListener: ExhibitionAdapter.OnItemClickListener?) {
        exhibition?.let {
            itemBinding.tvTitle.text = it.name
            itemBinding.tvText.text = it.info

            Glide.with(itemView.context)
                .load(it.mainImage)
                .into(itemBinding.imageView)

            itemBinding.clItem.setOnClickListener {
                clickListener?.onItemClick(it, adapterPosition)
            }
        }
    }
}


class ExhibitionLikeHolder(
    private val itemBinding: LikelistItemBinding,
    private val clickListener: ExhibitionAdapter.OnItemClickListener?
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(exhibition: ExhibitionEntity?, clickListener: ExhibitionAdapter.OnItemClickListener? ) {
        exhibition?.let {
            itemBinding.tvTitle.text = it.name
            itemBinding.tvText.text = it.info

            Glide.with(itemView.context)
                .load(it.mainImage)
                .into(itemBinding.imageView)

        }
    }
}

class ExhibitionVisitedHolder(
    private val itemBinding: VisitedlistItemBinding,
    private val clickListener: ExhibitionAdapter.OnItemClickListener?
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(exhibition: ExhibitionEntity?, clickListener: ExhibitionAdapter.OnItemClickListener?) {
        exhibition?.let {
            itemBinding.tvTitle.text = it.name
            itemBinding.tvText.text = it.info
        }
    }
}



