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
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var scoredStocks = mutableStateListOf<ScoredStock>()
        private set
    var loadedStocks = mutableStateListOf<Stock>()
        private set

    var selectedPage by mutableStateOf("RECOMMENDATIONS")

    var showSearch by mutableStateOf(false)
    var searchValue by mutableStateOf("")

    var sortExpanded by mutableStateOf(false)
    var selectedSorting by mutableStateOf(SortOption.TICKER)
    var ascendingSorting by mutableStateOf(true)

    init {
        viewModelScope.launch {
            scoredStocks.addAll(Api.getRecommendations().orEmpty())
        }
        viewModelScope.launch {
            loadedStocks.addAll(Api.getStocks(0).orEmpty())
        }
    }

    suspend fun updateQueriedStocks() {
        val queriedStocks = Api.queryStocks(
            search = searchValue,
            sortingType = selectedSorting,
            ascending = ascendingSorting
        ).orEmpty()
        loadedStocks.clear()
        loadedStocks.addAll(queriedStocks)
    }
}

enum class SortOption(val label: String) {
    TICKER("Ticker"),
    BROKERAGE("Brokerage"),
    TIME("Date")
}
