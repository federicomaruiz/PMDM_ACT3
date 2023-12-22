import androidx.room.Embedded
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Relation
import com.utad.ideas.room.model.Detail
import com.utad.ideas.room.model.Ideas

data class IdeasWithDetails(
    @Embedded
    val ideas: Ideas,

    @Relation(
        parentColumn = "id",
        entityColumn = "idIdeas"
    )
    val details: List<DetailWithText>
)

data class DetailWithText(
    @Embedded
    val detail: Detail,

    val detailText: List<String>
)
