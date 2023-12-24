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
    // 관심 전시 상태 업데이트
    @Query("UPDATE exhibitions SET isLiked = :isLiked WHERE id = :exhibitionId")
    suspend fun updateLikedStatus(exhibitionId: Long, isLiked: Boolean)

    // 다녀온 전시 상태 업데이트
    @Query("UPDATE exhibitions SET isVisited = :isVisited WHERE id = :exhibitionId")
    suspend fun updateVisitedStatus(exhibitionId: Long, isVisited: Boolean)

    // 관심 전시 저장
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExhibition(exhibition: ExhibitionEntity)

    // 관심 전시 삭제
    @Delete
    suspend fun deleteExhibition(exhibition: ExhibitionEntity)

    // 관심 전시 목록 조회
    @Query("SELECT * FROM exhibitions WHERE isLiked = 1")
    fun getLikedExhibitions(): LiveData<List<ExhibitionEntity>>

    @Query("SELECT * FROM exhibitions WHERE exhibitionNumber = :exhibitionNumber LIMIT 1")
    suspend fun findExhibitionByNumber(exhibitionNumber: String): ExhibitionEntity?


    // 다녀온 전시 저장
    @Update
    suspend fun updateExhibition(exhibition: ExhibitionEntity)

    @Update
    suspend fun updateExhibitionScore(exhibition: ExhibitionEntity)

    // 다녀온 전시 목록 조회
    @Query("SELECT * FROM exhibitions WHERE isVisited = 1")
    fun getVisitedExhibitions(): LiveData<List<ExhibitionEntity>>
}
