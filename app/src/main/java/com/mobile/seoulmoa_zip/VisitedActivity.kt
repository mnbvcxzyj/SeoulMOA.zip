package com.mobile.seoulmoa_zip

import BaseActivity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
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
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class VisitedActivity : BaseActivity(), ExhibitionAdapter.OnCameraListener {
    lateinit var visitedBinding: ActivityVisitedBinding
    lateinit var adapter: ExhibitionAdapter
    private val db by lazy { ExhibitionDB.getDatabase(this) }
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visitedBinding = ActivityVisitedBinding.inflate(layoutInflater)
        setContentView(visitedBinding.root)
        setupToolbar()

        adapter = ExhibitionAdapter(ExhibitionAdapter.TYPE_VISITED).apply {
            setOnCameraListener(this@VisitedActivity)
        }
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



        adapter.setOnModifyListener(object : ExhibitionAdapter.OnModifyListener {
            override fun onModifyChanged(exhibition: ExhibitionEntity) {
                CoroutineScope(Dispatchers.IO).launch {
                    db.exhibitionDao().updateExhibition(exhibition)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@VisitedActivity, "변경사항이 저장되었습니다!", Toast.LENGTH_SHORT)
                            .show()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })

        visitedExhibitions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // 현재 선택된 전시 객체의 사진 경로 업데이트
            currentSelectedExhibition?.let {
                it.imagePath = currentPhotoPath
                CoroutineScope(Dispatchers.IO).launch {
                    db.exhibitionDao().updateExhibition(it)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@VisitedActivity, "사진이 저장되었습니다!", Toast.LENGTH_SHORT)
                            .show()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }


    var currentSelectedExhibition: ExhibitionEntity? = null
    lateinit var currentPhotoPath: String
    var currentPhotoFileName: String? = null

    //    파일명 지정
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File("${storageDir?.path}/${timeStamp}.jpg")

        currentPhotoFileName = file.name
        currentPhotoPath = file.absolutePath
        return file
    }

    private fun visitedExhibitions() {
        val db = ExhibitionDB.getDatabase(this)

        db.exhibitionDao().getVisitedExhibitions().observe(this, Observer { exhibitions ->

            adapter.myExhibitions = exhibitions

            adapter.notifyDataSetChanged()
        })
    }

    override fun onCameraClick(exhibition: ExhibitionEntity) {
        dispatchTakePictureIntent()
        currentSelectedExhibition = exhibition // 현재 선택한 전시를 설정

    }

    // 카메라 호출
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 카메라 앱확인
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // 고화질 저장할 파일 생성
            val photoFile: File? = try {
                createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }

            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this, // context
                    "com.mobile.seoulmoa_zip.fileprovider", // 파일프로바이더 권한
                    photoFile
                )

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }

        }
    }

}