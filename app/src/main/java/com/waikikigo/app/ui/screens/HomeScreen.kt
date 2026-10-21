package com.waikikigo.app.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    repository: WaikikiDataRepository,
    onSelectRestaurant: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isMapExpanded by remember { mutableStateOf(false) }
    var useSatelliteMap by remember { mutableStateOf(false) }
    var selectedRestaurantId by remember { mutableStateOf<String?>("r1") }

    val userProfile by repository.userProfile.collectAsState()
    val batteryMode by repository.batteryMode.collectAsState()
    val quests by repository.quests.collectAsState()

    val filteredRestaurants = remember(searchQuery, userProfile) {
        repository.getFilteredRestaurants(searchQuery)
    }

    val coroutineScope = rememberCoroutineScope()

    // User Location on Kalakaua Ave, Waikiki
    val userLocation = LatLng(21.2770, -157.8270)

    // Waikiki center location
    val waikikiOverview = LatLng(21.2762, -157.8268)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(waikikiOverview, 15f)
    }

    val selectedRestaurant = remember(selectedRestaurantId, filteredRestaurants) {
        filteredRestaurants.find { it.id == selectedRestaurantId } ?: filteredRestaurants.firstOrNull()
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

        // --- Interactive Functional Waikiki Map Container ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isMapExpanded) 380.dp else 230.dp)
                .animateContentSize()
        ) {
            if (useSatelliteMap) {
                // Interactive Google Maps SDK View
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        myLocationButtonEnabled = true
                    ),
                    properties = MapProperties(
                        isMyLocationEnabled = false
                    )
                ) {
                    // User Position
                    Marker(
                        state = rememberMarkerState(position = userLocation),
                        title = "Your Location in Waikiki 📍",
                        snippet = "Walking on Kalakaua Ave",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )

                    // Restaurant Pins
                    filteredRestaurants.forEach { restaurant ->
                        val isSelected = restaurant.id == selectedRestaurantId
                        val markerState = rememberMarkerState(key = restaurant.id, position = LatLng(restaurant.latitude, restaurant.longitude))

                        Marker(
                            state = markerState,
                            title = "${restaurant.name} 🍱 (${restaurant.rating}⭐)",
                            snippet = "${restaurant.cuisine} • ${restaurant.distanceMeters}m away",
                            icon = if (isSelected) {
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                            } else {
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                            },
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
            } else {
                // Rich Custom Cartographic Waikiki Map Canvas View
                WaikikiCartographicMapCanvas(
                    restaurants = filteredRestaurants,
                    selectedRestaurantId = selectedRestaurantId,
                    onSelectRestaurant = { id ->
                        selectedRestaurantId = id
                    }
                )
            }

            // Top Map Controls Overlay Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.75f),
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
                            text = "Waikiki Local Grindz Map",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Switch Map Mode (Cartographic vs Google Satellite)
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { useSatelliteMap = !useSatelliteMap }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = null,
                                tint = WaikikiOceanBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (useSatelliteMap) "Graphics" else "Satellite",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = WaikikiOceanBlue
                            )
                        }
                    }

                    // Expand Map Toggle
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { isMapExpanded = !isMapExpanded }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
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
                                text = if (isMapExpanded) "Collapse" else "Expand",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = WaikikiOceanBlue
                            )
                        }
                    }
                }
            }

            // Bottom Selected Restaurant Floating Quick Card
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                selectedRestaurant?.let { rest ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = SunsetCoral,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = rest.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text(
                                    text = "${rest.cuisine} • ${rest.distanceMeters}m away in Waikiki",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = { onSelectRestaurant(rest.id) },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = WaikikiOceanBlue)
                            ) {
                                Text(
                                    text = "View Grindz ➔",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
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
                        .clickable {
                            selectedRestaurantId = restaurant.id
                        },
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

/**
 * Custom Cartographic Canvas displaying the Waikiki Ocean, Beachfront, Avenues & Tappable Restaurant Pins
 */
@Composable
fun WaikikiCartographicMapCanvas(
    restaurants: List<Restaurant>,
    selectedRestaurantId: String?,
    onSelectRestaurant: (String) -> Unit
) {
    val density = LocalDensity.current.density

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F2F1))
    ) {
        val widthPx = this.constraints.maxWidth.toFloat()
        val heightPx = this.constraints.maxHeight.toFloat()

        // Relative spatial mapping for Waikiki restaurants
        val restaurantPositions = mapOf(
            "r1" to Offset(widthPx * 0.72f, heightPx * 0.38f), // Ono Seafood (Kapahulu Ave)
            "r2" to Offset(widthPx * 0.28f, heightPx * 0.65f), // Duke's Waikiki (Beachfront)
            "r3" to Offset(widthPx * 0.80f, heightPx * 0.52f), // Rainbow Drive-In (Kanaina)
            "r4" to Offset(widthPx * 0.48f, heightPx * 0.42f), // Marugame Udon (Kuhio Ave)
            "r5" to Offset(widthPx * 0.22f, heightPx * 0.58f), // Poke Bar (Kalakaua Ave)
            "r6" to Offset(widthPx * 0.62f, heightPx * 0.25f)  // Helena's (Shuttle)
        )

        val userPos = Offset(widthPx * 0.32f, heightPx * 0.62f)
        val selectedPos = restaurantPositions[selectedRestaurantId] ?: restaurantPositions["r1"]!!

        Canvas(modifier = Modifier.fillMaxSize()) {
            // 1. Draw Pacific Ocean
            val oceanPath = Path().apply {
                moveTo(0f, heightPx * 0.70f)
                cubicTo(
                    widthPx * 0.3f, heightPx * 0.72f,
                    widthPx * 0.6f, heightPx * 0.78f,
                    widthPx, heightPx * 0.85f
                )
                lineTo(widthPx, heightPx)
                lineTo(0f, heightPx)
                close()
            }
            drawPath(path = oceanPath, color = Color(0xFF00838F))

            // 2. Draw Waikiki Beach Shoreline (Golden Sand)
            val beachPath = Path().apply {
                moveTo(0f, heightPx * 0.66f)
                cubicTo(
                    widthPx * 0.3f, heightPx * 0.68f,
                    widthPx * 0.6f, heightPx * 0.74f,
                    widthPx, heightPx * 0.82f
                )
                lineTo(widthPx, heightPx * 0.86f)
                cubicTo(
                    widthPx * 0.6f, heightPx * 0.79f,
                    widthPx * 0.3f, heightPx * 0.73f,
                    0f, heightPx * 0.71f
                )
                close()
            }
            drawPath(path = beachPath, color = Color(0xFFFBC02D))

            // 3. Draw Ala Wai Canal (North border)
            val canalPath = Path().apply {
                moveTo(0f, heightPx * 0.15f)
                lineTo(widthPx * 0.75f, heightPx * 0.18f)
                lineTo(widthPx * 0.88f, heightPx * 0.32f)
            }
            drawPath(
                path = canalPath,
                color = Color(0xFF0288D1),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 18f)
            )

            // 4. Draw Main Waikiki Avenues (Kalakaua & Kuhio)
            // Kalakaua Ave
            drawLine(
                color = Color.White,
                start = Offset(0f, heightPx * 0.63f),
                end = Offset(widthPx, heightPx * 0.78f),
                strokeWidth = 14f
            )
            // Kuhio Ave
            drawLine(
                color = Color.White.copy(alpha = 0.85f),
                start = Offset(0f, heightPx * 0.48f),
                end = Offset(widthPx * 0.8f, heightPx * 0.58f),
                strokeWidth = 10f
            )
            // Kapahulu Ave
            drawLine(
                color = Color.White.copy(alpha = 0.85f),
                start = Offset(widthPx * 0.75f, heightPx * 0.18f),
                end = Offset(widthPx * 0.85f, heightPx * 0.80f),
                strokeWidth = 12f
            )

            // 5. Draw Dashed Route Path from User to Selected Restaurant
            drawPath(
                path = Path().apply {
                    moveTo(userPos.x, userPos.y)
                    lineTo((userPos.x + selectedPos.x) / 2, userPos.y)
                    lineTo(selectedPos.x, selectedPos.y)
                },
                color = WaikikiOceanBlue,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 8f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f)
                )
            )

            // 6. Draw User Location Pulse Circle
            drawCircle(
                color = WaikikiOceanBlue.copy(alpha = 0.25f),
                radius = 36f,
                center = userPos
            )
            drawCircle(
                color = Color(0xFF0288D1),
                radius = 16f,
                center = userPos
            )
            drawCircle(
                color = Color.White,
                radius = 8f,
                center = userPos
            )
        }

        // Tappable Restaurant Pin Chips Overlay
        restaurants.forEach { restaurant ->
            val pos = restaurantPositions[restaurant.id] ?: Offset(widthPx * 0.5f, heightPx * 0.5f)
            val isSelected = restaurant.id == selectedRestaurantId

            Box(
                modifier = Modifier
                    .offset(
                        x = (pos.x / density - 50).dp,
                        y = (pos.y / density - 20).dp
                    )
                    .clickable { onSelectRestaurant(restaurant.id) }
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) SunsetCoral else Color.White,
                    shadowElevation = if (isSelected) 8.dp else 3.dp,
                    border = BorderStroke(1.5.dp, if (isSelected) Color.White else WaikikiOceanBlue)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isSelected) "📍 ${restaurant.name}" else restaurant.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (isSelected) Color.White else Color.Black
                        )
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
