/*package com.utad.ideas.room.model

import androidx.room.Embedded
import androidx.room.Relation

data class IdeaAndDetail(
    @Embedded
    val ideas: Ideas,
    @Relation(
       parentColumn = "id",
        entityColumn = "ideaId"
    )

    val itemlist: List<Detail>

)
*/