package kartollika.recipesbook.data.remote.search.response

data class SearchRecipeComplexResponse(
    val results: List<SearchComplexRecipeModelResponse>,
    val baseUrl: String,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)