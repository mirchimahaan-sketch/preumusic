package com.example.premiummusicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PremiumMusicPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF07070C)
                ) {
                    PremiumMusicApp()
                }
            }
        }
    }
}

// ================= COLOR THEME =================
val GoldPremium = Color(0xFFFFD700)
val GoldDark = Color(0xFFB8860B)
val PremiumDeepBg = Color(0xFF09090E)
val CardBackground = Color(0xFF151522)
val NeonCyan = Color(0xFF00E5FF)
val PremiumPurple = Color(0xFF7000FF)

@Composable
fun PremiumMusicPlayerTheme(content: @Composable () -> Unit) {
    val colors = darkColorScheme(
        primary = GoldPremium,
        secondary = NeonCyan,
        background = PremiumDeepBg,
        surface = CardBackground,
        onPrimary = Color.Black,
        onSecondary = Color.Black
    )
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

// ================= DATA MODELS =================
data class Track(
    val id: Int,
    val title: String,
    val artist: String,
    val duration: String,
    val durationSeconds: Int,
    val color: Color
)

// ================= APP LAYOUT =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumMusicApp() {
    // Media Playback State
    val trackList = listOf(
        Track(1, "Raatan Lambiyan", "Jubin Nautiyal, Asees Kaur", "3:50", 230, Color(0xFFFF5252)),
        Track(2, "Kesariya", "Arijit Singh, Pritam", "4:28", 268, Color(0xFFFFAB40)),
        Track(3, "Blinding Lights", "The Weeknd", "3:20", 200, Color(0xFFE040FB)),
        Track(4, "Tum Hi Ho", "Arijit Singh", "4:22", 262, Color(0xFF448AFF)),
        Track(5, "Starboy", "The Weeknd ft. Daft Punk", "3:50", 230, Color(0xFF69F0AE)),
        Track(6, "Apna Bana Le", "Arijit Singh, Sachin-Jigar", "4:24", 264, Color(0xFFFF4081)),
        Track(7, "Chaleya", "Anirudh Ravichander, Arijit Singh", "3:20", 200, Color(0xFF18FFFF))
    )

    var currentTrackIndex by remember { mutableStateOf(0) }
    val currentTrack = trackList[currentTrackIndex]
    var isPlaying by remember { mutableStateOf(false) }
    var playbackProgress by remember { mutableStateOf(45f) } // percentage
    var isMuted by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }

    // Navigation state: 0 = Discover, 1 = Equalizer, 2 = VIP Premium
    var selectedTab by remember { mutableStateOf(0) }
    var isPlayerExpanded by remember { mutableStateOf(false) }

    // Simulating active playback progression
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                delay(1000)
                if (playbackProgress < 100f) {
                    playbackProgress += 100f / currentTrack.durationSeconds
                } else {
                    playbackProgress = 0f
                    // Move to next track
                    currentTrackIndex = (currentTrackIndex + 1) % trackList.size
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            Column {
                // Mini Player (Visible only when details are collapsed and not on Premium upgrade screen explicitly)
                if (!isPlayerExpanded) {
                    MiniPlayer(
                        track = currentTrack,
                        isPlaying = isPlaying,
                        progress = playbackProgress,
                        onPlayPauseToggle = { isPlaying = !isPlaying },
                        onNext = {
                            currentTrackIndex = (currentTrackIndex + 1) % trackList.size
                            playbackProgress = 0f
                        },
                        onExpand = { isPlayerExpanded = true }
                    )
                }
                
                // Bottom Navigation
                NavigationBar(
                    containerColor = Color(0xFF0C0C14),
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = (selectedTab == 0 && !isPlayerExpanded),
                        onClick = {
                            selectedTab = 0
                            isPlayerExpanded = false
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Discover", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPremium,
                            selectedTextColor = GoldPremium,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0x33FFD700)
                        )
                    )
                    NavigationBarItem(
                        selected = (selectedTab == 1 && !isPlayerExpanded),
                        onClick = {
                            selectedTab = 1
                            isPlayerExpanded = false
                        },
                        icon = { Icon(Icons.Default.GraphicEq, contentDescription = "Equalizer") },
                        label = { Text("Sound FX", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPremium,
                            selectedTextColor = GoldPremium,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0x33FFD700)
                        )
                    )
                    NavigationBarItem(
                        selected = (selectedTab == 2 && !isPlayerExpanded),
                        onClick = {
                            selectedTab = 2
                            isPlayerExpanded = false
                        },
                        icon = { Icon(Icons.Default.Star, contentDescription = "VIP") },
                        label = { Text("Premium", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPremium,
                            selectedTextColor = GoldPremium,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0x33FFD700)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0A0B10), Color(0xFF141525))
                    )
                )
        ) {
            // Main views based on tab choice
            AnimatedContent(
                targetState = if (isPlayerExpanded) 99 else selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "MainTransition"
            ) { targetState ->
                when (targetState) {
                    0 -> DiscoverScreen(
                        trackList = trackList,
                        currentTrack = currentTrack,
                        onTrackSelected = { track ->
                            val index = trackList.indexOf(track)
                            if (index != -1) {
                                currentTrackIndex = index
                                isPlaying = true
                                playbackProgress = 0f
                            }
                        }
                    )
                    1 -> EqualizerScreen()
                    2 -> PremiumUpgradeScreen()
                    99 -> FullPlayerScreen(
                        track = currentTrack,
                        isPlaying = isPlaying,
                        playbackProgress = playbackProgress,
                        isMuted = isMuted,
                        isLiked = isLiked,
                        onPlayPauseToggle = { isPlaying = !isPlaying },
                        onNext = {
                            currentTrackIndex = (currentTrackIndex + 1) % trackList.size
                            playbackProgress = 0f
                        },
                        onPrev = {
                            currentTrackIndex = if (currentTrackIndex - 1 < 0) trackList.size - 1 else currentTrackIndex - 1
                            playbackProgress = 0f
                        },
                        onSeek = { playbackProgress = it },
                        onMuteToggle = { isMuted = !isMuted },
                        onLikeToggle = { isLiked = !isLiked },
                        onMinimize = { isPlayerExpanded = false }
                    )
                }
            }
        }
    }
}

