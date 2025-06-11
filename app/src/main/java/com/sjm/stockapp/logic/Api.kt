package com.sjm.stockapp.logic

import com.sjm.stockapp.config.RetrofitHelper
import com.sjm.stockapp.logic.models.ScoredStock
import com.sjm.stockapp.logic.models.Stock
import com.sjm.stockapp.screens.home.SortOption

object Api {
    private val api = RetrofitHelper.getInstance()

    suspend fun getRecommendations(): List<ScoredStock>? {
        return api.getRecommendations().body()
    }

    suspend fun queryStocks(
        search: String, sortingType: SortOption, ascending: Boolean, page: Int = 0
    ): List<Stock>? {
        return api.getQueryStocks(search, sortingType, ascending, page).body()
    }
}