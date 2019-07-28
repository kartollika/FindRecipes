package kartollika.recipesbook.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import kartollika.recipesbook.data.models.IngredientChosenType
import kartollika.recipesbook.data.models.IngredientSearch

@Entity(tableName = "ingredients_table", primaryKeys = ["name", "chosenType"])
data class IngredientEntity(
    @ColumnInfo var name: String,
    var chosenType: IngredientChosenType,
    var isActive: Boolean,
    var isPredefinedActive: Boolean = false
)

fun IngredientEntity.mapToIngredientSearchModel() =
    IngredientSearch(
        name = this.name,
        chosenType = this.chosenType,
        isActive = this.isActive,
        isPredefinedIntoleranceActive = this.isPredefinedActive
    )