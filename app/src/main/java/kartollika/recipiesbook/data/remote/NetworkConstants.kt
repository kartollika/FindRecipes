package kartollika.recipiesbook.data.remote

abstract class NetworkConstants {

    companion object {
        val BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/"
    }

    abstract class Recipies {

        companion object {
            const val recipesByIngredients = "recipes/findByIngredients"
        }

    }
}