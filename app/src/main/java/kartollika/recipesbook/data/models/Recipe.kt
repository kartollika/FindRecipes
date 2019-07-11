package kartollika.recipesbook.data.models

import kartollika.recipesbook.data.local.entities.RecipeEntity
import kartollika.recipesbook.data.remote.data.response.RecipeResponse
import kartollika.recipesbook.data.remote.data.response.mapToIngredientDetail

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val requiredIngredients: List<IngredientDetail> = listOf()
)

fun RecipeResponse.mapToRecipeModel() =
    Recipe(
        id = this.id,
        title = this.title,
        image = this.image,
        requiredIngredients = this.extendedIngredients.map { it.mapToIngredientDetail() }
    )

fun RecipeEntity.mapToRecipeModel() =
    Recipe(
        id = this.id,
        title = this.title,
        image = this.image
    )