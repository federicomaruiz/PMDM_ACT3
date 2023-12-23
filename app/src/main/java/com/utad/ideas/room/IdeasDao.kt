package com.utad.ideas.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.utad.ideas.room.model.Ideas
import kotlinx.coroutines.flow.Flow


// Interactuar con la  BD
// Clase que interactua con las querys para realizar CONSULTAS o insertar datos
@Dao
interface IdeasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIdeaList(ideas: Ideas)

    @Query("UPDATE Ideas SET time = :newTime, priority = :newPriority WHERE id = :ideaId")
    fun updateIdeaTimeAndPriority(ideaId: Int, newTime: String, newPriority: String)

    @Query("SELECT * FROM ideas")
    fun getAllIdeaList(): Flow <List<Ideas>>

    @Query("SELECT * FROM Ideas WHERE id=:listId")
    fun getIdeaListDetail(listId: Int): Ideas


    @Query("DELETE FROM Detail WHERE idIdeas = :ideaId")
    fun deleteDetailsByIdeaId(ideaId: Int)

    @Transaction
    @Delete
    fun deleteIdea(ideas: Ideas)

    /*
    * Cuando elimine una idea se eliminaran todos los detalles referenciados a ella
    * */
    @Transaction
    @Delete
    fun deleteList(ideas: Ideas) {
        deleteDetailsByIdeaId(ideas.id)
        deleteIdea(ideas)
    }


}