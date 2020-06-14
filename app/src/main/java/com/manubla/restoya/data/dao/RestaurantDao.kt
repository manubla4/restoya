package com.manubla.restoya.data.dao

import androidx.room.*
import com.manubla.restoya.data.model.Restaurant

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM ${Restaurant.TABLE_NAME} LIMIT :offset, :rows")
    suspend fun getAllWithOffset(offset: Int, rows: Int): List<Restaurant>

    @Query("SELECT COUNT(1) FROM ${Restaurant.TABLE_NAME}")
    suspend fun getRowsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(restaurants: List<Restaurant>)

    @Delete
    suspend fun deleteAll()

}