package kartollika.recipiesbook.data.remote.search.request

import kartollika.recipiesbook.data.models.Ranking

data class SearchRecipesComplexRequest(
    var limitLicence: Boolean = false,
    var offset: Int = 0,
    var number: Int = 10,
    var query: String = "",
    var cousine: String = "",
    var diet: String = "",
    var ranking: Ranking = Ranking.Relevance,
    var includedIngredients: String = "",
    var excludedIngredients: String = "",
    var intoleranceIngredients: String = ""
)