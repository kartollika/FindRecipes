package kartollika.recipiesbook.data.models

data class Ingredient(
    var name: String,
    var chosenType: IngredientChosenType,
    var isActive: Boolean
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