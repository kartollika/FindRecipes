package kartollika.recipesbook.data.models

data class IngredientExtended(
    val id: Int,
    val name: String,
    val image: String,
    val consistency: String,
    val amount: String,
    val unit: String,
    val original: String
)