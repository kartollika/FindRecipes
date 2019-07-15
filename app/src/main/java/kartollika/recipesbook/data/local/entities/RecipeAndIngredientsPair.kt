package kartollika.recipesbook.data.local.entities

import androidx.room.Embedded

class RecipeAndIngredientsPair {

    @Embedded
    lateinit var recipe: RecipeEntity

    @Embedded
    lateinit var ingredientEntity: RecipeIngredientEntity
}
