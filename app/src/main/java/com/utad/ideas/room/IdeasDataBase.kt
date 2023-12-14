package com.utad.ideas.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.utad.ideas.room.model.Ideas


// Le indico que va ser MI BASE DE DATOS, entre entities tengo que poner todas las tablas de mi BD
@Database(entities = [Ideas::class], version = 1)
abstract class IdeasDataBase : RoomDatabase() {

    abstract fun ideasDao(): IdeasDao // Creo una fun abstracta para vincular el DAO

}