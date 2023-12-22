package com.utad.ideas.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utad.ideas.room.model.Detail
import com.utad.ideas.room.model.Ideas


// Le indico que va ser MI BASE DE DATOS, entre entities tengo que poner todas las tablas de mi BD
@Database(entities = [Ideas::class, Detail::class], version = 1)
@TypeConverters(ImageTypeConverters::class)
abstract class IdeasDataBase : RoomDatabase() {

    abstract fun ideasDao(): IdeasDao // Creo una fun abstracta para vincular el DAO
    abstract fun detailDao(): DetalleDao

}