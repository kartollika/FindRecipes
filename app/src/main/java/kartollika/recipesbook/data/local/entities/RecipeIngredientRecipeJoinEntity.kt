package kartollika.recipesbook.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "recipe_ingredient_recipe_join_table",
    primaryKeys = ["recipeId", "recipeIngredientId"],
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = ["id"],
        childColumns = ["recipeId"]
    ),
        ForeignKey(
            entity = RecipeIngredientEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeIngredientId"]
        )]
)
data class RecipeIngredientRecipeJoinEntity(
    val recipeId: Int,
    val recipeIngredientId: Int
)