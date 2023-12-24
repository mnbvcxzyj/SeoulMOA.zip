package com.mobile.seoulmoa_zip.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExhibitionEntity::class], version = 1)
abstract class ExhibitionDB : RoomDatabase() {
    abstract fun exhibitionDao(): ExhibitionDao

    companion object {
        @Volatile       // Main memory 에 저장한 값 사용
        private var INSTANCE: ExhibitionDB? = null

        // INSTANCE 가 null 이 아니면 반환, null 이면 생성하여 반환
        fun getDatabase(context: Context): ExhibitionDB {
            return INSTANCE ?: synchronized(this) {     // 단일 스레드만 접근
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExhibitionDB::class.java, "exhibition_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
