package kartollika.recipesbook.data.remote.extract.response

import kartollika.recipesbook.data.models.FoodItem

data class DetectFoodItemResponse(
    val annotation: String,
    val tag: String,
    val image: String
)

fun DetectFoodItemResponse.mapToFoodItem() =
    FoodItem().also {
        it.name = annotation
        it.tag = tag
        it.image = image
    }