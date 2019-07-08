package kartollika.recipesbook.data.models

import androidx.room.TypeConverter

enum class IngredientChosenType {
    Included, Excluded, Intolerance;

    companion object Converters {

        @JvmStatic
        @TypeConverter
        fun convertToChosenType(raw: String): IngredientChosenType = valueOf(raw)

        @JvmStatic
        @TypeConverter
        fun convertFromChosenType(chosenType: IngredientChosenType) = chosenType.name
    }
}