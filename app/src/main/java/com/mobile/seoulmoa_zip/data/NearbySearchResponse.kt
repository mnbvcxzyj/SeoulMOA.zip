package com.mobile.seoulmoa_zip.data

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
