package com.mobile.seoulmoa_zip

import BaseActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.seoulmoa_zip.databinding.ActivityMainBinding
import com.mobile.seoulmoa_zip.databinding.ActivityVisitedBinding
import com.mobile.seoulmoa_zip.ui.ExhibitionAdapter


class VisitedActivity : BaseActivity(){
    lateinit var visitedBinding: ActivityVisitedBinding
    lateinit var adapter: ExhibitionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visitedBinding = ActivityVisitedBinding.inflate(layoutInflater)
        setContentView(visitedBinding.root)
        setupToolbar()

        adapter = ExhibitionAdapter(ExhibitionAdapter.TYPE_VISITED)
        visitedBinding.rvWent.adapter = adapter
        visitedBinding.rvWent.layoutManager = LinearLayoutManager(this)


//        adapter.setOnItemClickListener(object : ExhibitionAdapter.OnItemClickListner {
//            override fun onItemClick(view: View, position: Int) {
//                val exhibition = adapter.exhibitions?.get(position)
//                val intent = Intent(this@VisitedActivity, VisitedActivity::class.java)
//                intent.putExtra("exhibition", exhibition)
//                startActivity(intent)
//            }
//        })
    }

}