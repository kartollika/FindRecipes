package kartollika.recipesbook.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val HOUR = 1000 * 60 * 60

@Entity(tableName = "recipes_table")
data class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val image: String,
    val cachedUntil: Date = Date(System.currentTimeMillis() + HOUR)
)