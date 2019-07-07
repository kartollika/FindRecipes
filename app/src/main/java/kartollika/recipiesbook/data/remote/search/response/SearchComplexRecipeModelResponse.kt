package kartollika.recipiesbook.data.remote.search.response

import kartollika.recipiesbook.data.models.Recipe

data class SearchComplexRecipeModelResponse(
    val id: Int,
    val usedIngredientCount: Any?,
    val missedIngredientCount: Any?,
    val likes: Int?,
    val title: String,
    val image: String,
    val calories: Int?,
    val protein: String?,
    val fat: String?,
    val carbs: String?
)

fun SearchComplexRecipeModelResponse.mapToRecipeModel() =
    Recipe(
        id = id,
        usedIngredientCount = this.usedIngredientCount,
        missedIngredientCount = this.missedIngredientCount,
        likes = this.likes,
        title = this.title,
        image = this.image
    )
