package kartollika.recipesbook.data.models

import kartollika.recipesbook.data.local.entities.RecipeIngredientEntity

data class IngredientDetail(
    val id: Int,
    val name: String,
    val original: String,
    val image: String,
    val amount: Double,
    val unit: String
)

fun IngredientDetail.mapToRecipeIngredientEntity() =
    RecipeIngredientEntity(
        id = this.id,
        name = this.name,
        original = this.original,
        image = this.image,
        amount = this.amount,
        unit = this.unit
    )