package kartollika.recipiesbook.data.repository

import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kartollika.recipiesbook.data.remote.extract.ExtractApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesFilterRepository
@Inject constructor(
    private val ingredientsRepository: IngredientsRepository,
    private val extractApi: ExtractApi
) {
    fun getIncludedIngredients() = ingredientsRepository.getIncludedIngredients()

    fun addNewIngredient(ingredientsRawText: String) {
        extractApi.detectFoodInText(ingredientsRawText)
            .subscribeOn(Schedulers.io())
            .flatMap { t ->
                Observable.fromIterable(t.annotations)
                    .filter { it.tag == "ingredient" }
                    .map { it.annotation }
            }
            .subscribeBy(
                onNext = { ingredient ->
                    ingredientsRepository.addIncludedIngredient(
                        ingredient
                    )
                },
                onError = { thr -> thr.printStackTrace() }
            )
    }
}
