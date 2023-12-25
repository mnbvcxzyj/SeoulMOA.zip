package com.mobile.seoulmoa_zip.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exhibitions")
data class ExhibitionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val exhibitionNumber: String, // 전시NO
    val sequence: String, // 전시일련번호
    val name: String, // 전시회명
    val place: String?, // 전시장소
    val startDate: String, // 전시시작기간
    val endDate: String, // 전시끝기간
    val artPart: String, // 전시부문
    val info: String, // 전시설명
    val mainImage: String?, // 대표이미지
    val link: String?, // 바로가기링크
    var isLiked: Boolean?,
    var isVisited: Boolean?,
    var score: Float?,
    var memo : String? = "메모를 입력하세요"
)
