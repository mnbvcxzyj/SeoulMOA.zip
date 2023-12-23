package com.mobile.seoulmoa_zip

import BaseActivity
import android.os.Bundle
import com.mobile.seoulmoa_zip.databinding.ActivityDetailBinding


class DetailActivity : BaseActivity() {
    val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    val fileManager: FileManager by lazy {
        FileManager(applicationContext)
    }

    // 이미지 변수
    var img: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

    }
}
//
//        // 이미지 url 전달
//        val url = intent.getStringExtra("url")
//
//        // 이미지 저장
//        detailBinding.btnSave.setOnClickListener {
//            img = "${fileManager.getCurrentTime()}.jpg"
//            fileManager.writeImage(img!!, url.toString())
//        }
//
//        // 파일 불러오기
//        // 위에서 저장한 시간.jpg 파일을 불러와 ImageView 에 표시 (이미지 초기화 후 실행)
//        detailBinding.btnRead.setOnClickListener {
//            Glide.with(applicationContext)
//                .asBitmap()
//                .load(filesDir.toString() + "/${img}" )
//                .into(detailBinding.imgBookCover)
//
//            fileManager.readImage(filesDir.toString() + "/${img}", detailBinding.imgBookCover)
//
//            // fileManager.readImage("image.jpg", detailBinding.imgBookCover)
//        }
//
//        // 이미지 초기화
//        // ImageView 를 초기 이미지로 지정
//        detailBinding.btnInit.setOnClickListener {
//            detailBinding.imgBookCover.setImageResource(R.mipmap.ic_launcher)
//        }
//
//        // 파일 지우기
//        // 저장한 이미지 파일 삭제
//        detailBinding.btnRemove.setOnClickListener {
//            applicationContext.deleteFile(img)
//        }
//    }

