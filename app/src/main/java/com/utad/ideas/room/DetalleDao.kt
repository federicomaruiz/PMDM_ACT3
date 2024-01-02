package com.utad.ideas.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utad.ideas.room.model.Detail

@Dao
interface DetalleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetail(detail: Detail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(details: List<Detail>)

    @Query("SELECT * FROM Detail WHERE idIdeas = :ideaId")
    suspend fun getDetailsByIdeaId(ideaId: Int): List<Detail>

    @Update
    suspend fun updateDetail(detail: Detail)

    @Delete
    suspend fun deleteDetail(detail: Detail)


}
