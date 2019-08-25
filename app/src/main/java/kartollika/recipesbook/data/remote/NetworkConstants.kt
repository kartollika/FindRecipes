package kartollika.recipesbook.data.remote

abstract class NetworkConstants {

    companion object {
        val BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/"

        const val limitLicense_QUERY = "limitLicense"
        const val offset_QUERY = "offset"
        const val number_QUERY = "number"
        const val query_QUERY = "query"
        const val includeIngredients_QUERY = "includeIngredients"
        const val excludeIngredients_QUERY = "excludeIngredients"
        const val intolerances_QUERY = "intolerances"
        const val ranking_QUERY = "ranking"
        const val addRecipeInformation_QUERY = "addRecipeInformation"

        const val IMAGE_BASE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"

    }

    abstract class Search {
        companion object {
            const val recipesByIngredients = "recipes/searchComplex"
        }
    }

    abstract class Extract {
        companion object {
            const val detectFoodInText = "food/detect"
        }
    }

    abstract class Data {
        companion object {
            const val recipeInformation = "recipes/{id}/information"
            const val recipeRequiredEquipment = "recipes/{id}/equipmentWidget.json"
        }
    }
}