// ================= DISCOVER SCREEN =================
@Composable
fun DiscoverScreen(
    trackList: List<Track>,
    currentTrack: Track,
    onTrackSelected: (Track) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredTracks = trackList.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.artist.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp)
    ) {
        // Upper VIP Badge Area
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good Evening,",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Premium Listener",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(listOf(GoldPremium, GoldDark)),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "PRO VIP",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                placeholder = { Text("Search premium songs, artists...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = GoldPremium) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPremium,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedContainerColor = Color(0xFF151522),
                    unfocusedContainerColor = Color(0xFF101018)
                ),
                singleLine = true
            )
        }

        // Dynamic Premium Playlist Carousel
        item {
            Text(
                text = "Premium Highlights",
                color = GoldPremium,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(trackList.take(4)) { track ->
                    HighlightCard(track = track, onClick = { onTrackSelected(track) })
                }
            }
        }

        // Tracks Listing Header
        item {
            Text(
                text = "Your Private Lounge",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // All Songs List
        items(filteredTracks) { track ->
            TrackListItem(
                track = track,
                isCurrent = track.id == currentTrack.id,
                onClick = { onTrackSelected(track) }
            )
        }
    }
}

@Composable
fun HighlightCard(track: Track, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1D30))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(track.color, Color(0xFF10101C))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(48.dp)
                )
                // Visual Indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = "HD+", color = GoldPremium, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = track.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                color = Color.Gray,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TrackListItem(track: Track, isCurrent: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isCurrent) Color(0x1AFFFFD7) else Color(0xFF12121F))
            .clickable(onClick = onClick)
            .border(
                1.dp,
                if (isCurrent) GoldPremium.copy(alpha = 0.4f) else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Small disk representation
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Brush.sweepGradient(listOf(track.color, Color.Black))),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                color = if (isCurrent) GoldPremium else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                color = Color.Gray,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (isCurrent) {
            // Live active visualizer mini wave
            MiniVisualizer()
        } else {
            Text(
                text = track.duration,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

// ================= SOUND EQUALIZER SCREEN =================
@Composable
fun EqualizerScreen() {
    var bassBoost by remember { mutableStateOf(85f) }
    var surroundSound by remember { mutableStateOf(60f) }
    var vocalClarity by remember { mutableStateOf(70f) }
    var reverbSelection by remember { mutableStateOf("Premium Studio") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Dolby Atmos™ Equalizer",
            color = GoldPremium,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Optimize acoustic settings for high fidelity professional headphones.",
            color = Color.Gray,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Preset Configs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("Jazz", "Rock", "Pop", "Classical", "Bass Pro").forEach { mode ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (mode == "Bass Pro") GoldPremium else Color(0xFF1E1E30),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode,
                        color = if (mode == "Bass Pro") Color.Black else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Slider controls
        PremiumEqualizerSlider(label = "Bass Ultra Boost", value = bassBoost, onValueChange = { bassBoost = it })
        PremiumEqualizerSlider(label = "3D Spatial Surround", value = surroundSound, onValueChange = { surroundSound = it })
        PremiumEqualizerSlider(label = "Crystal Clear Vocals", value = vocalClarity, onValueChange = { vocalClarity = it })

        Spacer(modifier = Modifier.height(32.dp))

        // Advanced Environment Select
        Text(
            text = "Acoustic Environment",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val options = listOf("Premium Studio", "Grand Opera Concert Hall", "Live Stadium", "Cozy Jazz Lounge")
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (reverbSelection == option) Color(0x3300E5FF) else Color(0xFF151522))
                    .border(
                        width = 1.dp,
                        color = if (reverbSelection == option) NeonCyan else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { reverbSelection = option }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (reverbSelection == option),
                    onClick = { reverbSelection = option },
                    colors = RadioButtonDefaults.colors(selectedColor = NeonCyan, unselectedColor = Color.Gray)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = option, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun PremiumEqualizerSlider(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = Color.LightGray, fontSize = 14.sp)
            Text(text = "${value.toInt()}%", color = GoldPremium, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = GoldPremium,
                activeTrackColor = GoldPremium,
                inactiveTrackColor = Color.DarkGray
            )
        )
    }
}

// ================= PREMIUM UPGRADE SCREEN =================
@Composable
fun PremiumUpgradeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "VIP Icon",
            tint = GoldPremium,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "GOLD PREMIUM MEMBER",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Experience High Fidelity lossless audios with ultimate spatial sound customizations.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Subscription Tier card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF221A30), Color(0xFF140D20))
                    )
                )
                .border(2.dp, GoldPremium, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VIP Premium Pass",
                        color = GoldPremium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Save 50%",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0x3300E5FF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "₹129 / Month Only",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 26.sp
                )
                Text(
                    text = "Billed annually. Cancel anytime.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Feature checklist
                listOf(
                    "High Fidelity 320kbps Master Quality",
                    "Custom 3D Equalizer Unlocked",
                    "Infinite Offline Playback (No Ads)",
                    "Shared family account (up to 5 members)"
                ).forEach { feature ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = feature, color = Color.LightGray, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Payment Call-To-Action Button
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(12.dp, RoundedCornerShape(28.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPremium
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "UPGRADE NOW TO VIP",
                color = Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
    }
}

// ================= COMPACT MINI PLAYER =================
@Composable
fun MiniPlayer(
    track: Track,
    isPlaying: Boolean,
    progress: Float,
    onPlayPauseToggle: () -> Unit,
    onNext: () -> Unit,
    onExpand: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF101018))
            .clickable(onClick = onExpand)
    ) {
        // Linear Progress Indicator along the top edge of mini-player
        LinearProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier.fillMaxWidth().height(2.dp),
            color = GoldPremium,
            trackColor = Color.DarkGray
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Spinning disk on music play
            val infiniteTransition = rememberInfiniteTransition(label = "DiskRotate")
            val rotationAngle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 8000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "Angle"
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Brush.sweepGradient(listOf(track.color, Color.Black)))
                    .rotate(if (isPlaying) rotationAngle else 0f),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = track.artist,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onPlayPauseToggle) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.White
                )
            }

            IconButton(onClick = onNext) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = Color.White
                )
            }
        }
    }
}

