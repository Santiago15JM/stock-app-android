package com.sjm.stockapp.screens.stock_details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sjm.stockapp.logic.models.Stock
import com.sjm.stockapp.ui.theme.Green
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun StockDetails(stock: Stock) {
    val date =
        OffsetDateTime.parse(stock.time).format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
        ) {
            Surface(
                shape = CircleShape,
                shadowElevation = 8.dp,
                border = BorderStroke(5.dp, Green),
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stock.ticker,
                        fontSize = 32.sp,
                        fontWeight = FontWeight(800),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(50.dp))

            HorizontalDivider()
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(fontSize = 20.sp)
            ) {
                Detail("Ticker", stock.ticker)
                Detail("Company", stock.company)
                Detail("Action", stock.action)
                Detail("Brokerage", stock.brokerage)
                Detail("Previous rating", stock.ratingFrom)
                Detail("Current rating", stock.ratingTo)
                Detail("Previous target price", "${stock.targetFrom}")
                Detail("Current target price", "${stock.targetTo}")
                Detail("Issue date", date)
            }
            HorizontalDivider()
        }
    }
}

@Composable
fun Detail(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("$label:", fontWeight = FontWeight(600))
        Text(
            text = value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            textAlign = TextAlign.End
        )
    }
}