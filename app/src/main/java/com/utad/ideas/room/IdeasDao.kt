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
    suspend fun saveIdeaList(ideas: Ideas)

    @Query("UPDATE Ideas SET time = :newTime, priority = :newPriority WHERE id = :ideaId")
    suspend fun updateIdeaTimeAndPriority(ideaId: Int, newTime: String, newPriority: String)

    @Query("UPDATE Ideas SET time = :newTime WHERE id = :ideaId")
    fun updateIdeaTime(ideaId: Int, newTime: String)

    @Query("UPDATE Ideas SET priority = :newPriority WHERE id = :ideaId")
    suspend fun updateIdeaPriority(ideaId: Int, newPriority: String)

    @Query("SELECT * FROM ideas")
    fun getAllIdeaList(): Flow<List<Ideas>>

    @Query("SELECT * FROM Ideas WHERE id=:listId")
    suspend fun getIdeaListDetail(listId: Int): Ideas


    @Query("DELETE FROM Detail WHERE idIdeas = :ideaId")
    suspend fun deleteDetailsByIdeaId(ideaId: Int)

    @Transaction
    @Delete
    suspend fun deleteIdea(ideas: Ideas)

    /*
    * Cuando elimine una idea se eliminaran todos los detalles referenciados a ella
    * */
    @Transaction
    @Delete
    suspend fun deleteList(ideas: Ideas) {
        deleteDetailsByIdeaId(ideas.id)
        deleteIdea(ideas)
    }


}
