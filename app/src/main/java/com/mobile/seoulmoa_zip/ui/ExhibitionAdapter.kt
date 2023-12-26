package com.mobile.seoulmoa_zip.ui

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.seoulmoa_zip.R
import com.mobile.seoulmoa_zip.data.Exhibition
import com.mobile.seoulmoa_zip.data.ExhibitionEntity
import com.mobile.seoulmoa_zip.databinding.ListItemBinding
import com.mobile.seoulmoa_zip.databinding.LikelistItemBinding
import com.mobile.seoulmoa_zip.databinding.VisitedlistItemBinding
import java.io.File


class ExhibitionAdapter(private val layoutId: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var exhibitions: List<Exhibition>? = null
    var myExhibitions: List<ExhibitionEntity>? = null
    private var clickListener: OnItemClickListener? = null
    private var deleteListener: OnDeleteClickListener? = null
    private var ratingBarListener: OnRatingBarListener? = null
    private var modifyListener: OnModifyListener? = null
    private var cameraListener: OnCameraListener? = null

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
                ExhibitionMainHolder(itemBinding)
            }

            TYPE_LIKE -> {
                val likeItemBinding =
                    LikelistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ExhibitionLikeHolder(likeItemBinding)
            }

            TYPE_VISITED -> {
                val visitedItemBinding = VisitedlistItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ExhibitionVisitedHolder(visitedItemBinding)
            }

            else -> throw IllegalArgumentException("해당하는 뷰가 없습니다!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExhibitionMainHolder -> holder.bind(exhibitions?.get(position), clickListener)
            is ExhibitionLikeHolder -> holder.bind(myExhibitions?.get(position), deleteListener)
            is ExhibitionVisitedHolder -> modifyListener?.let {
                holder.bind(
                    myExhibitions?.get(position),
                    deleteListener,
                    modifyListener,
                    cameraListener
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(exhibitionEntity: ExhibitionEntity)
    }

    interface OnRatingBarListener {
        fun onRatingChanged(exhibition: ExhibitionEntity, rating: Float)
    }

    interface OnModifyListener {
        //수정
        fun onModifyChanged(exhibition: ExhibitionEntity)
    }

    interface OnCameraListener {
        fun onCameraClick(exhibition: ExhibitionEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        this.deleteListener = listener
    }


    fun setOnModifyListener(listener: OnModifyListener) {
        this.modifyListener = listener
    }

    fun setOnCameraListener(listener: OnCameraListener) {
        this.cameraListener = listener
    }


}

class ExhibitionMainHolder(
    private val itemBinding: ListItemBinding
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
    private val itemBinding: LikelistItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(
        exhibitionEntity: ExhibitionEntity?,
        deleteListener: ExhibitionAdapter.OnDeleteClickListener?
    ) {
        exhibitionEntity?.let { exhibition ->
            itemBinding.tvTitle.text = exhibition.name
            itemBinding.tvText.text = exhibition.info
            Glide.with(itemView.context).load(exhibition.mainImage).into(itemBinding.imageView)

            itemBinding.btnDelete.setOnClickListener {
                deleteListener?.onDeleteClick(exhibition)
            }
        }
    }
}


class ExhibitionVisitedHolder(
    private val itemBinding: VisitedlistItemBinding,
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(
        exhibition: ExhibitionEntity?,
        deleteListener: ExhibitionAdapter.OnDeleteClickListener?,
        modifyListener: ExhibitionAdapter.OnModifyListener?,
        cameraListener: ExhibitionAdapter.OnCameraListener?
    ) {
        exhibition?.let {
            itemBinding.tvTitle.text = it.name
            itemBinding.etMemo.setText(it.memo)
            Glide.with(itemView.context).load(exhibition.mainImage).into(itemBinding.imageView)
            // 현재 별점 보여주기
            itemBinding.ratingBar.rating = it.score ?: 0f
            itemBinding.tvScore.text = it.score.toString()

            itemBinding.btnDelete.setOnClickListener {
                deleteListener?.onDeleteClick(exhibition)
            }

            // 변경된 별점과 메모를 임시로 저장할 변수 (tofh)
            var newRating: Float = exhibition.score ?: 0f

            // 별점 변경 리스너 (tofh)
            itemBinding.ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                    newRating = rating
                }

            itemBinding.btnSave.setOnClickListener {
                val latestMemo = itemBinding.etMemo.text.toString()

                if (exhibition.score != newRating || exhibition.memo != latestMemo) {
                    exhibition.score = newRating
                    exhibition.memo = latestMemo

                    modifyListener?.onModifyChanged(exhibition)
                }
            }


            // 카메라 호출
            itemBinding.imageView.setOnClickListener {
                cameraListener?.onCameraClick(exhibition!!)
            }

            exhibition?.imagePath?.let { path ->
                Glide.with(itemView.context)
                    .load(File(path))
                    .into(itemBinding.imageView)
            }

        }
    }
}

