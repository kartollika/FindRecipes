package kartollika.recipesbook.data.models

data class RecipePreview(
    var id: Int = 0,
    var title: String = "",
    var image: String = "",
    var missedIngredientCount: Int = -1,
    var usedIngredientCount: Int = -1,
    var likes: Int?,
    var readyInMinutes: Int?
) {
    fun areContentsTheSame(other: RecipePreview): Boolean {
        return title == other.title && image == other.image
    }
}