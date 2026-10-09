package com.waikikigo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.waikikigo.app.model.Quest
import com.waikikigo.app.model.QuestType
import com.waikikigo.app.ui.theme.PalmGreen
import com.waikikigo.app.ui.theme.SandyGold
import com.waikikigo.app.ui.theme.WaikikiOceanBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(
    repository: WaikikiDataRepository
) {
    val quests by repository.quests.collectAsState()
    val userProfile by repository.userProfile.collectAsState()

    var activeCameraQuest by remember { mutableStateOf<Quest?>(null) }
    var celebrationQuest by remember { mutableStateOf<Quest?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Banner
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
                    text = "Scavenger Quests 🎯",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Dine at Waikiki local grindz & post photo/video proof to earn points!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Quests Completed: ${quests.count { it.isCompleted }} / ${quests.size}",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SandyGold
                    ) {
                        Text(
                            text = "${userProfile.totalPoints} Points",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Quests List
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(quests) { quest ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (quest.isCompleted) PalmGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
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
                                    text = quest.restaurantName,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SandyGold
                            ) {
                                Text(
                                    text = "+${quest.pointsReward} pts",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = quest.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = quest.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (quest.isCompleted) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = PalmGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Quest Complete!",
                                        color = PalmGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                Text(
                                    text = quest.questType.displayName,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            Button(
                                onClick = { activeCameraQuest = quest },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WaikikiOceanBlue
                                )
                            ) {
                                Icon(
                                    imageVector = when (quest.questType) {
                                        QuestType.PHOTO -> Icons.Default.CameraAlt
                                        QuestType.VIDEO -> Icons.Default.Videocam
                                        QuestType.REVIEW -> Icons.Default.RateReview
                                        QuestType.CHECK_IN -> Icons.Default.PinDrop
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Complete ${quest.questType.displayName}")
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Interactive Camera Proof Capture Modal ---
    activeCameraQuest?.let { quest ->
        AlertDialog(
            onDismissRequest = { activeCameraQuest = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = null,
                    tint = WaikikiOceanBlue,
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text(
                    text = quest.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Waikiki Go Camera Preview",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Simulated Camera Viewfinder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color.Black, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Point camera at your meal at ${quest.restaurantName}",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "GPS verified at ${quest.restaurantName}. Submitting grants +${quest.pointsReward} pts!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        repository.completeQuest(quest.id)
                        celebrationQuest = quest
                        activeCameraQuest = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WaikikiOceanBlue)
                ) {
                    Text("Snap & Submit 📸")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeCameraQuest = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- Celebration Point Claim Modal ---
    celebrationQuest?.let { quest ->
        AlertDialog(
            onDismissRequest = { celebrationQuest = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = SandyGold,
                    modifier = Modifier.size(56.dp)
                )
            },
            title = {
                Text(
                    text = "Chee-Hoo! Quest Complete! 🌺",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = WaikikiOceanBlue
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "You earned +${quest.pointsReward} points for completing:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = quest.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Check out the Rewards page to redeem your points for free meals and discounts!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { celebrationQuest = null },
                    colors = ButtonDefaults.buttonColors(containerColor = WaikikiOceanBlue)
                ) {
                    Text("Claim Points! 🤙")
                }
            }
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun QuestScreenPreview() {
    com.waikikigo.app.ui.theme.WaikikiGoTheme {
        QuestScreen(repository = WaikikiDataRepository())
    }
}

