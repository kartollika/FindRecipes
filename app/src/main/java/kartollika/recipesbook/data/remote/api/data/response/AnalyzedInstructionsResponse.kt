package kartollika.recipesbook.data.remote.api.data.response

data class AnalyzedInstructionsResponse(
    val name: String,
    val steps: List<AnalyzedInstructionStepResponse>
)