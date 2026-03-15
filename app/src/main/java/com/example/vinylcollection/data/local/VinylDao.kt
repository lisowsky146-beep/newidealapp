package com.example.vinylcollection.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vinylcollection.data.model.VinylRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface VinylDao {
    @Query("SELECT * FROM records ORDER BY createdAt DESC")
    fun getAllRecords(): Flow<List<VinylRecord>>

    @Query("SELECT * FROM records WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavorites(): Flow<List<VinylRecord>>

    @Query("""
        SELECT * FROM records
        WHERE artist LIKE '%' || :query || '%'
           OR album LIKE '%' || :query || '%'
           OR genre LIKE '%' || :query || '%'
           OR barcode LIKE '%' || :query || '%'
           OR notes LIKE '%' || :query || '%'
           OR coverText LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun searchRecords(query: String): Flow<List<VinylRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: VinylRecord)

    @Update
    suspend fun update(record: VinylRecord)

    @Delete
    suspend fun delete(record: VinylRecord)
}
