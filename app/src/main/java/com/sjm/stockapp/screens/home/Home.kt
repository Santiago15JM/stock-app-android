package com.sjm.stockapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.sjm.stockapp.logic.models.Stock
import com.sjm.stockapp.ui.theme.Green
import kotlinx.serialization.Serializable

@Serializable
object Home

@Composable
fun Main(nav: NavController, vm: HomeViewModel = HomeViewModel()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    vm.selectedPage == "RECOMMENDATIONS",
                    label = { Text("Recommended") },
                    icon = { Icon(Icons.Default.Home, "Home") },
                    onClick = { vm.selectedPage = "RECOMMENDATIONS" })
                NavigationBarItem(
                    vm.selectedPage == "ALL",
                    label = { Text("All stocks") },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, "All Stocks") },
                    onClick = { vm.selectedPage = "ALL" })
            }
        }
    ) { innerPadding ->
        when (vm.selectedPage) {
            "RECOMMENDATIONS" -> {
                Recommendations(vm, nav, innerPadding)
            }

            "ALL" -> {
                AllStockScreen(vm, nav, innerPadding)
            }
        }
    }
}

@Composable
fun Recommendations(vm: HomeViewModel, nav: NavController, innerPadding: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(innerPadding)
            .padding(10.dp)
    ) {
        Text(
            "Welcome",
            fontSize = 36.sp,
            fontWeight = FontWeight(600),
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .weight(0.4f)
        )

        Column(Modifier.weight(0.5f)) {
            Text(
                "Trending stocks", fontSize = 18.sp, fontWeight = FontWeight(800)
            )

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text("Ticker")
                Text("Company")
                Text("Est. Imp.")
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(vm.scoredStocks, key = { it.stock.ticker }) {
                    if (it == vm.scoredStocks[0]) {
                        Box(Modifier.height(62.dp)) {
                            Surface(
                                color = Green, shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .zIndex(1f)
                                    .offset(x = 2.dp)
                            ) {
                                Text(
                                    "Best choice",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight(900),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                            Surface(
                                border = BorderStroke(2.dp, Green),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.offset(y = 14.dp)
                            ) {
                                StockItem(it.stock, nav)
                            }
                        }
                    } else StockItem(it.stock, nav)
                    if (it != vm.scoredStocks.last()) HorizontalDivider(Modifier.fillMaxWidth(0.95f))
                }
            }
        }
    }
}

@Composable
fun AllStockScreen(vm: HomeViewModel, nav: NavController, innerPadding: PaddingValues) {
    LaunchedEffect(vm.searchValue, vm.selectedSorting) { vm.updateQueriedStocks() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(innerPadding)
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "All stocks",
                    fontSize = 36.sp,
                    fontWeight = FontWeight(600),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(10.dp)
                )

                Row {
                    Box {
                        AssistChip(
                            onClick = { vm.sortExpanded = true },
                            label = {
                                Text("Sort", fontWeight = FontWeight(700))
                            })
                        DropdownMenu(
                            expanded = vm.sortExpanded,
                            onDismissRequest = { vm.sortExpanded = false }) {
                            SortOption.entries.forEach {
                                DropdownMenuItem(
                                    { Text(it.label) },
                                    onClick = { vm.selectedSorting = it; vm.sortExpanded = false },
                                    modifier =
                                        if (vm.selectedSorting == it) Modifier.background(
                                            MaterialTheme.colorScheme.onPrimary,
                                            RoundedCornerShape(6.dp)
                                        )
                                        else Modifier
                                )
                            }
                            HorizontalDivider(Modifier.fillMaxWidth(0.95f))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 10.dp),
                            ) {
                                Text("Descending", fontSize = 14.sp)
                                Switch(vm.ascendingSorting, { vm.ascendingSorting = it })
                                Text("Ascending")
                            }
                        }
                    }

                    IconButton(onClick = { vm.showSearch = !vm.showSearch }) {
                        Icon(
                            if (vm.showSearch) Icons.Default.Close else Icons.Default.Search,
                            "Search",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

            }

            AnimatedVisibility(vm.showSearch) {
                OutlinedTextField(
                    vm.searchValue,
                    placeholder = { Text("Search ticker, company name or brokerage") },
                    onValueChange = { vm.searchValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.padding(10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Text("Ticker")
            Text("Company")
            Text("Est. Imp.")
        }

        if (vm.loadedStocks.isEmpty())
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) { Text("No stocks found") }
        else
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(vm.loadedStocks, key = { it.ticker }) {
                    StockItem(it, nav)
                    if (it != vm.loadedStocks.last())
                        HorizontalDivider(Modifier.fillMaxWidth(0.95f))
                }
            }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun StockItem(stock: Stock, nav: NavController) {
    val perc = (stock.targetTo - stock.targetFrom) / stock.targetFrom * 100

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { nav.navigate(stock) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stock.ticker,
            fontSize = 22.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier.weight(2f)
        )

        Text(stock.company, modifier = Modifier.weight(4f))

        Text(
            String.format("%.1f", perc) + "%",
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.5f)
        )
    }
}
