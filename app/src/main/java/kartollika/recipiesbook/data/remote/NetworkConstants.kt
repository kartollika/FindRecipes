package kartollika.recipiesbook.data.remote

abstract class NetworkConstants {

    companion object {
        val BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/"
        const val INGREDIENTS_QUERY = "ingredients"
        const val NUMBER_QUERY = "number"
        const val RANKING_QUERY = "ranking"
        const val IGNOREPANTRY_QUERY = "ignorePantry"

    }

    abstract class Recipies {

        companion object {
            const val recipesByIngredients = "recipes/findByIngredients"
        }

    }
}