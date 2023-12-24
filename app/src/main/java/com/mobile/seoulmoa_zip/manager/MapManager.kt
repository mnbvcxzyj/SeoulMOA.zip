//package com.mobile.seoulmoa_zip.manager
//
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//
//class MapManager(private val onMapReady: (GoogleMap) -> Unit) : OnMapReadyCallback {
//    private lateinit var googleMap: GoogleMap
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        this.googleMap = googleMap
//        onMapReady(this.googleMap)
//        configureMap()
//    }
//
//    private fun configureMap() {
//        googleMap.uiSettings.isZoomControlsEnabled = true
//    }
//
//    fun moveCameraToLocation(location: LatLng, zoomLevel: Float) {
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
//    }
//
//    fun addMarkerAtLocation(location: LatLng, title: String) {
//        googleMap.addMarker(MarkerOptions().position(location).title(title))
//    }
//
//}
//
