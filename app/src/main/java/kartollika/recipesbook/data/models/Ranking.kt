package kartollika.recipesbook.data.models

import kartollika.recipesbook.R

enum class Ranking(val ranking: Int, val naming: String = "", val icon: Int = -1) {
    MinMissingIngredients(0, "Minimize missing", R.drawable.ic_trending_up_black_24dp),
    MaxUsedIngredients(1, "Maximize used", R.drawable.ic_trending_up_black_24dp),
    Relevance(2, "Relevance", R.drawable.ic_trending_up_black_24dp);

    companion object {
        fun fromRankingValue(ranking: Int): Ranking =
            when (ranking) {
                0 -> MinMissingIngredients
                1 -> MaxUsedIngredients
                2 -> Relevance
                else -> throw IllegalArgumentException()
            }
    }

}