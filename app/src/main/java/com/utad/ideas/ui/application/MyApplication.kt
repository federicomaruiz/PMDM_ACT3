package com.utad.ideas.ui.application

import android.app.Application
import androidx.room.Room
import com.utad.ideas.room.IdeasDataBase

// Le inidico a mi app cual va ser la Base de Datos
// Debo modificar el Manifest android:name = "nombre de mi nueva clase"

class MyApplication : Application() {

    lateinit var dataBase: IdeasDataBase // Creo la variable data base para luego inicializarla con mi BD

    override fun onCreate() {
        super.onCreate()
        dataBase = Room.databaseBuilder(this, IdeasDataBase::class.java, "IdeasDataBase").build() //
    }
}