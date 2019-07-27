package kartollika.recipesbook.features.settings

enum class ClearCacheState(val message: String = "") {
    Uninitialized, Running, Finished("Clear cache done"), Error("Error occured while clearing cache")
}