package kartollika.recipiesbook.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kartollika.recipiesbook.data.models.IngredientChosenType

@Entity(tableName = "ingredients_table")
@TypeConverters(IngredientChosenType::class)
data class Ingredient(
    @PrimaryKey @ColumnInfo var name: String,
    var chosenType: IngredientChosenType,
    var isActive: Boolean
)
