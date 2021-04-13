package com.example.localdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDao {

    @Insert
    suspend fun insertItem(entity: Entity) :Long

    @Query("select * from  imagePathTable")
    fun getImagePath(): LiveData<List<Entity>>
}