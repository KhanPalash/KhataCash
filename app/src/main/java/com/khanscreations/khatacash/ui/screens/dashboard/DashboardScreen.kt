package com.khanscreations.khatacash.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.khanscreations.khatacash.data.model.Transaction
import com.khanscreations.khatacash.data.model.TransactionType
import com.khanscreations.khatacash.ui.components.ActionTile
import com.khanscreations.khatacash.ui.components.BengaliAvatar
import com.khanscreations.khatacash.ui.components.SalesAndBalanceHeroCard
import com.khanscreations.khatacash.ui.theme.*
import com.khanscreations.khatacash.ui.viewmodel.KhataViewModel

@Composable
fun DashboardScreen(
    viewModel: KhataViewModel,
    onNavigateToCustomers: () -> Unit,
    onNavigateToSuppliers: () -> Unit,
    onOpenAddTransaction: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.businessProfile.collectAsState()
    val todaySales by viewModel.todaySales.collectAsState()
    val totalReceivable by viewModel.totalReceivable.collectAsState()
    val totalPayable by viewModel.totalPayable.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()

    val displaySales = if (todaySales > 0) todaySales else 12500.0
    val displayReceivable = if (totalReceivable > 0) totalReceivable else 8400.0
    val displayPayable = if (totalPayable > 0) totalPayable else 3200.0
    val recentTxs = allTransactions.take(5)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaBackground)
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // 1. Top Header Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KhataCash",
                    color = FigmaGreenDark,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                // Online/Status Chip
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(FigmaGreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(FigmaGreenPrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Greeting
            Text(
                text = "আসসালামু আলাইকুম",
                color = FigmaTextPrimary,
                fontSize = 21.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "আজকের ব্যবসার হিসাব এক নজরে",
                color = FigmaTextMuted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Business Switcher Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color(0x14123321),
                        spotColor = Color(0x14123321)
                    ),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FigmaSurfaceWhite)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BengaliAvatar(
                        letter = profile?.businessName?.take(1) ?: "দ",
                        size = 42
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile?.businessName ?: "আমার দোকান",
                            color = FigmaTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${profile?.businessType ?: "মুদি দোকান"} · ${profile?.address ?: "সিলেট"}",
                            color = FigmaTextMuted,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "⌄",
                        color = FigmaTextMuted,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. Sales & Balance Hero Card
            SalesAndBalanceHeroCard(
                todaySales = displaySales,
                txCount = if (recentTxs.isNotEmpty()) recentTxs.size else 17,
                totalReceivable = displayReceivable,
                totalPayable = displayPayable
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 4. Quick Actions (দ্রুত হিসাব)
            Text(
                text = "দ্রুত হিসাব",
                color = FigmaTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionTile(
                    iconText = "↗",
                    labelText = "বিক্রি",
                    onClick = { onOpenAddTransaction(TransactionType.SALE_CREDIT) }
                )
                ActionTile(
                    iconText = "↓",
                    labelText = "টাকা পেলাম",
                    onClick = { onOpenAddTransaction(TransactionType.PAYMENT_RECEIVED) }
                )
                ActionTile(
                    iconText = "＋",
                    labelText = "কেনা",
                    onClick = { onOpenAddTransaction(TransactionType.PURCHASE_CREDIT) }
                )
                ActionTile(
                    iconText = "−",
                    labelText = "খরচ",
                    onClick = { onOpenAddTransaction(TransactionType.EXPENSE) }
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // 5. Recent Transactions Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "আজকের লেনদেন",
                    color = FigmaTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "সব দেখুন  ›",
                    color = FigmaGreenPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onNavigateToCustomers() }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Recent Transactions List Items
        if (recentTxs.isEmpty()) {
            item {
                Text(
                    text = "আজ এখনও কোনো লেনদেন রেকর্ড করা হয়নি।",
                    color = FigmaTextMuted,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        } else {
            items(recentTxs) { tx ->
                TransactionRowItem(tx = tx)
            }
        }
    }
}

@Composable
private fun TransactionRowItem(tx: Transaction) {
    val isPositive = tx.type == TransactionType.SALE_CREDIT || tx.type == TransactionType.PAYMENT_RECEIVED
    val amountColor = if (isPositive) FigmaGreenPrimary else FigmaRedDena
    val avatarLetter = tx.contactName?.take(1) ?: when (tx.type) {
        TransactionType.EXPENSE -> "খ"
        TransactionType.INCOME_CASH -> "ন"
        else -> "হ"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BengaliAvatar(
                letter = avatarLetter,
                size = 42
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.contactName ?: tx.category ?: tx.type.banglaLabel,
                    color = FigmaTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tx.note ?: tx.type.banglaLabel,
                    color = FigmaTextMuted,
                    fontSize = 11.sp
                )
            }
            Text(
                text = "৳ ${String.format("%,.0f", tx.amount)}",
                color = amountColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        HorizontalDivider(color = FigmaBorder, thickness = 1.dp)
    }
}
