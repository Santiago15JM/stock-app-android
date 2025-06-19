package com.sjm.stockapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sjm.stockapp.R
import com.sjm.stockapp.logic.models.Stock
import com.sjm.stockapp.ui.theme.Bearish
import com.sjm.stockapp.ui.theme.Bullish
import com.sjm.stockapp.ui.theme.Green
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Serializable
object Home

@Composable
fun Main(nav: NavController, vm: HomeViewModel = viewModel()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(), bottomBar = {
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
        }) { innerPadding ->
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
            .fillMaxSize()
    ) {
        if (vm.showHelpDialog) HelpDialog { vm.showHelpDialog = false }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Welcome",
                fontSize = 36.sp,
                fontWeight = FontWeight(600),
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(10.dp)
            )

            IconButton({ vm.showHelpDialog = true }) {
                Icon(
                    Icons.Default.Info, "Recommendations help button"
                )
            }
        }

        Column {
            Text(
                "Trending stocks",
                fontSize = 18.sp,
                fontWeight = FontWeight(800),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Headers()

            if (vm.scoredStocks.isEmpty()) Text("Theres no current recommendations")
            else LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(vm.scoredStocks, key = { it.stock.ticker }) {
                    if (it == vm.scoredStocks.first()) {
                        BestStock(it.stock) { nav.navigate(it.stock) }
                    } else {
                        StockItem(it.stock) { nav.navigate(it.stock) }
                    }

                    if (it != vm.scoredStocks.last()) {
                        HorizontalDivider(
                            Modifier
                                .fillMaxWidth(0.95f)
                                .height(0.dp)
                                .offset(y = (5).dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AllStockScreen(vm: HomeViewModel, nav: NavController, innerPadding: PaddingValues) {
    val listState = rememberLazyListState()

    LaunchedEffect(
        vm.searchValue, vm.selectedSorting, vm.ascendingSorting, vm.searchActive
    ) {
        vm.updateQueriedStocks()
        listState.scrollToItem(0)
    }

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
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Row {
                    Box {
                        AssistChip(onClick = { vm.sortExpanded = true }, label = {
                            Text("Sort", fontWeight = FontWeight(700))
                        })
                        DropdownMenu(
                            expanded = vm.sortExpanded,
                            onDismissRequest = { vm.sortExpanded = false }) {
                            SortOption.entries.forEach {
                                DropdownMenuItem(
                                    { Text(it.label) },
                                    onClick = { vm.selectedSorting = it; vm.sortExpanded = false },
                                    modifier = if (vm.selectedSorting == it) Modifier.background(
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

                    IconButton(onClick = { vm.searchActive = !vm.searchActive }) {
                        Icon(
                            if (vm.searchActive) Icons.Default.Close else Icons.Default.Search,
                            "Search",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

            }

            AnimatedVisibility(
                vm.searchActive,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 300),
                    expandFrom = Alignment.Top
                ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 300),
                    shrinkTowards = Alignment.Top
                )
            ) {
                OutlinedTextField(
                    vm.searchValue,
                    placeholder = { Text("Search ticker, company name or brokerage") },
                    onValueChange = { vm.searchValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.padding(5.dp))

        Headers()

        if (vm.loadedStocks.isEmpty()) Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) { Text("No stocks found") }
        else LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            items(vm.loadedStocks, key = { it.ticker }) {
                StockItem(it) { nav.navigate(it) }

                if (it != vm.loadedStocks.last()) {
                    HorizontalDivider(
                        Modifier
                            .fillMaxWidth(0.95f)
                            .height(0.dp)
                            .offset(y = 3.dp)
                    )
                }
            }
            item {
                if (vm.endOfList) Text("You reached the end of the list")
                else Button({ vm.loadMore() }) { Text("Load more") }
            }
        }
    }
}

@Composable
fun Headers() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
    ) {
        Text("Ticker")
        Text("Company & brokerage")
        Text("Forecast")
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun StockItem(stock: Stock, onClick: () -> Unit) {
    val perc = (stock.targetTo - stock.targetFrom) / stock.targetFrom * 100
    val formDate = OffsetDateTime.parse(stock.time).format(DateTimeFormatter.ofPattern("dd/MM/yy"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                stock.ticker,
                fontSize = 22.sp,
                fontWeight = FontWeight(700),
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(3f)
        ) {
            Text(
                stock.company,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            Text(
                stock.brokerage, fontStyle = FontStyle.Italic, fontSize = 14.sp
            )
        }

        Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
            Text(
                String.format("%.1f", perc) + "%",
                textAlign = TextAlign.End,
                color = when {
                    perc < 0f -> Bearish
                    perc > 0f -> Bullish
                    else -> Color.Gray
                },
            )

            Text(formDate, fontSize = 14.sp)
        }
    }
}

@Composable
fun BestStock(stock: Stock, onClick: () -> Unit) {
    Box(Modifier.padding(top = 11.dp)) {
        Surface(
            color = Green,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .zIndex(1f)
                .offset(x = 10.dp, y = (-11).dp)
        ) {
            Text(
                "Best choice",
                fontSize = 10.sp,
                fontWeight = FontWeight(900),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        Surface(
            border = BorderStroke(3.dp, Green),
            shape = RoundedCornerShape(12.dp),
        ) {
            StockItem(stock, onClick)
        }
    }
}

@Composable
fun HelpDialog(
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest, properties = DialogProperties()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    "How are recommendations chosen?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    textAlign = TextAlign.Center
                )
                Text(
                    stringResource(R.string.recommendation_help_text), textAlign = TextAlign.Center
                )
                Button(onDismissRequest) { Text("OK") }
            }
        }
    }
}
