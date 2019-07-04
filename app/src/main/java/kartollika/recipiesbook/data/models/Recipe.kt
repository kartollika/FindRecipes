package kartollika.recipiesbook.data.models

import android.net.Uri

data class Recipe(
    var id: Int = 0,
    var title: String = "",
    var image: Uri = Uri.EMPTY,
    var missingIngredients: Int = 0,
    var usedIngredientsCount: Int = 0
)