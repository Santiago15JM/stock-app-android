package com.sjm.stockapp.logic.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Stock(
    val ticker: String,
    @SerializedName("target_from")
    val targetFrom: Float,
    @SerializedName("target_to")
    val targetTo: Float,
    val company: String,
    val action: String,
    val brokerage: String,
    @SerializedName("rating_from")
    val ratingFrom: String,
    @SerializedName("rating_to")
    val ratingTo: String,
    val time: String
)
