package kartollika.recipiesbook.common.utils

import kartollika.recipiesbook.data.remote.recipies.request.RecipeByIngredientRequest

fun List<String>.parseToSearchByIngredientsRequest(): RecipeByIngredientRequest {
    return RecipeByIngredientRequest().apply {
        ingredients = this@parseToSearchByIngredientsRequest.joinToString(separator = ",")
    }
}