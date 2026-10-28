package com.khanscreations.khatacash.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khanscreations.khatacash.ui.theme.*

@Composable
fun BengaliAvatar(
    letter: String,
    modifier: Modifier = Modifier,
    size: Int = 42
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(FigmaGreenLight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter.take(1),
            color = FigmaGreenPrimary,
            fontSize = (size * 0.4).sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionTile(
    iconText: String,
    labelText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(80.dp)
            .height(72.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, FigmaBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = FigmaSurfaceWhite
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = iconText,
                color = FigmaGreenPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = labelText,
                color = FigmaTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SalesAndBalanceHeroCard(
    todaySales: Double,
    txCount: Int,
    totalReceivable: Double,
    totalPayable: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x1A123321),
                spotColor = Color(0x1A123321)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FigmaGreenPrimary)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "আজকের বিক্রি",
                color = FigmaSurfaceWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "৳ ${String.format("%,.0f", todaySales)}",
                color = FigmaSurfaceWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "আজ ${txCount}টি লেনদেন",
                color = FigmaGreenLightText,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(
                color = FigmaGreenDivider,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "পাওনা",
                        color = FigmaGreenLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "৳ ${String.format("%,.0f", totalReceivable)}",
                        color = FigmaSurfaceWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "দেনা",
                        color = FigmaAmberLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "৳ ${String.format("%,.0f", totalPayable)}",
                        color = FigmaAmberLight,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun FigmaBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        color = FigmaSurfaceWhite,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalDivider(color = FigmaBorder, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavTabItem(
                    icon = "⌂",
                    label = "হোম",
                    isSelected = currentRoute == "dashboard",
                    onClick = { onNavigate("dashboard") }
                )
                NavTabItem(
                    icon = "▣",
                    label = "খাতা",
                    isSelected = currentRoute == "customers" || currentRoute == "suppliers",
                    onClick = { onNavigate("customers") }
                )
                NavTabItem(
                    icon = "◇",
                    label = "স্টক",
                    isSelected = currentRoute == "inventory",
                    onClick = { onNavigate("inventory") }
                )
                NavTabItem(
                    icon = "▥",
                    label = "রিপোর্ট",
                    isSelected = currentRoute == "reports",
                    onClick = { onNavigate("reports") }
                )
            }
        }
    }
}

@Composable
private fun NavTabItem(
    icon: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isSelected) FigmaGreenPrimary else FigmaTextMuted

    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = icon,
            color = tint,
            fontSize = 18.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}
