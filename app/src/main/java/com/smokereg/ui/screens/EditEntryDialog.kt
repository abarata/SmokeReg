package com.smokereg.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smokereg.R
import com.smokereg.model.SmokeEntry
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * Dialog for editing an existing smoke entry
 */
@Composable
fun EditEntryDialog(
    entry: SmokeEntry,
    onDismiss: () -> Unit,
    onSave: (SmokeEntry) -> Unit,
    onDelete: () -> Unit
) {
    var isAvoidable by remember { mutableStateOf(entry.isAvoidable) }
    var selectedDateTime by remember {
        mutableStateOf(
            try {
                LocalDateTime.parse(entry.dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } catch (e: Exception) {
                LocalDateTime.now()
            }
        )
    }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_entry)) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Date and Time Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Date & Time",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedDateTime.format(
                                DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Date/Time Picker Button
                OutlinedButton(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        calendar.set(
                            selectedDateTime.year,
                            selectedDateTime.monthValue - 1,
                            selectedDateTime.dayOfMonth,
                            selectedDateTime.hour,
                            selectedDateTime.minute
                        )

                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        selectedDateTime = LocalDateTime.of(
                                            year,
                                            month + 1,
                                            dayOfMonth,
                                            hourOfDay,
                                            minute
                                        )
                                    },
                                    selectedDateTime.hour,
                                    selectedDateTime.minute,
                                    true
                                ).show()
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change Date & Time")
                }

                // Avoidable Checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAvoidable,
                        onCheckedChange = { isAvoidable = it }
                    )
                    Text(
                        text = stringResource(R.string.avoidable_checkbox),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Delete Button
                Divider()
                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Entry")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedEntry = entry.copy(
                        dateTime = selectedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        isAvoidable = isAvoidable
                    )
                    onSave(updatedEntry)
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}