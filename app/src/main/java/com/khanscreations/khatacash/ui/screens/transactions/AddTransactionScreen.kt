package com.khanscreations.khatacash.ui.screens.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khanscreations.khatacash.data.model.Contact
import com.khanscreations.khatacash.data.model.ContactType
import com.khanscreations.khatacash.data.model.TransactionType
import com.khanscreations.khatacash.ui.components.BengaliAvatar
import com.khanscreations.khatacash.ui.theme.*
import com.khanscreations.khatacash.ui.viewmodel.KhataViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: KhataViewModel,
    initialType: TransactionType = TransactionType.SALE_CREDIT,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSaleCredit by remember { mutableStateOf(initialType == TransactionType.SALE_CREDIT || initialType == TransactionType.PAYMENT_RECEIVED) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }
    var amountText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("বাকি") }
    var sendReminder by remember { mutableStateOf(true) }
    var isDropdownOpen by remember { mutableStateOf(false) }

    val customers by viewModel.customers.collectAsState()
    val suppliers by viewModel.suppliers.collectAsState()
    val availableContacts = if (isSaleCredit) customers else suppliers

    val currentDateStr = remember {
        val sdf = SimpleDateFormat("d MMMM yyyy", Locale("bn", "BD"))
        "আজ · ${sdf.format(Date())}"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaBackground)
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
    ) {
        // 1. Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "লেনদেন যোগ করুন",
                        color = FigmaTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "আপনার ব্যবসার খাতায় নতুন হিসাব লিখুন",
                        color = FigmaTextMuted,
                        fontSize = 13.sp
                    )
                }
                IconButton(onClick = onDismiss) {
                    Text(text = "✕", fontSize = 20.sp, color = FigmaTextMuted)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Transaction Type Segmented Switcher (বিক্রি / পাওনা vs কেনা / দেনা)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color(0x14123321),
                        spotColor = Color(0x14123321)
                    ),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(17.5.dp))
                            .background(if (isSaleCredit) FigmaGreenPrimary else Color.Transparent)
                            .clickable {
                                isSaleCredit = true
                                selectedContact = null
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "বিক্রি / পাওনা",
                            color = if (isSaleCredit) FigmaSurfaceWhite else FigmaTextMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(17.5.dp))
                            .background(if (!isSaleCredit) FigmaGreenPrimary else Color.Transparent)
                            .clickable {
                                isSaleCredit = false
                                selectedContact = null
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "কেনা / দেনা",
                            color = if (!isSaleCredit) FigmaSurfaceWhite else FigmaTextMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Contact Selection (কার সঙ্গে লেনদেন?)
            Text(
                text = "কার সঙ্গে লেনদেন?",
                color = FigmaTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clickable { isDropdownOpen = true }
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Color(0x14123321),
                            spotColor = Color(0x14123321)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BengaliAvatar(
                            letter = selectedContact?.name?.take(1) ?: if (isSaleCredit) "ক" else "স",
                            size = 38
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = selectedContact?.name ?: (if (isSaleCredit) "কাস্টমার নির্বাচন করুন" else "সাপ্লায়ার নির্বাচন করুন"),
                            color = if (selectedContact != null) FigmaTextPrimary else FigmaTextMuted,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "⌄",
                            color = FigmaTextMuted,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                DropdownMenu(
                    expanded = isDropdownOpen,
                    onDismissRequest = { isDropdownOpen = false }
                ) {
                    if (availableContacts.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("কোনো কন্টাক্ট পাওয়া যায়নি") },
                            onClick = { isDropdownOpen = false }
                        )
                    } else {
                        availableContacts.forEach { contact ->
                            DropdownMenuItem(
                                text = { Text("${contact.name} (${contact.phone})") },
                                onClick = {
                                    selectedContact = contact
                                    isDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Amount Input (টাকার পরিমাণ)
            Text(
                text = "টাকার পরিমাণ",
                color = FigmaTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(87.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color(0x14123321),
                        spotColor = Color(0x14123321)
                    ),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "৳",
                        color = FigmaGreenPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    TextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        placeholder = {
                            Text(
                                text = "০.০০",
                                fontSize = 28.sp,
                                color = FigmaTextMuted,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaTextPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Description Input (বিবরণ - ঐচ্ছিক)
            Text(
                text = "বিবরণ (ঐচ্ছিক)",
                color = FigmaTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = Color(0x14123321),
                        spotColor = Color(0x14123321)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
            ) {
                TextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = {
                        Text(
                            text = "পণ্য বা লেনদেনের বিবরণ লিখুন",
                            fontSize = 13.sp,
                            color = FigmaTextMuted
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxSize(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6. Date & Payment Method (Two Columns)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "তারিখ",
                        color = FigmaTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .shadow(
                                elevation = 3.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color(0x14123321),
                                spotColor = Color(0x14123321)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = currentDateStr,
                                color = FigmaTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "পেমেন্ট ধরন",
                        color = FigmaTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable {
                                paymentMethod = if (paymentMethod == "বাকি") "নগদ" else "বাকি"
                            }
                            .shadow(
                                elevation = 3.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color(0x14123321),
                                spotColor = Color(0x14123321)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = paymentMethod,
                                color = FigmaTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "⇅",
                                color = FigmaTextMuted,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 7. Reminder Toggle Row
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = FigmaGreenSoftBg)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "পরিশোধের রিমাইন্ডার পাঠাব",
                        color = FigmaTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Switch(
                        checked = sendReminder,
                        onCheckedChange = { sendReminder = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = FigmaGreenPrimary,
                            checkedTrackColor = FigmaGreenLight,
                            uncheckedThumbColor = FigmaTextMuted,
                            uncheckedTrackColor = Color.Transparent
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 8. Save Transaction Button
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (amount > 0 && selectedContact != null) {
                        val contact = selectedContact!!
                        if (isSaleCredit) {
                            viewModel.addSaleCredit(
                                contactId = contact.id,
                                contactName = contact.name,
                                amount = amount,
                                note = noteText.ifBlank { "বাকি বিক্রি" }
                            )
                        } else {
                            viewModel.addPurchaseCredit(
                                contactId = contact.id,
                                contactName = contact.name,
                                amount = amount,
                                note = noteText.ifBlank { "বাকি ক্রয়" }
                            )
                        }
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FigmaGreenPrimary)
            ) {
                Text(
                    text = "হিসাব সংরক্ষণ করুন",
                    color = FigmaSurfaceWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footnote
            Text(
                text = "সংরক্ষণের পর কাস্টমারকে রসিদ পাঠাতে পারবেন",
                color = FigmaTextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
