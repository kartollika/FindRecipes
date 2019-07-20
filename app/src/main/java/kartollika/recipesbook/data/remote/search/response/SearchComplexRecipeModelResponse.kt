package kartollika.recipesbook.data.remote.search.response

import kartollika.recipesbook.data.models.RecipePreview

data class SearchComplexRecipeModelResponse(
    val id: Int,
    val usedIngredientCount: Int?,
    val missedIngredientCount: Int?,
    val likes: Int?,
    val title: String,
    val image: String,
    val calories: Int?,
    val protein: String?,
    val fat: String?,
    val carbs: String?,
    val readyInMinutes: Int?
)

fun SearchComplexRecipeModelResponse.mapToRecipeModel() =
    RecipePreview(
        id = id,
        usedIngredientCount = this.usedIngredientCount ?: -1,
        missedIngredientCount = this.missedIngredientCount ?: -1,
        likes = this.likes,
        title = this.title,
        image = this.image,
        readyInMinutes = this.readyInMinutes
    )
