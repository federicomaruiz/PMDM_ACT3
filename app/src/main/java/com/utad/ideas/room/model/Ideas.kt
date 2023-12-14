package com.utad.ideas.room.model


import androidx.room.Entity
import androidx.room.PrimaryKey

//Tablas que voy a tener dentro de mi BD

@Entity
data class Ideas(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val ideaName: String
)
