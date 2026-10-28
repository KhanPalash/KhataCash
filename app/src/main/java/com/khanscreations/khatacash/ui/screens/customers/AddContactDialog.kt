package com.khanscreations.khatacash.ui.screens.customers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.khanscreations.khatacash.data.model.ContactType
import com.khanscreations.khatacash.ui.theme.*

@Composable
fun AddContactDialog(
    initialType: ContactType = ContactType.CUSTOMER,
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, address: String?, initialBalance: Double, isCustomer: Boolean) -> Unit
) {
    var isCustomer by remember { mutableStateOf(initialType == ContactType.CUSTOMER) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var initialBalanceText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = if (isCustomer) "নতুন কাস্টমার যোগ" else "নতুন সাপ্লায়ার যোগ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = FigmaTextPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    FilterChip(
                        selected = isCustomer,
                        onClick = { isCustomer = true },
                        label = { Text("কাস্টমার") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = !isCustomer,
                        onClick = { isCustomer = false },
                        label = { Text("সাপ্লায়ার") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("নাম (আবশ্যক)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("মোবাইল নম্বর") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("ঠিকানা / এলাকা (ঐচ্ছিক)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = initialBalanceText,
                    onValueChange = { initialBalanceText = it },
                    label = { Text("পূর্বের বাকি / প্রারম্ভিক হিসাব (৳)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("বাতিল", color = FigmaTextMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onConfirm(
                                    name,
                                    phone,
                                    address.ifBlank { null },
                                    initialBalanceText.toDoubleOrNull() ?: 0.0,
                                    isCustomer
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FigmaGreenPrimary)
                    ) {
                        Text("সংরক্ষণ করুন")
                    }
                }
            }
        }
    }
}
