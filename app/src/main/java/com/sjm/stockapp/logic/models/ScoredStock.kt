package com.sjm.stockapp.logic.models

import com.google.gson.annotations.SerializedName

data class ScoredStock(
    @SerializedName("Stock")
    val stock: Stock,
    @SerializedName("Score")
    val score: Float
)
