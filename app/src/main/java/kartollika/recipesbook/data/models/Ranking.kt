package kartollika.recipesbook.data.models

enum class Ranking(ranking: Int) {
    MinMissingIngredients(0),
    MaxUsedIngredients(1),
    Relevance(2)
}