package com.utad.ideas.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utad.ideas.room.model.Ideas


// Interactuar con la  BD
// Clase que interactua con las querys para realizar CONSULTAS o insertar datos
@Dao
interface IdeasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun saveIdeaList(ideas: Ideas)

    @Insert
     fun saveManyIdeaList(ideas: List<Ideas>)

    @Update
    fun updateIdeaList(ideas: Ideas)

    @Query("SELECT * FROM ideas")
    fun getAllIdeaList(): List<Ideas>

    @Query("SELECT * FROM Ideas WHERE id=:listId")
     fun getIdeaListDetail(listId: Int): Ideas

    @Delete
    fun deleteList(ideas: Ideas)
}