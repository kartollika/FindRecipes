package kartollika.recipiesbook.data.models

enum class Intolerances(name: String) {
    Dairy("Dairy"),
    Egg("Egg"),
    Gluten("Gluten"),
    Peanut("Peanut"),
    Sesame("Sesame"),
    Seafood("Seafood"),
    Shellfish("Shellfish"),
    Soy("Soy"),
    Sulfite("Sulfite"),
    TreeNut("Tree nut"),
    Wheat("Wheat");

    override fun toString(): String {
        return name
    }

    companion object {
        fun getAll() = listOf(
            Dairy, Egg, Gluten, Peanut, Sesame, Seafood, Shellfish, Soy, Sulfite, TreeNut, Wheat
        )
    }
}