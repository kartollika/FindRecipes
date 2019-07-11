package kartollika.recipesbook.data.remote.data.response

data class RecipeResponse(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val extendedIngredients: List<IngredientResponse>,
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val veryHealthy: Boolean,
    val cheap: Boolean,
    val veryPopular: Boolean,
    val sustainable: Boolean,
    val weightWatcherSmartPoints: Int,
    val gaps: String,
    val lowFodmap: Boolean,
    val ketogenic: Boolean,
    val whole30: Boolean,
    val servings: Int,
    val sourceUrl: String,
    val spoonacularSourceUrl: String,
    val aggregateLikes: Int,
    val creditText: String,
    val sourceName: String,
    val instructions: String
)