package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.models

data class ListBlockModel<T>(
    val title: String,
    val list: List<T>
)