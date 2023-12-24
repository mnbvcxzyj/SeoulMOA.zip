package com.mobile.seoulmoa_zip

import BaseActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.seoulmoa_zip.data.ExhibitionDB
import com.mobile.seoulmoa_zip.data.ExhibitionEntity
import com.mobile.seoulmoa_zip.databinding.ActivityLikeBinding
import com.mobile.seoulmoa_zip.ui.ExhibitionAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikeActivity : BaseActivity() {
    lateinit var likeBinding: ActivityLikeBinding
    lateinit var adapter: ExhibitionAdapter
    private val db by lazy { ExhibitionDB.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        likeBinding = ActivityLikeBinding.inflate(layoutInflater)
        setContentView(likeBinding.root)
        setupToolbar()

        adapter = ExhibitionAdapter(ExhibitionAdapter.TYPE_LIKE)
        likeBinding.rvLike.adapter = adapter
        likeBinding.rvLike.layoutManager = LinearLayoutManager(this)

        adapter.setOnDeleteClickListener(object : ExhibitionAdapter.OnDeleteClickListener {
            override fun onDeleteClick(exhibitionEntity: ExhibitionEntity) {
                CoroutineScope(Dispatchers.IO).launch {
                    val updatedExhibition = exhibitionEntity.apply {
                        isLiked = false
                    }
                    db.exhibitionDao().updateExhibition(updatedExhibition)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LikeActivity, "삭제가 완료되었습니다!.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        likedExhibitions()
    }

    private fun likedExhibitions() {
        val db = ExhibitionDB.getDatabase(this)

        db.exhibitionDao().getLikedExhibitions().observe(this, Observer { exhibitions ->

            adapter.myExhibitions = exhibitions

            adapter.notifyDataSetChanged()
        })
    }

}