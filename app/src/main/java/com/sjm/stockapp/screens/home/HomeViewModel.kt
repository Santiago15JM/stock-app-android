package com.sjm.stockapp.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjm.stockapp.logic.Api
import com.sjm.stockapp.logic.models.ScoredStock
import com.sjm.stockapp.logic.models.Stock
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var scoredStocks = mutableStateListOf<ScoredStock>()
        private set
    var loadedStocks = mutableStateListOf<Stock>()
        private set
    var loadedPage = 0
        private set
    var endOfList by mutableStateOf(false)
        private set

    var selectedPage by mutableStateOf("RECOMMENDATIONS")

    var searchActive by mutableStateOf(false)
    var searchValue by mutableStateOf("")

    var sortExpanded by mutableStateOf(false)
    var selectedSorting by mutableStateOf(SortOption.TICKER)
    var ascendingSorting by mutableStateOf(true)

    private var requestJob: Job? = null

    var showHelpDialog by mutableStateOf(false)

    init {
        viewModelScope.launch {
            scoredStocks.addAll(Api.getRecommendations().orEmpty())
        }
    }

    fun updateQueriedStocks() {
        requestJob?.cancel()

        requestJob = viewModelScope.launch {
            delay(500)

            val queriedStocks = Api.queryStocks(
                search = if (searchActive) searchValue else "",
                sortingType = selectedSorting,
                ascending = ascendingSorting
            ).orEmpty()

            loadedStocks.clear()
            loadedPage = 0
            endOfList = false
            if (queriedStocks.size < 50) endOfList = true
            loadedStocks.addAll(queriedStocks)
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            val queriedStocks = Api.queryStocks(
                search = searchValue,
                sortingType = selectedSorting,
                ascending = ascendingSorting,
                page = ++loadedPage
            ).orEmpty()
            if (queriedStocks.size < 50) {
                endOfList = true
            } else {
                loadedStocks.addAll(queriedStocks)
            }
        }
    }
}

enum class SortOption(val label: String) {
    TICKER("Ticker"),
    BROKERAGE("Brokerage"),
    TIME("Date")
}
