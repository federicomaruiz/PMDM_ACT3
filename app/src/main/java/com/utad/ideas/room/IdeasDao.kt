package com.utad.ideas.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
//import com.utad.ideas.room.model.Detail
//import com.utad.ideas.room.model.IdeaAndDetail
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

    @Query("UPDATE Ideas SET detail = :newDetail WHERE id = :ideaId")
    fun updateDetail(ideaId: Int, newDetail: String)

    @Query("SELECT detail FROM Ideas WHERE id = :ideaId")
    fun getIdeaDetail(ideaId: Int): String?


    @Query("SELECT * FROM ideas")
    fun getAllIdeaList(): List<Ideas>


    @Query("SELECT * FROM Ideas WHERE id=:listId")
    fun getIdeaListDetail(listId: Int): Ideas

    @Delete
    fun deleteList(ideas: Ideas)


}