package com.mobile.seoulmoa_zip.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExhibitionRoot(
    @SerializedName("ListExhibitionOfSeoulMOAInfo") val listExhibitionOfSeoulMOAInfo: ExhibitionList
)

data class ExhibitionList(
    @SerializedName("row") val exhibitions: List<Exhibition>
)

data class Exhibition(
    @SerializedName("DP_EX_NO") val exhibitionNumber: String, // 전시NO
    @SerializedName("DP_SEQ") val sequence: String, //DP_SEQ	전시일련번호
    @SerializedName("DP_NAME") val name: String, // DP_NAME	전시회명
    @SerializedName("DP_PLACE") val place: String?, // DP_PLACE	전시장소
    @SerializedName("DP_START") val startDate: String, // DP_START	전시시작기간
    @SerializedName("DP_END") val endDate: String, // DP_END	전시끝기간
    @SerializedName("DP_ART_PART") val artPart: String, // DP_ART_PART	전시부문
    @SerializedName("DP_INFO") var info: String, // DP_INFO	전시설명
    @SerializedName("DP_MAIN_IMG") val mainImage: String?, // DP_MAIN_IMG	대표이미지
    @SerializedName("DP_LNK") val link: String?, // DP_LNK	바로가기링크
): Serializable
