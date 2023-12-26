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
    @Query("UPDATE exhibitions SET isLiked = :isLiked WHERE id = :exhibitionId")
    suspend fun updateLikedStatus(exhibitionId: Long, isLiked: Boolean)

    @Query("UPDATE exhibitions SET isVisited = :isVisited WHERE id = :exhibitionId")
    suspend fun updateVisitedStatus(exhibitionId: Long, isVisited: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExhibition(exhibition: ExhibitionEntity)

    @Delete
    suspend fun deleteExhibition(exhibition: ExhibitionEntity)

    @Query("SELECT * FROM exhibitions WHERE isLiked = 1")
    fun getLikedExhibitions(): LiveData<List<ExhibitionEntity>>

    @Query("SELECT * FROM exhibitions WHERE exhibitionNumber = :exhibitionNumber LIMIT 1")
    suspend fun findExhibitionByNumber(exhibitionNumber: String): ExhibitionEntity?


    @Update
    suspend fun updateExhibition(exhibition: ExhibitionEntity)

    @Query("SELECT * FROM exhibitions WHERE isVisited = 1")
    fun getVisitedExhibitions(): LiveData<List<ExhibitionEntity>>

    @Query("UPDATE exhibitions SET score = :score WHERE id = :exhibitionId")
    suspend fun updateScore(exhibitionId: Long, score: Float)
}
