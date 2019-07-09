package kartollika.recipesbook.data.models

data class Ingredient(
    var name: String,
    var chosenType: IngredientChosenType,
    var isActive: Boolean = false
) {
    companion object {
        fun getIntolerances(): List<Ingredient> {
            val intolerances = mutableListOf<Ingredient>()

            for (intolerance in Intolerances.getAll()) {
                intolerances.add(
                    Ingredient(
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