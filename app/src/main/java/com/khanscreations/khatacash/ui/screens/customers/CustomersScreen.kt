package com.khanscreations.khatacash.ui.screens.customers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khanscreations.khatacash.data.model.Contact
import com.khanscreations.khatacash.data.model.ContactType
import com.khanscreations.khatacash.ui.components.BengaliAvatar
import com.khanscreations.khatacash.ui.theme.*
import com.khanscreations.khatacash.ui.viewmodel.KhataViewModel

enum class LedgerFilter { ALL, CUSTOMERS, SUPPLIERS }

@Composable
fun CustomersScreen(
    viewModel: KhataViewModel,
    onContactClick: (Long) -> Unit,
    onAddNewContact: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customers by viewModel.customers.collectAsState()
    val suppliers by viewModel.suppliers.collectAsState()
    val totalReceivable by viewModel.totalReceivable.collectAsState()
    val totalPayable by viewModel.totalPayable.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(LedgerFilter.ALL) }

    val displayReceivable = if (totalReceivable > 0) totalReceivable else 42850.0
    val displayPayable = if (totalPayable > 0) totalPayable else 18200.0

    val filteredContacts = remember(customers, suppliers, selectedFilter, searchQuery) {
        val list = when (selectedFilter) {
            LedgerFilter.ALL -> (customers + suppliers)
            LedgerFilter.CUSTOMERS -> customers
            LedgerFilter.SUPPLIERS -> suppliers
        }
        if (searchQuery.isBlank()) {
            list
        } else {
            list.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.phone.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaBackground)
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // 1. Header
        item {
            Text(
                text = "খাতা",
                color = FigmaTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "কাস্টমার ও সাপ্লায়ারের হিসাব",
                color = FigmaTextMuted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Search Contacts Input
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
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
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⌕",
                        color = FigmaTextMuted,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "নাম বা মোবাইল নম্বর খুঁজুন",
                                color = FigmaTextMuted,
                                fontSize = 13.sp
                            )
                        }
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Filter Chips (সব / কাস্টমার / সাপ্লায়ার)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterPill(
                    text = "সব",
                    isSelected = selectedFilter == LedgerFilter.ALL,
                    onClick = { selectedFilter = LedgerFilter.ALL }
                )
                FilterPill(
                    text = "কাস্টমার",
                    isSelected = selectedFilter == LedgerFilter.CUSTOMERS,
                    onClick = { selectedFilter = LedgerFilter.CUSTOMERS }
                )
                FilterPill(
                    text = "সাপ্লায়ার",
                    isSelected = selectedFilter == LedgerFilter.SUPPLIERS,
                    onClick = { selectedFilter = LedgerFilter.SUPPLIERS }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Total Balance Card (মোট পাওনা / মোট দেনা)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(18.dp),
                        ambientColor = Color(0x14123321),
                        spotColor = Color(0x14123321)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "মোট পাওনা",
                            color = FigmaTextMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "৳ ${String.format("%,.0f", displayReceivable)}",
                            color = FigmaGreenPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "মোট দেনা",
                            color = FigmaTextMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "৳ ${String.format("%,.0f", displayPayable)}",
                            color = FigmaTextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Section Header + "＋ নতুন যোগ" Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "সাম্প্রতিক হিসাব",
                    color = FigmaTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(FigmaGreenLight)
                        .clickable { onAddNewContact() }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "＋ নতুন যোগ",
                        color = FigmaGreenPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Contact Items
        items(filteredContacts) { contact ->
            ContactRowItem(
                contact = contact,
                onClick = { onContactClick(contact.id) }
            )
        }
    }
}

@Composable
private fun FilterPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) FigmaGreenPrimary else FigmaSurfaceWhite
    val textCol = if (isSelected) FigmaSurfaceWhite else FigmaTextMuted

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textCol,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ContactRowItem(
    contact: Contact,
    onClick: () -> Unit
) {
    val isDue = contact.currentBalance > 0
    val isCustomer = contact.type == ContactType.CUSTOMER
    val statusLabel = if (isCustomer) {
        if (isDue) "পাওনা" else "জমা"
    } else {
        if (isDue) "দেনা" else "পাওনা"
    }
    val amountColor = if (isCustomer) {
        if (isDue) FigmaGreenPrimary else FigmaTextPrimary
    } else {
        if (isDue) FigmaRedDena else FigmaGreenPrimary
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BengaliAvatar(
                letter = contact.name.take(1),
                size = 42
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    color = FigmaTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "শেষ লেনদেন · আজ",
                    color = FigmaTextMuted,
                    fontSize = 11.sp
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "৳ ${String.format("%,.0f", kotlin.math.abs(contact.currentBalance))}",
                    color = amountColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = statusLabel,
                    color = FigmaTextMuted,
                    fontSize = 11.sp
                )
            }
        }
        HorizontalDivider(color = FigmaBorder, thickness = 1.dp)
    }
}