// ================= EXPANDED FULLSCREEN PLAYER =================
@Composable
fun FullPlayerScreen(
    track: Track,
    isPlaying: Boolean,
    playbackProgress: Float,
    isMuted: Boolean,
    isLiked: Boolean,
    onPlayPauseToggle: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSeek: (Float) -> Unit,
    onMuteToggle: () -> Unit,
    onLikeToggle: () -> Unit,
    onMinimize: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CdDiscExpansion")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "DiscRotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F0E17), Color(0xFF1E1C2B))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMinimize) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "NOW PLAYING",
                color = GoldPremium,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                letterSpacing = 2.sp
            )
            IconButton(onClick = onMuteToggle) {
                Icon(
                    imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = "Volume State",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.4f))

        // Large Rotating Premium Vinyl Album Disc
        Box(
            modifier = Modifier
                .size(280.dp)
                .shadow(32.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.Black)
                .rotate(if (isPlaying) rotationAngle else 0f),
            contentAlignment = Alignment.Center
        ) {
            // Vinyl texture lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                drawCircle(color = Color(0xFF1A1A1A), radius = size.minDimension / 2)
                drawCircle(color = Color(0xFF222222), radius = size.minDimension * 0.42f)
                drawCircle(color = Color(0xFF1F1F1F), radius = size.minDimension * 0.35f)
                drawCircle(color = Color(0xFF292929), radius = size.minDimension * 0.28f)
            }

            // Cover custom radial art
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(track.color, Color.Black)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Center spindle hole
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F0E17))
            )
        }

        Spacer(modifier = Modifier.weight(0.4f))

        // Title and artist metadata
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = track.title,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = track.artist,
                color = GoldPremium,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.weight(0.3f))

        // Audio dynamic Visualizer Panel
        LiveEqualizerWave(isPlaying = isPlaying)

        Spacer(modifier = Modifier.weight(0.3f))

        // Master playback progress bar
        Column(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = playbackProgress,
                onValueChange = onSeek,
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = GoldPremium,
                    activeTrackColor = GoldPremium,
                    inactiveTrackColor = Color.DarkGray
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val elapsedSeconds = (track.durationSeconds * (playbackProgress / 100f)).toInt()
                val currentMinutes = elapsedSeconds / 60
                val currentSecs = elapsedSeconds % 60
                Text(
                    text = String.format("%d:%02d", currentMinutes, currentSecs),
                    color = Color.Gray,
                    fontSize = 11.sp
                )
                Text(
                    text = track.duration,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.3f))

        // Audio controller panel buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onLikeToggle) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like Track",
                    tint = if (isLiked) Color.Red else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            IconButton(onClick = onPrev) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(listOf(GoldPremium, GoldDark)))
                    .clickable(onClick = onPlayPauseToggle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play Control",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(onClick = onNext) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.2f))
    }
}

