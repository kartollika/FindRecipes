package kartollika.recipesbook.data.remote.api.extract.response

data class DetectFoodResponse(
    val annotations: List<DetectFoodItemResponse>,
    val processedInMs: Long
)