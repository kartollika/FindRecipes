package kartollika.recipesbook.data.models

import androidx.room.TypeConverter

enum class IngredientChosenType {
    Included, Excluded, Intolerance;
}

class IngredientChosenTypeConverters {

    @TypeConverter
    fun convertToChosenType(raw: String): IngredientChosenType =
        IngredientChosenType.valueOf(raw)

    @TypeConverter
    fun convertFromChosenType(chosenType: IngredientChosenType) = chosenType.name
}