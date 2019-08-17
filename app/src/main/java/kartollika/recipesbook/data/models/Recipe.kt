package kartollika.recipesbook.data.models

import kartollika.recipesbook.data.local.entities.RecipeEntity
import kartollika.recipesbook.data.remote.api.data.response.RecipeResponse
import kartollika.recipesbook.data.remote.api.data.response.mapToIngredientDetail

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val requiredIngredients: List<IngredientDetail> = emptyList(),
    val requiredEquipment: List<Equipment> = emptyList(),
    val cookingTime: Int = 0,
    val pricePerServing: Double = 0.0,
    val totalServings: Int = 0
)

fun RecipeResponse.mapToRecipeModel() =
    Recipe(
        id = this.id,
        title = this.title,
        image = this.image,
        requiredIngredients = this.extendedIngredients.map { it.mapToIngredientDetail() },
        cookingTime = this.readyInMinutes,
        pricePerServing = this.pricePerServing,
        totalServings = this.servings
    )

fun RecipeEntity.mapToRecipeModel() =
    Recipe(
        id = this.id,
        title = this.title,
        image = this.image
    )