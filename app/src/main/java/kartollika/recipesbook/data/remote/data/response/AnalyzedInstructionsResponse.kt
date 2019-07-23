package kartollika.recipesbook.data.remote.data.response

data class AnalyzedInstructionsResponse(
    val name: String,
    val steps: List<AnalyzedInstructionStepResponse>
)