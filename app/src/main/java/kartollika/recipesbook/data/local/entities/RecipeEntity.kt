package kartollika.recipesbook.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val HOUR = 1000 * 60 * 60

@Entity(tableName = "recipes_table")
data class RecipeEntity(
    @PrimaryKey @ColumnInfo var id: Int,
    var title: String,
    var image: String,
    var cachedUntil: Date = Date(System.currentTimeMillis() + HOUR)
)