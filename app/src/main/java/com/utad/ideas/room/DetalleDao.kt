package com.utad.ideas.room

import DetailWithText
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
    fun insertDetail(detail: Detail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetails(details: List<Detail>)

    @Query("SELECT * FROM Detail WHERE idIdeas = :ideaId")
    fun getDetailsByIdeaId(ideaId: Int): List<Detail>

    @Update
    fun updateDetail(detail: Detail)

    @Delete
    fun deleteDetail(detail: Detail)


}