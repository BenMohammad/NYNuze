package com.benmohammad.nynuze.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.benmohammad.nynuze.data.dao.NewsDao
import com.benmohammad.nynuze.data.entity.News

@Database(entities = [News::class],  version = 1, exportSchema = false)
abstract class NYDatabase: RoomDatabase() {
    abstract fun homeNewsDao(): NewsDao
}