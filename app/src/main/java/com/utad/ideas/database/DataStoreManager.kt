package com.utad.ideas.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


// variable que va referenciar la DB para luego acceder
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("com.utad.ideas")

object DataStoreManager {
    // con suspend le digo que va actuar en segundo plano corretinas IO
    suspend fun saveUser(context: Context, userName: String, password: String) {

        val userNameKey = stringPreferencesKey("user_name") // variable para guardar los valores
        val passwordKey = stringPreferencesKey("password")

        context.dataStore.edit { editor -> // Llamo al context donde hago referencia a mi BD
            editor[userNameKey] = userName // accedo a las claves y guardo los valores que me llegan
            editor[passwordKey] = password
        }
    }


    // Funcion para recuperar datos

    suspend fun getUser(context: Context): Flow<String> {
        val userNameKey = stringPreferencesKey("user_name") // variable para guardar los valores
        return context.dataStore.data.map { editor ->
            editor[userNameKey] ?: "Es nulo"
        }
    }

    suspend fun getPassword(context: Context): Flow<String> {
        val passwordKey = stringPreferencesKey("password")
        return context.dataStore.data.map { editor ->
            editor[passwordKey] ?: "Es nula la contraseña"

        }
    }

    // "Credenciales" para saber si un usuario esta logeado y que vaya directo a la home
    suspend fun setUserLogged(context: Context, isLogged: Boolean) {
        val userLogged = booleanPreferencesKey("user_logged")
        context.dataStore.edit { editor ->
            editor[userLogged] = isLogged;
        }
    }

    suspend fun getIsUserLogged(context: Context): Flow<Boolean> {
        val userLogged = booleanPreferencesKey("user_logged")
        return context.dataStore.data.map { editor ->
            editor[userLogged] ?: false
        }
    }

    suspend fun deleteLogin(context: Context) {
        setUserLogged(context, false)

    }


    suspend fun deleteUser(context: Context) {
            context.dataStore.edit { editor ->
                //Borrar datos individuales // mirar si borra todo
                editor.clear()
                //editor.remove(stringPreferencesKey("user_name"))
                //editor.remove(stringPreferencesKey("password"))
            }
        }
    }

