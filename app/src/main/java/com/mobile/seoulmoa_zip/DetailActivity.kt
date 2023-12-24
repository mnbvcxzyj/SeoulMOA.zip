package com.mobile.seoulmoa_zip

import BaseActivity
import android.content.ContentValues.TAG
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.mobile.seoulmoa_zip.data.Exhibition
import com.mobile.seoulmoa_zip.data.ExhibitionDB
import com.mobile.seoulmoa_zip.data.ExhibitionEntity
import com.mobile.seoulmoa_zip.databinding.ActivityDetailBinding
import com.mobile.seoulmoa_zip.manager.FileManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailActivity : BaseActivity() {
    val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    val fileManager: FileManager by lazy {
        FileManager(applicationContext)
    }

    private val database by lazy { ExhibitionDB.getDatabase(this) }


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var currentLoc: Location

    // 지도 저장 하는 멤버 변수
    private lateinit var googleMap: GoogleMap
    var centerMarker: Marker? = null


    // 이미지 변수
    var img: String? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)
        setupToolbar()

        // 이미지 url 전달
        val url = intent.getStringExtra("url")

//
//        val mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//
//        mapFragment.getMapAsync(mapReadyCallback)

        val exhibition = intent.getSerializableExtra("exhibition") as? Exhibition
        exhibition?.let { showExhibitionDetails(it) }

// 좋아요 버튼 클릭 리스너
        detailBinding.btnLike.setOnClickListener {
            exhibition?.let {
                val exhibitionEntity = ExhibitionEntity(
                    exhibitionNumber = it.exhibitionNumber,
                    sequence = it.sequence,
                    place = it.place,
                    mainImage = it.mainImage,
                    link = it.link,
                    name = it.name,
                    startDate = it.startDate,
                    endDate = it.endDate,
                    artPart = it.artPart,
                    info = it.info,
                    isLiked = true, // 좋아요 표시
                    isVisited = false,
                    score = 0 // 점수 초기값 설정
                )
                saveToDatabase(exhibitionEntity, "메뉴 > ❤️관심 전시에 저장 완료!")
            }
        }

// 다녀온 전시 버튼 클릭 리스너
        detailBinding.btnSave.setOnClickListener {
            exhibition?.let {
                val exhibitionEntity = ExhibitionEntity(
                    exhibitionNumber = it.exhibitionNumber,
                    sequence = it.sequence,
                    place = it.place,
                    mainImage = it.mainImage,
                    link = it.link,
                    name = it.name,
                    startDate = it.startDate,
                    endDate = it.endDate,
                    artPart = it.artPart,
                    info = it.info,
                    isLiked = false, // 좋아요 표시
                    isVisited = true,
                    score = 0 // 점수 초기값 설정
                )
                saveToDatabase(exhibitionEntity, "메뉴 > 🎨다녀온 전시에 저장 완료!")
            }
        }



        /*GoogleMap 로딩이 완료될 경우 실행하는 Callback*/
        val mapReadyCallback = OnMapReadyCallback { map ->
            googleMap = map
            Log.d(TAG, "Googlemap ready")

            // 마커 클릭 이벤트 처리
            googleMap.setOnMarkerClickListener { marker ->
                Toast.makeText(applicationContext, marker.tag.toString(), Toast.LENGTH_SHORT)
                    .show()
                false // true일 경우 이벤트처리 종료이므로 info window 미출력
            }
            // 마커 InfoWindow 클릭 이벤트 처리
            googleMap.setOnInfoWindowClickListener { marker ->
                Toast.makeText(applicationContext, marker.title, Toast.LENGTH_SHORT).show()
            }

            // 지도 특정 지점 클릭 이벤트 처리
            googleMap.setOnMapClickListener { latLng: LatLng ->
                Toast.makeText(
                    applicationContext,
                    latLng.toString(), Toast.LENGTH_SHORT
                ).show()
            }

            // 지도 특정 지점 롱클릭 이벤트 처리
            googleMap.setOnMapLongClickListener { latLng: LatLng ->
                Toast.makeText(applicationContext, latLng.toString(), Toast.LENGTH_SHORT).show()
            }

            //            polylineOptions = PolylineOptions()
        }


    }

    private fun showExhibitionDetails(exhibition: Exhibition) {
        detailBinding.tvTitle.text = exhibition.name
        detailBinding.tvLocation.text = exhibition.place ?: "정보 없음"
        detailBinding.tvDate.text = "${exhibition.startDate} ~ ${exhibition.endDate}"
        detailBinding.tvLink.text = exhibition.link
        detailBinding.tvInfo.text = exhibition.info

        exhibition.mainImage?.let { imageUrl ->
            Glide.with(this).load(imageUrl).into(detailBinding.ivImg)
        }
    }

    private fun saveToDatabase(exhibition: ExhibitionEntity, toastMessage: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existingExhibition = database.exhibitionDao().findExhibitionByNumber(exhibition.exhibitionNumber)

                if (existingExhibition != null) {
                    val updatedExhibition = existingExhibition.apply {
                        isLiked = exhibition.isLiked ?: existingExhibition.isLiked
                        isVisited = exhibition.isVisited ?: existingExhibition.isVisited
                        score = exhibition.score ?: existingExhibition.score
                    }
                    database.exhibitionDao().updateExhibition(updatedExhibition)
                } else {
                    database.exhibitionDao().insertExhibition(exhibition)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailActivity, toastMessage, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // 에러 처리
                Log.e(TAG, "Failed to save exhibition", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailActivity, "저장에 실패했습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }





}
