package com.utad.ideas.room.model


import android.graphics.Bitmap
import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Tablas que voy a tener dentro de mi BD

@Entity
data class Ideas(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ideaName: String,
    val description: String,
    val time: String,
    val priority: String,
    val detail: String?,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB )
    val image: Bitmap

)
