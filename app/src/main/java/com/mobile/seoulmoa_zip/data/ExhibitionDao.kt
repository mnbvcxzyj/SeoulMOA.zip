package com.mobile.seoulmoa_zip.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExhibitionDao {
    // 특정 전시의 '좋아요' 상태를 업데이트
    @Query("UPDATE exhibitions SET isLiked = :isLiked WHERE id = :exhibitionId")
    suspend fun updateLikedStatus(exhibitionId: Long, isLiked: Boolean)

    // 특정 전시의 '방문' 상태를 업데이트
    @Query("UPDATE exhibitions SET isVisited = :isVisited WHERE id = :exhibitionId")
    suspend fun updateVisitedStatus(exhibitionId: Long, isVisited: Boolean)

    // 새로운 전시를 데이터베이스에 추가하거나 기존의 것을 교체
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExhibition(exhibition: ExhibitionEntity)

    // 특정 전시를 데이터베이스에서 삭제
    @Delete
    suspend fun deleteExhibition(exhibition: ExhibitionEntity)

    // 관심 전시 목록
    @Query("SELECT * FROM exhibitions WHERE isLiked = 1")
    fun getLikedExhibitions(): LiveData<List<ExhibitionEntity>>

    // 특정 전시 반환
    @Query("SELECT * FROM exhibitions WHERE exhibitionNumber = :exhibitionNumber LIMIT 1")
    suspend fun findExhibitionByNumber(exhibitionNumber: String): ExhibitionEntity?

    // 전시의 세부 정보 업데이트
    @Update
    suspend fun updateExhibition(exhibition: ExhibitionEntity)

    // 다녀온 전시 목록
    @Query("SELECT * FROM exhibitions WHERE isVisited = 1")
    fun getVisitedExhibitions(): LiveData<List<ExhibitionEntity>>

    // 별점 업데이트
    @Query("UPDATE exhibitions SET score = :score WHERE id = :exhibitionId")
    suspend fun updateScore(exhibitionId: Long, score: Float)
}
