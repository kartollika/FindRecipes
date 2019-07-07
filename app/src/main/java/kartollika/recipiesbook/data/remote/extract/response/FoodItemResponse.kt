package kartollika.recipiesbook.data.remote.extract.response

import kartollika.recipiesbook.data.models.FoodItem

data class FoodItemResponse(
    val annotation: String,
    val tag: String,
    val image: String
)

fun FoodItemResponse.mapToFoodItem() =
    FoodItem().also {
        it.name = annotation
        it.tag = tag
        it.image = image
    }