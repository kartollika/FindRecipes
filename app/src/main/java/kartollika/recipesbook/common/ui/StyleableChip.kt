package kartollika.recipesbook.common.ui

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable

class StyleableChip(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : Chip(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context?): this(context, null)

    companion object {
        fun getInstance(context: Context?, style: Int): StyleableChip {
            val chip = StyleableChip(context)
            val chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, style)
            chip.setChipDrawable(chipDrawable)
            return chip
        }
    }
}