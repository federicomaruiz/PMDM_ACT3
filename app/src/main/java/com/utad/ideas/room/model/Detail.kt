package com.utad.ideas.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Detail(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val idIdeas: Int,
    val detailText: String?
)

