package kartollika.recipesbook.data.remote.extract.response

data class DetectFoodResponse(
    val annotations: List<DetectFoodItemResponse>,
    val processedInMs: Long
)