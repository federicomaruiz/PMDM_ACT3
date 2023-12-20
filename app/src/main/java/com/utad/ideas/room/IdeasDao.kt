package com.utad.ideas.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.utad.ideas.room.model.Detail
import com.utad.ideas.room.model.IdeaAndDetail
import com.utad.ideas.room.model.Ideas
import kotlinx.coroutines.flow.Flow


// Interactuar con la  BD
// Clase que interactua con las querys para realizar CONSULTAS o insertar datos
@Dao
interface IdeasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIdeaList(ideas: Ideas)

    @Insert
    fun saveManyIdeaList(ideas: List<Ideas>)

    @Query("UPDATE Ideas SET time = :newTime, priority = :newPriority WHERE id = :ideaId")
    fun updateIdeaTimeAndPriority(ideaId: Int, newTime: String, newPriority: String)


    @Query("SELECT * FROM ideas")
    fun getAllIdeaList(): List<Ideas>


    @Query("SELECT * FROM Ideas WHERE id=:listId")
    fun getIdeaListDetail(listId: Int): Ideas

/*    @Query("SELECT * FROM groceries WHERE id=:listId")
    fun getShoppingListDetail(listId: Int): Flow<Groceries>*/


    @Delete
    fun deleteList(ideas: Ideas)


    /**
     * Si usamos un flow para devolver los datos y tenerlos actualizados,
     * deberemos quitar el prefijo suspend de la función
     */
   /*@Query("SELECT * FROM DetailTable WHERE ideaId = :id")
    fun getListItems(id: Int): Flow<List<Detail>>*/

    /**
     * Si queremos hacer una consulta de dos tablas relacionadas, deberemos marcar esa Query como
     *  @Transaction
     *  e indicar que la función devolverá el objeto de esa relación.
     */
   /* @Transaction
    @Query("SELECT *  FROM Ideas WHERE id=:listId")
    fun getIdeasRelation(listId: Int): Flow<IdeaAndDetail?> */

    //endregion ---- GROCERIES ITEM----*/
}