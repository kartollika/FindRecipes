package kartollika.recipiesbook.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients_table")
data class Ingredient(
    @PrimaryKey @ColumnInfo var name: String
)