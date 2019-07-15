package kartollika.recipesbook.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import kartollika.recipesbook.data.models.IngredientChosenType

@Entity(tableName = "ingredients_table", primaryKeys = ["name", "chosenType"])
data class IngredientEntity(
    @ColumnInfo var name: String,
    var chosenType: IngredientChosenType,
    var isActive: Boolean
)
