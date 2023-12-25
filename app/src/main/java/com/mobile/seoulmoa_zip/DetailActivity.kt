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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.mobile.seoulmoa_zip.data.Exhibition
import com.mobile.seoulmoa_zip.data.ExhibitionDB
import com.mobile.seoulmoa_zip.data.ExhibitionEntity
import com.mobile.seoulmoa_zip.databinding.ActivityDetailBinding
import com.mobile.seoulmoa_zip.databinding.ActivityMainBinding
import com.mobile.seoulmoa_zip.manager.FileManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailActivity : BaseActivity(){
    lateinit var detailBinding: ActivityDetailBinding

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
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(detailBinding.root)

        setupToolbar()

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, R.string.map_key.toString())
        }

        val placesClient = Places.createClient(this)

        geocoder = Geocoder(this) // Geocoder 초기화


        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(mapReadyCallback)
//
//        val exhibition = intent.getSerializableExtra("exhibition") as? Exhibition

        val exhibition = intent.extras?.getSerializable("exhibition") as? Exhibition

        exhibition?.let {
            showExhibitionDetails(it)
            mapFragment.getMapAsync { googleMap ->
                placeMarker(geocoder, it.place, googleMap)
            }
        }

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
                    isVisited = null,
                    score = 0.0f // 점수 초기값 설정
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
                    isLiked = null, // 좋아요 표시
                    isVisited = true,
                    score = 0.0f // 점수 초기값 설정
                )
                saveToDatabase(exhibitionEntity, "메뉴 > 🎨다녀온 전시에 저장 완료!")
            }
        }
    }

    val mapReadyCallback = OnMapReadyCallback { map ->
        googleMap = map
        Log.d(TAG, "GoogleMap is ready")
    }

    fun placeMarker(geocoder: Geocoder, address: String?, googleMap: GoogleMap) {
        if (!address.isNullOrEmpty()) {
            try {
                val addressList = geocoder.getFromLocationName(address, 1)
                if (!addressList.isNullOrEmpty()) {
                    val location = addressList[0]
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.addMarker(MarkerOptions().position(latLng).title("Exhibition").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
                } else {
                    Log.e(TAG, "주소 변환 결과가 없습니다.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Geocoder 실패: ", e)
            }
        } else {
            Log.e(TAG, "주소가 제공되지 않았습니다.")
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
                val existingExhibition =
                    database.exhibitionDao().findExhibitionByNumber(exhibition.exhibitionNumber)

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
                    Toast.makeText(this@DetailActivity, toastMessage, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // 에러 처리
                Log.e(TAG, "Failed to save exhibition", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailActivity, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}