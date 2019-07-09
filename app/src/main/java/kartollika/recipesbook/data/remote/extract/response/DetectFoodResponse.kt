package kartollika.recipesbook.data.remote.extract.response

data class DetectFoodResponse(
    val annotations: List<FoodItemResponse>,
    val processedInMs: Long
)