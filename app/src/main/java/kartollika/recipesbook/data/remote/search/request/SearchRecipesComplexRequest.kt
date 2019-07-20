package kartollika.recipesbook.data.remote.search.request

data class SearchRecipesComplexRequest(
    var limitLicence: Boolean = false,
    var offset: Int = 0,
    var number: Int = 10,
    var query: String = "",
    var cousine: String = "",
    var diet: String = "",
    var ranking: Int = 2,
    var includedIngredients: String = "",
    var excludedIngredients: String = "",
    var intoleranceIngredients: String = "",
    var addRecipeInformation: Boolean = true
)