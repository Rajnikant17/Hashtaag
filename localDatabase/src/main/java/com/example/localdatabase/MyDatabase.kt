package com.example.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entity::class] , version = 1,exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun roomDao() : RoomDao

    companion object {
        const val DATABASE_NAME="hashtag_database"
    }
}