package com.sjm.stockapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.gson.annotations.SerializedName
import com.sjm.stockapp.logic.models.Stock
import com.sjm.stockapp.screens.home.Home
import com.sjm.stockapp.screens.home.Main
import com.sjm.stockapp.screens.stock_details.StockDetails
import com.sjm.stockapp.ui.theme.StockAppTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockAppTheme {
                val nav = rememberNavController()
                NavHost(nav, Home) {
                    composable<Home> { Main(nav) }
                    composable<Stock> { backStackEntry ->
                        val stock: Stock = backStackEntry.toRoute()
                        StockDetails(stock)
                    }
                }
            }
        }
    }
}
