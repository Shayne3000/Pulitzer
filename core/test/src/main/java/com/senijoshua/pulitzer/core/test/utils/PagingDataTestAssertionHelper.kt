package com.senijoshua.pulitzer.core.test.utils

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.withTimeout

/**
 * Helper file to facilitate validating the values in PagingData<T>.
 * AsyncPagingDataDiffer is the internal delegate for the PagingData's
 * internal Flow and can be extended to collect values from PagingData in tests.
 *
 * See a: https://engineering.theblueground.com/paging-assertions-made-possible/ &
 * b: https://stackoverflow.com/questions/66503911/unit-testing-a-repository-with-
 * paging-3-using-a-a-remote-mediator-and-paging-sou/66686920#66686920 (answer 2)
 * for more info.
 */

/**
 * This tracks and informs its caller of any changes to the paged data set
 * (i.e. insertions, removals etc) via the [onChange] callback.
 */
private class ListUpdateCallbackImpl(
    private val onChange: () -> Unit
) : ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) = onChange()

    override fun onRemoved(position: Int, count: Int) = onChange()

    override fun onMoved(fromPosition: Int, toPosition: Int) = onChange()

    override fun onChanged(position: Int, count: Int, payload: Any?) = onChange()
}

/**
 * This is normally used to differentiate between two items in the data set
 * indicating if a change to said set has occurred i.e. diffing
 * but we use it to denote that each item in the dataset is unique
 */
private class NoDiffCallback<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any) = false
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any) = false
}

/**
 * Collects the values of [PagingData] for assertion in tests.
 * Points to note:
 *  - When [ListUpdateCallbackImpl] notifies of a change to page data, we
 *  treat the collected items as part of the same page and save them while
 *  continuing to collect subsequent pages.
 *  - We keep the sum of retrieved items and trigger the getItem function
 * whenever [ListUpdateCallbackImpl] signals that a new page was inserted.
 */
suspend fun <T : Any> PagingData<T>.collectPagingData(): List<List<T>> {
    val pages: MutableList<List<T>> = mutableListOf()
    var currentPage = mutableListOf<T>()
    val currentPosition = MutableStateFlow(0)

    val updateCallback = ListUpdateCallbackImpl{
        pages.add(currentPage)
        currentPosition.value += currentPage.size
        currentPage = mutableListOf()
    }

    val differ = AsyncPagingDataDiffer<T>(
        diffCallback = NoDiffCallback(),
        updateCallback = updateCallback,
    )

    currentPosition.filter { it > 0 }
        .onEach { differ.getItem(it - 1) }
        .launchIn(TestScope())

    try {
        // We wrap our code in a withTimeout call to
        // prevent the execution from being blocked
        // if something goes wrong whilst collecting
        // subsequent pages
        withTimeout(5) {
            differ.submitData(this@collectPagingData.map {
                currentPage.add(it)
                it
            })

        }
    } catch (e: TimeoutCancellationException) {
        // Ignore exception we just need it in order to stop
        // the underlying implementation blocking the main thread
    }

    return pages
}
