package com.utad.ideas.room.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "DetailTable")
data class Detail(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ideaId: Int,
    val detailValue: String,
    /**
     * Con la notación Embedded podemos anidar objetos simples dentro de nuestra tabla
     * */
    @Embedded
    val amount: Amount
)

data class Amount(
    val amount: Int,
    val unit: String
)
