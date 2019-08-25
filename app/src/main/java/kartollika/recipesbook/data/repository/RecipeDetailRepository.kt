package kartollika.recipesbook.data.repository

import io.reactivex.internal.schedulers.IoScheduler
import kartollika.recipesbook.data.local.FavoriteDao
import kartollika.recipesbook.data.local.entities.FavoriteRecipeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeDetailRepository
@Inject constructor(
    private val favoriteDao: FavoriteDao
) {

    fun addRecipeToFavorite(id: Int) =
        favoriteDao.insertFavorite(FavoriteRecipeEntity(id))
            .subscribeOn(IoScheduler())
            .subscribe()

    fun removeRecipeFromFavorite(id: Int) =
        favoriteDao.removeFavorite(id)
            .subscribeOn(IoScheduler())
            .subscribe()

    fun getFavoriteRecipes(number: Int = 15, offset: Int = 0) =
        favoriteDao.getAllFavoriteRecipes(number, offset)
            .subscribeOn(IoScheduler())

    fun isRecipeFavorite(id: Int) =
        favoriteDao.isRecipeFavorite(id)
            .subscribeOn(IoScheduler())
}