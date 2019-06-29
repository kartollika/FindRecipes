package kartollika.recipiesbook.data.remote.recipies.request

data class RecipeByIngredientRequest(
    var ingredients: String = "",
    var number: Int = 5,

    // Whether to ignore pantry ingredients such as water, salt, flour etc..
    var ignorePantry: Boolean = true
)