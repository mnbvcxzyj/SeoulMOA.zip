package com.mobile.seoulmoa_zip

import BaseActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.seoulmoa_zip.data.ExhibitionDB
import com.mobile.seoulmoa_zip.databinding.ActivityLikeBinding
import com.mobile.seoulmoa_zip.databinding.ActivityVisitedBinding
import com.mobile.seoulmoa_zip.ui.ExhibitionAdapter

class LikeActivity : BaseActivity() {
    lateinit var likeBinding: ActivityLikeBinding
    lateinit var adapter: ExhibitionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        likeBinding = ActivityLikeBinding.inflate(layoutInflater)
        setContentView(likeBinding.root)
        setupToolbar()

        adapter = ExhibitionAdapter(ExhibitionAdapter.TYPE_LIKE)
        likeBinding.rvLike.adapter = adapter
        likeBinding.rvLike.layoutManager = LinearLayoutManager(this)

        likedExhibitions()

//        adapter.setOnItemClickListener(object : ExhibitionAdapter.OnItemClickListner {
//            override fun onItemClick(view: View, position: Int) {
//                val exhibition = adapter.exhibitions?.get(position)
//                val intent = Intent(this@VisitedActivity, VisitedActivity::class.java)
//                intent.putExtra("exhibition", exhibition)
//                startActivity(intent)
//            }
//        })
    }

    private fun likedExhibitions() {
        val db = ExhibitionDB.getDatabase(this)

        db.exhibitionDao().getLikedExhibitions().observe(this, Observer { exhibitions ->

            adapter.myExhibitions = exhibitions

            adapter.notifyDataSetChanged()
        })
    }

}