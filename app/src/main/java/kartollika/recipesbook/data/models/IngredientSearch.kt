package kartollika.recipesbook.data.models

data class IngredientSearch(
    var name: String,
    var chosenType: IngredientChosenType,
    var isActive: Boolean = false,
    var isPredefinedIntoleranceActive: Boolean = false
) {

    fun isActiveNormal() = isActive
    fun isActivePredefined() = isPredefinedIntoleranceActive

    companion object {
        fun getIntolerances(): List<IngredientSearch> {
            val intolerances = mutableListOf<IngredientSearch>()

            for (intolerance in Intolerances.getAll()) {
                intolerances.add(
                    IngredientSearch(
                        intolerance.toString(),
                        IngredientChosenType.Intolerance,
                        false
                    )
                )
            }
            return intolerances
        }
    }
}