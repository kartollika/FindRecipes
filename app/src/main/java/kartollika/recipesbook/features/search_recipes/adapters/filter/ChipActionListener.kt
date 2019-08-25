package kartollika.recipesbook.features.search_recipes.adapters.filter

interface ChipActionListener {
    fun onCheckedStateChanged(name: String, isChecked: Boolean)
    fun onDeleteAction(name: String)
}