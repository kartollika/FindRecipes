package kartollika.recipesbook.data.remote.data.response

import kartollika.recipesbook.data.models.IngredientDetail

data class IngredientResponse(
    val id: Int,
    val aisle: String,
    val image: String,
    val name: String,
    val amount: Double,
    val unit: String,
    val unitShort: String,
    val unitLong: String,
    val originalString: String,
    val metainformation: List<String>
)

fun IngredientResponse.mapToIngredientDetail() =
    IngredientDetail(
        id = this.id,
        name = this.name,
        original = this.originalString,
        amount = this.amount,
        unit = this.unit,
        image = this.image
    )