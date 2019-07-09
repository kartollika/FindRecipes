package kartollika.recipesbook.features.recipe_detail

import android.os.Bundle
import com.bumptech.glide.Glide
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseActivity
import kotlinx.android.synthetic.main.recipe_detail_layout.*

class RecipeDetailActivity: BaseActivity() {

    override fun getLayoutRes(): Int = R.layout.recipe_detail_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Glide.with(recipe_item_image).load(R.drawable.sushi_tools).into(recipe_item_image)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }


}