package com.khanscreations.khatacash.ui.screens.customers

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.khanscreations.khatacash.data.model.ContactType
import com.khanscreations.khatacash.data.model.Transaction
import com.khanscreations.khatacash.data.model.TransactionType
import com.khanscreations.khatacash.ui.components.BengaliAvatar
import com.khanscreations.khatacash.ui.theme.*
import com.khanscreations.khatacash.ui.viewmodel.KhataViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CustomerDetailScreen(
    contactId: Long,
    viewModel: KhataViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contact by viewModel.getContactById(contactId).collectAsState(initial = null)
    val transactions by viewModel.getTransactionsForContact(contactId).collectAsState(initial = emptyList())
    val context = LocalContext.current

    var showAddTxDialog by remember { mutableStateOf(false) }
    var dialogTxType by remember { mutableStateOf(TransactionType.SALE_CREDIT) }

    val isCustomer = contact?.type == ContactType.CUSTOMER

    Scaffold(
        topBar = {
            Surface(
                color = FigmaSurfaceWhite,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Text(text = "←", fontSize = 24.sp, color = FigmaTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    BengaliAvatar(
                        letter = contact?.name?.take(1) ?: "ক",
                        size = 38
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = contact?.name ?: "লোড হচ্ছে...",
                            color = FigmaTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = contact?.phone ?: "",
                            color = FigmaTextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Action Buttons Bar
            Surface(
                color = FigmaSurfaceWhite,
                shadowElevation = 10.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            dialogTxType = if (isCustomer) TransactionType.SALE_CREDIT else TransactionType.PURCHASE_CREDIT
                            showAddTxDialog = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FigmaRedDena)
                    ) {
                        Text(
                            text = if (isCustomer) "বাকি দিলাম" else "মাল কিনলাম",
                            color = FigmaSurfaceWhite,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            dialogTxType = if (isCustomer) TransactionType.PAYMENT_RECEIVED else TransactionType.SUPPLIER_PAID
                            showAddTxDialog = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FigmaGreenPrimary)
                    ) {
                        Text(
                            text = if (isCustomer) "টাকা পেলাম" else "টাকা শোধ",
                            color = FigmaSurfaceWhite,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(FigmaBackground)
                .padding(padding)
                .padding(horizontal = 18.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            // Balance Hero Card
            item {
                val balance = contact?.currentBalance ?: 0.0
                val isReceivable = balance >= 0
                val balanceLabel = if (isCustomer) {
                    if (isReceivable) "বর্তমান পাওনা" else "অতিরিক্ত জমা"
                } else {
                    if (isReceivable) "বর্তমান দেনা" else "অগ্রিম পরিশোধ"
                }

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = balanceLabel,
                            color = FigmaTextMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "৳ ${String.format("%,.0f", kotlin.math.abs(balance))}",
                            color = if (isReceivable) FigmaGreenPrimary else FigmaRedDena,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (isCustomer && balance > 0) {
                            Spacer(modifier = Modifier.height(14.dp))
                            // Share Reminder Button
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(FigmaGreenSoftBg)
                                    .clickable {
                                        val message = "আসসালামু আলাইকুম ${contact?.name}, আপনার কাছে মোট বকেয়া পাওনা ৳${String.format("%,.0f", balance)} টাকা। অনুগ্রহ করে সুবিধাজনক সময়ে পরিশোধ করুন। ধন্যবাদ - খাতাক্যাশ।"
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, message)
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "তাগাদা বার্তা পাঠান"))
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "💬 হোয়াটসঅ্যাপ বা মেসেজে তাগাদা পাঠান",
                                    color = FigmaGreenPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "লেনদেনের হিসাব বিবরণী",
                    color = FigmaTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Transaction items
            if (transactions.isEmpty()) {
                item {
                    Text(
                        text = "কোনো লেনদেন পাওয়া যায়নি। নিচের বোতামে চাপ দিয়ে হিসাব যোগ করুন।",
                        color = FigmaTextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            } else {
                items(transactions) { tx ->
                    DetailTransactionRow(tx = tx)
                }
            }
        }
    }

    if (showAddTxDialog && contact != null) {
        AddSingleTxDialog(
            contactName = contact!!.name,
            txType = dialogTxType,
            onDismiss = { showAddTxDialog = false },
            onConfirm = { amount, note ->
                when (dialogTxType) {
                    TransactionType.SALE_CREDIT -> viewModel.addSaleCredit(contact!!.id, contact!!.name, amount, note)
                    TransactionType.PAYMENT_RECEIVED -> viewModel.addPaymentReceived(contact!!.id, contact!!.name, amount, note)
                    TransactionType.PURCHASE_CREDIT -> viewModel.addPurchaseCredit(contact!!.id, contact!!.name, amount, note)
                    TransactionType.SUPPLIER_PAID -> viewModel.addSupplierPaid(contact!!.id, contact!!.name, amount, note)
                    else -> {}
                }
                showAddTxDialog = false
            }
        )
    }
}

@Composable
private fun DetailTransactionRow(tx: Transaction) {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val dateStr = sdf.format(Date(tx.date))
    val isPositive = tx.type == TransactionType.SALE_CREDIT || tx.type == TransactionType.PURCHASE_CREDIT
    val amountColor = if (isPositive) FigmaRedDena else FigmaGreenPrimary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.type.banglaLabel,
                    color = FigmaTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (!tx.note.isNullOrBlank()) {
                    Text(
                        text = tx.note,
                        color = FigmaTextMuted,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = dateStr,
                    color = FigmaTextMuted,
                    fontSize = 10.sp
                )
            }
            Text(
                text = "${if (isPositive) "+" else "-"}৳ ${String.format("%,.0f", tx.amount)}",
                color = amountColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AddSingleTxDialog(
    contactName: String,
    txType: TransactionType,
    onDismiss: () -> Unit,
    onConfirm: (Double, String) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "${txType.banglaLabel} · $contactName",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = FigmaTextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("টাকার পরিমাণ (৳)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("বিবরণ (ঐচ্ছিক)") },
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
                            val amount = amountText.toDoubleOrNull() ?: 0.0
                            if (amount > 0) {
                                onConfirm(amount, note)
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
