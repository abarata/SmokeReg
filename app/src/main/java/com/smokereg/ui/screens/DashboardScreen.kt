package com.smokereg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smokereg.R
import com.smokereg.ui.components.StatCard
import com.smokereg.viewmodel.DashboardViewModel
import com.smokereg.viewmodel.DashboardViewModelFactory

/**
 * Dashboard screen showing smoking statistics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModelFactory: DashboardViewModelFactory,
    modifier: Modifier = Modifier
) {
    val viewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
    val dashboardState by viewModel.dashboardState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.statistics),
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(
                    onClick = { viewModel.refreshStatistics() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // Loading State
        if (dashboardState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Statistics Cards
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 80.dp) // Account for bottom nav
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Today
                StatCard(
                    title = stringResource(R.string.today),
                    total = dashboardState.todayTotal,
                    avoidable = dashboardState.todayAvoidable,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )

                // Yesterday
                StatCard(
                    title = stringResource(R.string.yesterday),
                    total = dashboardState.yesterdayTotal,
                    avoidable = dashboardState.yesterdayAvoidable,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )

                // This Week
                StatCard(
                    title = stringResource(R.string.this_week),
                    total = dashboardState.weekTotal,
                    avoidable = dashboardState.weekAvoidable,
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )

                // This Month
                StatCard(
                    title = stringResource(R.string.this_month),
                    total = dashboardState.monthTotal,
                    avoidable = dashboardState.monthAvoidable,
                    backgroundColor = Color(0xFFE8F5E9),
                    contentColor = Color(0xFF1B5E20)
                )

                // This Year
                StatCard(
                    title = stringResource(R.string.this_year),
                    total = dashboardState.yearTotal,
                    avoidable = dashboardState.yearAvoidable,
                    backgroundColor = Color(0xFFFFF3E0),
                    contentColor = Color(0xFFE65100)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Summary Card
                if (dashboardState.todayTotal > 0 || dashboardState.yesterdayTotal > 0) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Insights",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Calculate change from yesterday
                            val change = dashboardState.todayTotal - dashboardState.yesterdayTotal
                            val changeText = when {
                                change > 0 -> "+$change more than yesterday"
                                change < 0 -> "${-change} less than yesterday"
                                else -> "Same as yesterday"
                            }

                            Text(
                                text = changeText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = when {
                                    change > 0 -> Color(0xFFFF6F00)
                                    change < 0 -> Color(0xFF4CAF50)
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )

                            // Avoidable percentage this week
                            if (dashboardState.weekTotal > 0) {
                                val weekAvoidablePercentage =
                                    (dashboardState.weekAvoidable * 100f / dashboardState.weekTotal).toInt()
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$weekAvoidablePercentage% of this week's smokes were avoidable",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}