package kartollika.recipiesbook.data.models

data class Recipe(
    var id: Int = 0,
    var title: String = "",
    var image: String = "",
    var missedIngredientCount: Any?,
    var usedIngredientCount: Any?,
    var likes: Int?
) {
    fun areContentsTheSame(other: Recipe): Boolean {
        return title == other.title && image == other.image
    }
}