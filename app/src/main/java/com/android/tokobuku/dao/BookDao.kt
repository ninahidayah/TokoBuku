package com.android.tokobuku.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.tokobuku.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM 'tire_table' ORDER BY name ASC")
    fun getAllTire(): Flow<List<Book>>

    @Insert
    suspend fun insertTire(tire: Book)

    @Delete
    suspend fun deleteTire(tire: Book)

    @Update
    fun updateTire(tire: Book)
}