package kartollika.recipesbook.data.local.entities

import androidx.room.Relation

data class RecipeAndIngredients(

    val id: Int,

    @Relation(parentColumn = "id", entityColumn = "recipeId")
    val ingredients: List<RecipeIngredientEntity>
)