package kartollika.recipesbook.data.local.entities

import androidx.room.Entity
import kartollika.recipesbook.data.models.IngredientDetail

@Entity(tableName = "recipe_ingredient_table", primaryKeys = ["id", "recipeId"])
data class RecipeIngredientEntity(
    val id: Int,
    val name: String,
    val original: String,
    val image: String,
    val amount: Double,
    val unit: String,
    val recipeId: Int
)

fun RecipeIngredientEntity.mapToIngredientDetail() =
    IngredientDetail(
        id = this.id,
        name = this.name,
        original = this.original,
        image = this.image,
        amount = this.amount,
        unit = this.unit,
        recipeId = this.recipeId
    )