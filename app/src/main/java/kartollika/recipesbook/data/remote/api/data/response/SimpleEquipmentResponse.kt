package kartollika.recipesbook.data.remote.api.data.response

import kartollika.recipesbook.data.models.Equipment

data class SimpleEquipmentResponse(
    val id: Int = 0,
    val name: String,
    val image: String
)

fun SimpleEquipmentResponse.mapToEquipment() =
    Equipment(
        name = name,
        image = image
    )