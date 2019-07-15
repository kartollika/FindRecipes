package kartollika.recipesbook.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kartollika.recipesbook.data.models.IngredientDetail

@Entity(tableName = "recipe_ingredient_table")
data class RecipeIngredientEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val original: String,
    val image: String,
    val amount: Double,
    val unit: String
)

fun RecipeIngredientEntity.mapToIngredientDetail() =
    IngredientDetail(
        id = this.id,
        name = this.name,
        original = this.original,
        image = this.image,
        amount = this.amount,
        unit = this.unit
    )