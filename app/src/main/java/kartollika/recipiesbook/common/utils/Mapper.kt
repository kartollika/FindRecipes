package kartollika.recipiesbook.common.utils

fun List<String>.parseToSearchByIngredientsRequest(): RecipeByIngredientRequest {
    return RecipeByIngredientRequest().apply {
        ingredients = this@parseToSearchByIngredientsRequest.joinToString(separator = ",")
    }
}

fun RecipeByIngredientResponse.mapToRecipeModel() =
    Recipe().apply {
        title = this@mapToRecipeModel.title
    }