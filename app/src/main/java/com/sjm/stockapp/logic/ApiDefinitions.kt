package com.sjm.stockapp.logic

import com.sjm.stockapp.logic.models.ScoredStock
import com.sjm.stockapp.logic.models.Stock
import com.sjm.stockapp.screens.home.SortOption
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("/recommendation")
    suspend fun getRecommendations(): Response<List<ScoredStock>>

    @GET("/query-stocks")
    suspend fun getQueryStocks(
        @Query("search") search: String,
        @Query("sortingType") sortingType: SortOption,
        @Query("ascending") ascending: Boolean,
        @Query("page") page: Int,
        @Query("limit") limit: Int = 50,
    ): Response<List<Stock>>

}