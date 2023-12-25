package com.mobile.seoulmoa_zip

import BaseActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.seoulmoa_zip.data.ExhibitionDB
import com.mobile.seoulmoa_zip.data.ExhibitionEntity
import com.mobile.seoulmoa_zip.databinding.ActivityVisitedBinding
import com.mobile.seoulmoa_zip.ui.ExhibitionAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VisitedActivity : BaseActivity() {
    lateinit var visitedBinding: ActivityVisitedBinding
    lateinit var adapter: ExhibitionAdapter
    private val db by lazy { ExhibitionDB.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visitedBinding = ActivityVisitedBinding.inflate(layoutInflater)
        setContentView(visitedBinding.root)
        setupToolbar()

        adapter = ExhibitionAdapter(ExhibitionAdapter.TYPE_VISITED)
        visitedBinding.rvVisit.adapter = adapter
        visitedBinding.rvVisit.layoutManager = LinearLayoutManager(this)


        adapter.setOnDeleteClickListener(object : ExhibitionAdapter.OnDeleteClickListener {
            override fun onDeleteClick(exhibitionEntity: ExhibitionEntity) {
                CoroutineScope(Dispatchers.IO).launch {
                    val updatedExhibition = exhibitionEntity.apply {
                        isVisited = false
                    }
                    db.exhibitionDao().updateExhibition(updatedExhibition)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@VisitedActivity, "삭제가 완료되었습니다!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        adapter.setOnRatingBarListener(object : ExhibitionAdapter.OnRatingBarListener {
            override fun onRatingChanged(exhibition: ExhibitionEntity, rating: Float) {
                CoroutineScope(Dispatchers.IO).launch {
                    val updatedExhibition = exhibition.apply {
                        score = rating
                    }
                    db.exhibitionDao().updateExhibition(updatedExhibition)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@VisitedActivity, "별점을 변경하였습니다!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        })

        adapter.setOnModifyListener(object : ExhibitionAdapter.OnModifyListener {


            override fun onModifyChanged(exhibition: ExhibitionEntity) {
                CoroutineScope(Dispatchers.IO).launch {
                    db.exhibitionDao().updateExhibition(exhibition)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@VisitedActivity, "메모가 업데이트 되었습니다!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })


        visitedExhibitions()

    }

    private fun visitedExhibitions() {
        val db = ExhibitionDB.getDatabase(this)

        db.exhibitionDao().getVisitedExhibitions().observe(this, Observer { exhibitions ->

            adapter.myExhibitions = exhibitions

            adapter.notifyDataSetChanged()
        })
    }

}