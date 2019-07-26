package kartollika.recipesbook.common.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {

    private var visibleThreshold = 5
    private var loading: Boolean = false
    private var previousTotal = 0
    private val lock = Object()
    private var totalCount = 0

    abstract fun onLoadMoreItems()

    fun resetScrollingState() {
        previousTotal = 0
        totalCount = 0
        loading = false
    }

    fun getTotal() = totalCount

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val nowVisibleCount = recyclerView.childCount
        totalCount = recyclerView.layoutManager?.itemCount!!
        val firstVisibleItem =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        // Checks dynamically on scroll, if more data was loaded
        if (loading) {
            if (totalCount > previousTotal) {
                loading = false
                previousTotal = totalCount
            }
        }

        if (!loading && (totalCount - nowVisibleCount) <= (firstVisibleItem + visibleThreshold)) {
            onLoadMoreItems()
            loading = true
        }
    }
}