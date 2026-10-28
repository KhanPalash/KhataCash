package com.khanscreations.khatacash.ui.screens.inventory

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.khanscreations.khatacash.data.model.Product
import com.khanscreations.khatacash.ui.theme.*
import com.khanscreations.khatacash.ui.viewmodel.KhataViewModel

@Composable
fun InventoryScreen(
    viewModel: KhataViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val lowStockCount = products.count { it.currentStock <= it.lowStockThreshold }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaBackground)
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "পণ্য ও স্টক",
                        color = FigmaTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "দোকানের মালামাল ও মজুদ হিসাব",
                        color = FigmaTextMuted,
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(FigmaGreenLight)
                        .clickable { showAddDialog = true }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "＋ নতুন পণ্য",
                        color = FigmaGreenPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inventory Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(3.dp, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "মোট পণ্য পদ",
                            color = FigmaTextMuted,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${products.size} টি",
                            color = FigmaTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(3.dp, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = if (lowStockCount > 0) FigmaRedBg else FigmaSurfaceWhite)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "কম স্টকের পণ্য",
                            color = if (lowStockCount > 0) FigmaRedDena else FigmaTextMuted,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$lowStockCount টি",
                            color = if (lowStockCount > 0) FigmaRedDena else FigmaTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "মজুদ পণ্যের তালিকা",
                color = FigmaTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (products.isEmpty()) {
            item {
                Text(
                    text = "কোনো পণ্য যুক্ত করা হয়নি। 'নতুন পণ্য' বোতামে চাপ দিয়ে যোগ করুন।",
                    color = FigmaTextMuted,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        } else {
            items(products) { product ->
                ProductItemRow(
                    product = product,
                    onStockIn = { viewModel.adjustStock(product.id, 1.0) },
                    onStockOut = { viewModel.adjustStock(product.id, -1.0) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, unit, pPrice, sPrice, stock ->
                viewModel.addProduct(name, unit, pPrice, sPrice, stock)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun ProductItemRow(
    product: Product,
    onStockIn: () -> Unit,
    onStockOut: () -> Unit
) {
    val isLowStock = product.currentStock <= product.lowStockThreshold

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .shadow(2.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.name,
                        color = FigmaTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (isLowStock) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(FigmaRedBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "স্টক কম",
                                color = FigmaRedDena,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "বিক্রি: ৳ ${String.format("%,.0f", product.sellingPrice)} · ক্রয়: ৳ ${String.format("%,.0f", product.purchasePrice)}",
                    color = FigmaTextMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = "মজুদ: ${String.format("%.0f", product.currentStock)} ${product.unit}",
                    color = if (isLowStock) FigmaRedDena else FigmaGreenPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Quick Stock In/Out Buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(FigmaGreenSoftBg)
                        .clickable { onStockOut() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "−", color = FigmaTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(FigmaGreenLight)
                        .clickable { onStockIn() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "+", color = FigmaGreenPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, unit: String, purchasePrice: Double, sellingPrice: Double, stock: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("পিস") }
    var purchasePriceText by remember { mutableStateOf("") }
    var sellingPriceText by remember { mutableStateOf("") }
    var stockText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "নতুন পণ্য যোগ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = FigmaTextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("পণ্যের নাম") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("একক (পিস, কেজি, প্যাকেট)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = purchasePriceText,
                        onValueChange = { purchasePriceText = it },
                        label = { Text("ক্রয় মূল্য") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = sellingPriceText,
                        onValueChange = { sellingPriceText = it },
                        label = { Text("বিক্রয় মূল্য") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = stockText,
                    onValueChange = { stockText = it },
                    label = { Text("বর্তমান মজুদ সংখ্যা") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

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
                                    unit,
                                    purchasePriceText.toDoubleOrNull() ?: 0.0,
                                    sellingPriceText.toDoubleOrNull() ?: 0.0,
                                    stockText.toDoubleOrNull() ?: 0.0
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FigmaGreenPrimary)
                    ) {
                        Text("যোগ করুন")
                    }
                }
            }
        }
    }
}
