package kartollika.recipesbook.data.remote.data.response

data class AnalyzedInstructionStepResponse(
    val number: Int,
    val step: String,
    val ingredients: List<SimpleIngredientResponse>,
    val equipment: List<SimpleEquipmentResponse>,
    val length: InstructionStepLengthResponse
)

