package com.mobile.seoulmoa_zip

import BaseActivity
import android.content.ContentValues.TAG
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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
import com.google.gson.Gson
import com.mobile.seoulmoa_zip.data.Exhibition
import com.mobile.seoulmoa_zip.data.ExhibitionDB
import com.mobile.seoulmoa_zip.data.ExhibitionEntity
import com.mobile.seoulmoa_zip.data.NearbySearchResponse
import com.mobile.seoulmoa_zip.databinding.ActivityDetailBinding
import com.mobile.seoulmoa_zip.manager.FileManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class DetailActivity : BaseActivity(){
    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var currentLoc: Location
    private val database by lazy { ExhibitionDB.getDatabase(this) }

    val fileManager: FileManager by lazy {
        FileManager(applicationContext)
    }

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

//        placeMarker(geocoder, exhibition?.place, googleMap)

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


    val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
        }
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

                    fetchNearbyCafes(latLng)

//                    fetchNearbyCafes(latLng)
                } else {
                    detailBinding.tvLocation.text = "주소가 제공되지 않습니다."
                    Toast.makeText(this@DetailActivity, "주소가 제공되지 않습니다.", LENGTH_SHORT)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Geocoder 실패: ", e)
            }
        } else {
            Log.e(TAG, "주소가 제공되지 않았습니다.")
        }
    }

    fun fetchNearbyCafes(location: LatLng) {
        // OkHttpClient 인스턴스 생성
        val client = OkHttpClient()

//        37.56410, 126.9738
        val latitude = location.latitude
//        val latitude =  37.56410
        val longitude = location.longitude
//        val longitude = 126.9738

        val radiusInMeters = 9000

        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "keyword=&location=$latitude,$longitude&radius=1000&type=cafe&key=AIzaSyCCYGzhqOuGMBOE2fH9oHwkmpi1epyPEVA"

        // 요청 생성
        val request = Request.Builder().url(url).build()

        // 비동기적으로 요청 실행
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful && responseData != null) {
                    // 응답 처리
                    runOnUiThread {
                        try {
                            // Gson을 사용하여 JSON 응답 파싱
                            val gson = Gson()
                            val nearbySearchResponse = gson.fromJson(responseData, NearbySearchResponse::class.java)

                            Log.d(TAG, nearbySearchResponse.toString())
                            System.out.println(nearbySearchResponse)
                            // 주변 카페 위치에 마커 추가
                            nearbySearchResponse.results.forEach { result ->
                                val cafeLocation = LatLng(result.geometry.location.lat, result.geometry.location.lng)
                                googleMap.addMarker(MarkerOptions()
                                    .position(cafeLocation)
                                    .title(result.name)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))) // 초록색 마커
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing JSON or adding markers", e)
                        }
                    }
                }
            }
        })
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
