package com.mobile.seoulmoa_zip.data

// JSON 응답에 대한 데이터 클래스
data class NearbySearchResponse(
    val results: List<Result>
)

data class Result(
    val name: String,
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
