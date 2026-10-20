package com.waikikigo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waikikigo.app.data.WaikikiDataRepository
import com.waikikigo.app.model.Reward
import com.waikikigo.app.ui.theme.PalmGreen
import com.waikikigo.app.ui.theme.SandyGold
import com.waikikigo.app.ui.theme.SunsetCoral
import com.waikikigo.app.ui.theme.WaikikiOceanBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    repository: WaikikiDataRepository
) {
    val userProfile by repository.userProfile.collectAsState()
    val rewards by repository.rewards.collectAsState()

    var activeVoucherReward by remember { mutableStateOf<Reward?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = WaikikiOceanBlue,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Waikiki Rewards & Perks 🎁",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Redeem your quest points for purchase-based perks & discounts!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Points Balance Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.18f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Available Balance",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${userProfile.totalPoints} Points",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 26.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = SandyGold
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ConfirmationNumber,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Explorer Rank",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Rewards list
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rewards) { reward ->
                val canAfford = userProfile.totalPoints >= reward.pointCost

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (reward.isRedeemed) PalmGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = reward.restaurantName,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (canAfford || reward.isRedeemed) SandyGold else Color.Gray
                            ) {
                                Text(
                                    text = "${reward.pointCost} pts",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = reward.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = reward.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Purchase Requirement Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SunsetCoral.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = SunsetCoral,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = reward.purchaseRequirement,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SunsetCoral
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (reward.isRedeemed) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = null,
                                        tint = PalmGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Coupon Unlocked!",
                                        color = PalmGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                Button(
                                    onClick = { activeVoucherReward = reward },
                                    colors = ButtonDefaults.buttonColors(containerColor = PalmGreen),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Show Voucher 🎟️")
                                }
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (canAfford) {
                                        val success = repository.redeemReward(reward.id)
                                        if (success) {
                                            activeVoucherReward = reward.copy(isRedeemed = true)
                                        }
                                    } else {
                                        errorMessage = "You need ${reward.pointCost - userProfile.totalPoints} more points to unlock this reward!"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = canAfford,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WaikikiOceanBlue
                                )
                            ) {
                                Text(
                                    text = if (canAfford) "Redeem Reward Perks 🎟️" else "Insufficient Points (${reward.pointCost} Pts Needed)"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Voucher Modal Displaying QR Code & Voucher Code ---
    activeVoucherReward?.let { reward ->
        AlertDialog(
            onDismissRequest = { activeVoucherReward = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = WaikikiOceanBlue,
                    modifier = Modifier.size(44.dp)
                )
            },
            title = {
                Text(
                    text = reward.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Present this voucher to your waiter at ${reward.restaurantName}:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = SunsetCoral.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Condition: ${reward.purchaseRequirement}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SunsetCoral
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated QR Code Graphic Box
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.QrCode2,
                                contentDescription = "QR Code",
                                tint = Color.Black,
                                modifier = Modifier.size(120.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "PROMO: ${reward.couponCode}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = WaikikiOceanBlue
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { activeVoucherReward = null },
                    colors = ButtonDefaults.buttonColors(containerColor = WaikikiOceanBlue)
                ) {
                    Text("Done")
                }
            }
        )
    }

    // Error Snackbar alert
    errorMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Points Needed") },
            text = { Text(msg) },
            confirmButton = {
                TextButton(onClick = { errorMessage = null }) {
                    Text("OK")
                }
            }
        )
    }
}
