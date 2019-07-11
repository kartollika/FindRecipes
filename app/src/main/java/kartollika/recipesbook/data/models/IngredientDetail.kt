package kartollika.recipesbook.data.models

data class IngredientDetail(
    val id: Int,
    val name: String,
    val original: String,
    val image: String,
    val amount: Double,
    val unit: String
)