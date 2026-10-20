package com.waikikigo.app.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.waikikigo.app.data.WaikikiDataRepository
import com.waikikigo.app.model.BatteryLocationMode
import com.waikikigo.app.model.Restaurant
import com.waikikigo.app.ui.theme.PalmGreen
import com.waikikigo.app.ui.theme.SandyGold
import com.waikikigo.app.ui.theme.SunsetCoral
import com.waikikigo.app.ui.theme.WaikikiGoTheme
import com.waikikigo.app.ui.theme.WaikikiOceanBlue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    repository: WaikikiDataRepository,
    onSelectRestaurant: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isMapExpanded by remember { mutableStateOf(false) }
    var selectedRestaurantId by remember { mutableStateOf<String?>(null) }

    val userProfile by repository.userProfile.collectAsState()
    val batteryMode by repository.batteryMode.collectAsState()
    val quests by repository.quests.collectAsState()

    val filteredRestaurants = remember(searchQuery, userProfile) {
        repository.getFilteredRestaurants(searchQuery)
    }

    // Waikiki center location
    val waikikiCenter = LatLng(21.2762, -157.8268)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(waikikiCenter, 15f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Header Banner with Battery Power Saver & GPS Status ---
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Aloha, ${userProfile.username}! 🌺",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Waikiki, HI • GPS Tracking Active",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    // Points pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SandyGold
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = "Points",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${userProfile.totalPoints} pts",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Battery Saver GPS Mode Toggle Card
                Card(
                    onClick = { repository.toggleBatteryMode() },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (batteryMode == BatteryLocationMode.HIGH_PERFORMANCE) Icons.Default.BatteryChargingFull else Icons.Default.BatterySaver,
                                contentDescription = "Battery Mode",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = batteryMode.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = batteryMode.description,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Switch(
                            checked = batteryMode == BatteryLocationMode.BATTERY_SAVER,
                            onCheckedChange = { repository.toggleBatteryMode() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = PalmGreen
                            )
                        )
                    }
                }
            }
        }

        // --- Interactive Waikiki Google Map Container ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isMapExpanded) 360.dp else 220.dp)
                .animateContentSize()
        ) {
            // Google Map View displaying all Waikiki restaurant pins
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false
                ),
                properties = MapProperties(
                    isMyLocationEnabled = false
                )
            ) {
                // User Current Position Pin
                Marker(
                    state = rememberMarkerState(position = LatLng(21.2770, -157.8270)),
                    title = "Your Location in Waikiki 📍",
                    snippet = "Walking on Kalakaua Ave"
                )

                // Restaurant Location Pins
                filteredRestaurants.forEach { restaurant ->
                    val markerState = rememberMarkerState(key = restaurant.id, position = LatLng(restaurant.latitude, restaurant.longitude))
                    Marker(
                        state = markerState,
                        title = "${restaurant.name} 🍱 (${restaurant.rating}⭐)",
                        snippet = "${restaurant.cuisine} • ${restaurant.distanceMeters}m away • Tap to view",
                        onClick = {
                            selectedRestaurantId = restaurant.id
                            false
                        },
                        onInfoWindowClick = {
                            onSelectRestaurant(restaurant.id)
                        }
                    )
                }
            }

            // Top Map Overlay Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Waikiki Beach Map View",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clickable { isMapExpanded = !isMapExpanded }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isMapExpanded) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = null,
                            tint = WaikikiOceanBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isMapExpanded) "Collapse Map" else "Expand Map",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WaikikiOceanBlue
                        )
                    }
                }
            }

            // Bottom Map Location Pin Carousel
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredRestaurants) { rest ->
                        val isSelected = selectedRestaurantId == rest.id
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) WaikikiOceanBlue else MaterialTheme.colorScheme.surface,
                            shadowElevation = 6.dp,
                            modifier = Modifier.clickable {
                                selectedRestaurantId = rest.id
                                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                                    LatLng(rest.latitude, rest.longitude), 16f
                                )
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else SunsetCoral,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = rest.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${rest.distanceMeters}m",
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Search Bar & Filter Bar ---
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search local grindz, poke, loco moco...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                } else null,
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            // Active filters indicator
            if (userProfile.selectedTags.isNotEmpty() || userProfile.allergies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        Text(
                            text = "Filtered by:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    items(userProfile.selectedTags.toList()) { tag ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text(tag.displayName, fontSize = 11.sp) }
                        )
                    }
                    items(userProfile.allergies.toList()) { allergy ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text("No ${allergy.displayName}", fontSize = 11.sp, color = SunsetCoral) },
                            border = SuggestionChipDefaults.suggestionChipBorder(
                                enabled = true,
                                borderColor = SunsetCoral
                            )
                        )
                    }
                }
            }
        }

        // --- Restaurant List ---
        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredRestaurants) { restaurant ->
                val restaurantQuests = quests.filter { it.restaurantId == restaurant.id }
                val availableQuests = restaurantQuests.count { !it.isCompleted }
                val isSelected = selectedRestaurantId == restaurant.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectRestaurant(restaurant.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) WaikikiOceanBlue.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                    ),
                    border = if (isSelected) BorderStroke(2.dp, WaikikiOceanBlue) else null,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = restaurant.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${restaurant.cuisine} • ${restaurant.priceRange}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = SandyGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = restaurant.rating.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = restaurant.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Safe allergy clearance badge & dietary chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PalmGreen.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = PalmGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Allergy Checked Safe",
                                        fontSize = 11.sp,
                                        color = PalmGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            restaurant.dietaryTags.take(3).forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = tag.displayName,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                                    contentDescription = null,
                                    tint = WaikikiOceanBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${restaurant.distanceMeters}m away in Waikiki",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = { onSelectRestaurant(restaurant.id) },
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (availableQuests > 0) "Quests ($availableQuests) 🎯" else "View Grindz",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WaikikiGoTheme {
        HomeScreen(
            repository = WaikikiDataRepository(),
            onSelectRestaurant = {}
        )
    }
}
