package com.mobile.seoulmoa_zip.network

import com.google.gson.annotations.SerializedName
import com.mobile.seoulmoa_zip.data.Exhibition
import com.mobile.seoulmoa_zip.data.ExhibitionRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IExhibitionService {
    @GET("{apiKey}/{type}/{service}/{startIndex}/{endIndex}")
    fun getExhibitions(
        @Path("apiKey") apiKey: String,
        @Path("type") type: String,
        @Path("service") service: String,
        @Path("startIndex") startIndex: Int,
        @Path("endIndex") endIndex: Int
    ): Call<ExhibitionRoot>
}

