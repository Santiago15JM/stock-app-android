package com.sjm.stockapp.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.sjm.stockapp.logic.StockApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime

object RetrofitHelper {
    private const val baseUrl = "http://10.0.2.2:8080/"

    fun getInstance(): StockApi {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder().baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StockApi::class.java)
    }
}

private val gson = GsonBuilder()
    .registerTypeAdapter(Instant::class.java, InstantAdapter())
    .create()

class InstantAdapter : JsonDeserializer<Instant>, JsonSerializer<Instant> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Instant {
        return Instant.parse(json.asString)
    }

    override fun serialize(
        src: Instant,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}