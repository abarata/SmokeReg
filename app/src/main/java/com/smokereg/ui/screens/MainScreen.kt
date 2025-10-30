package com.smokereg.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smokereg.R
import com.smokereg.ui.components.SmokeEntryCard
import com.smokereg.viewmodel.SmokeViewModel
import com.smokereg.viewmodel.SmokeViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * Main screen for smoke registration and viewing today's entries
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModelFactory: SmokeViewModelFactory,
    modifier: Modifier = Modifier
) {
    val viewModel: SmokeViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsState()
    val todaysEntries by viewModel.todaysEntries.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val showEditDialog by viewModel.showEditDialog.collectAsState()
    val showTimeAdjustDialog by viewModel.showTimeAdjustDialog.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val context = LocalContext.current

    // Show toast messages
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.main_title),
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // Date Selector
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous Day Button
                IconButton(onClick = { viewModel.goToPreviousDay() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Day"
                    )
                }

                // Date Display with Calendar Picker
                TextButton(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        calendar.set(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)

                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                viewModel.setSelectedDate(LocalDate.of(year, month + 1, dayOfMonth))
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Today Button
                if (selectedDate != LocalDate.now()) {
                    IconButton(onClick = { viewModel.goToToday() }) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = "Today"
                        )
                    }
                }

                // Next Day Button (only show if not future)
                if (selectedDate < LocalDate.now()) {
                    IconButton(onClick = { viewModel.goToNextDay() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next Day"
                        )
                    }
                } else {
                    // Spacer to maintain layout
                    Box(modifier = Modifier.size(48.dp))
                }
            }
        }

        // Registration Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Avoidable Checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.isAvoidableChecked,
                        onCheckedChange = { viewModel.updateAvoidableCheckbox(it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = stringResource(R.string.avoidable_checkbox),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Register Smoke Button
                    Button(
                        onClick = {
                            viewModel.registerSmoke(uiState.isAvoidableChecked)
                            viewModel.updateAvoidableCheckbox(false) // Reset checkbox
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.register_smoke))
                    }

                    // Adjust Time Button
                    OutlinedButton(
                        onClick = { viewModel.showTimeAdjustDialog() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.adjust_time))
                    }
                }
            }
        }

        // Entries Section
        Text(
            text = if (selectedDate == LocalDate.now()) {
                stringResource(R.string.todays_entries)
            } else {
                "Entries for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Loading State
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        // Error State
        else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        // Empty State
        else if (todaysEntries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_entries_today),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
        // Entries List
        else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp) // Account for bottom nav
            ) {
                items(todaysEntries) { entry ->
                    SmokeEntryCard(
                        entry = entry,
                        onEditClick = { viewModel.showEditDialog(it) }
                    )
                }
            }
        }
    }

    // Edit Dialog
    showEditDialog?.let { entry ->
        EditEntryDialog(
            entry = entry,
            onDismiss = { viewModel.hideEditDialog() },
            onSave = { updatedEntry ->
                viewModel.updateEntry(updatedEntry)
            },
            onDelete = {
                viewModel.deleteEntry(entry.id)
            }
        )
    }

    // Time Adjust Dialog
    if (showTimeAdjustDialog) {
        TimeAdjustDialog(
            isAvoidable = uiState.isAvoidableChecked,
            onDismiss = { viewModel.hideTimeAdjustDialog() },
            onConfirm = { dateTime, isAvoidable ->
                viewModel.registerSmokeWithCustomTime(dateTime, isAvoidable)
                viewModel.updateAvoidableCheckbox(false) // Reset checkbox
            }
        )
    }
}