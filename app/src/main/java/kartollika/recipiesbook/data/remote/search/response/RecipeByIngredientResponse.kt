package kartollika.recipiesbook.data.remote.search.response

data class RecipeByIngredientResponse(
    var id: Int,
    var title: String,
    var image: String,
    var usedIngredientsCount: Int,
    var missedIngredientsCount: Int,
    var likes: Int
)