// ================= AUDIO VISUALIZER EFFECT COMPONENTS =================
@Composable
fun LiveEqualizerWave(isPlaying: Boolean) {
    val barCount = 20
    val waveHeightScales = remember { Array(barCount) { Animatable(0.2f) } }

    if (isPlaying) {
        waveHeightScales.forEachIndexed { index, animatable ->
            LaunchedEffect(key1 = isPlaying, key2 = index) {
                while (true) {
                    val randomValue = Random.nextFloat().coerceIn(0.1f, 1f)
                    animatable.animateTo(
                        targetValue = randomValue,
                        animationSpec = tween(
                            durationMillis = 250 + (index * 20),
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }
        }
    } else {
        waveHeightScales.forEach { animatable ->
            LaunchedEffect(key1 = isPlaying) {
                animatable.animateTo(0.1f, tween(300))
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(waveHeightScales[i].value)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(GoldPremium, NeonCyan)
                        )
                    )
            )
        }
    }
}

@Composable
fun MiniVisualizer() {
    val animationTransition = rememberInfiniteTransition(label = "MiniWave")
    val waveOne by animationTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w1"
    )
    val waveTwo by animationTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w2"
    )
    val waveThree by animationTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w3"
    )

    Row(
        modifier = Modifier
            .width(20.dp)
            .height(18.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(waveOne)
                .background(GoldPremium)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(waveTwo)
                .background(GoldPremium)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(waveThree)
                .background(GoldPremium)
        )
    }
}