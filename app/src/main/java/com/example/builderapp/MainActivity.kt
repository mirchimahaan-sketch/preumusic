package com.premium.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Premium Color Palette
val PremiumDeepObsidian = Color(0xFF090A0F)
val PremiumCardBg = Color(0xFF131520)
val PremiumGold = Color(0xFFFFD700)
val PremiumGoldDark = Color(0xFFB8860B)
val PremiumGoldLight = Color(0xFFFFF9E6)
val TextGray = Color(0xFF8A8D9F)
val PremiumAccentGlow = Color(0x33FFD700)

data class Track(
    val title: String,
    val artist: String,
    val duration: String,
    val durationSeconds: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PremiumDeepObsidian
                ) {
                    PremiumMusicPlayerScreen()
                }
            }
        }
    }
}

@Composable
fun PremiumMusicPlayerScreen() {
    val trackList = remember {
        listOf(
            Track("Aeolian Whisper", "Ethereal Echoes", "03:45", 225),
            Track("Golden Horizon", "Solaris Vibe", "04:12", 252),
            Track("Midnight Velvet", "Deep Obsidian", "02:58", 178),
            Track("Cyber Resonance", "Hyperion Aura", "05:15", 315),
            Track("Symphony of Stars", "Cosmic Orchestra", "03:30", 210)
        )
    }

    var currentTrackIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPlaybackPosition by remember { mutableStateOf(0) }
    var activeTab by remember { mutableStateOf(0) } // 0: Player, 1: Tracks, 2: Equalizer
    var selectedPresetIndex by remember { mutableStateOf(0) }

    val currentTrack = trackList[currentTrackIndex]

    // Simulated playback loop
    LaunchedEffect(isPlaying, currentTrackIndex) {
        if (isPlaying) {
            while (currentPlaybackPosition < currentTrack.durationSeconds) {
                delay(1000)
                currentPlaybackPosition++
            }
            // Auto-next track logic
            if (currentTrackIndex < trackList.lastIndex) {
                currentTrackIndex++
                currentPlaybackPosition = 0
            } else {
                isPlaying = false
                currentPlaybackPosition = 0
            }
        }
    }

    Scaffold(
        bottomBar = {
            PremiumBottomNavigation(
                activeTab = activeTab,
                onTabSelected = { activeTab = it }
            )
        },
        containerColor = PremiumDeepObsidian
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            PremiumDeepObsidian,
                            Color(0xFF12141F),
                            PremiumDeepObsidian
                        )
                    )
                )
        ) {
            when (activeTab) {
                0 -> NowPlayingTab(
                    track = currentTrack,
                    isPlaying = isPlaying,
                    playbackPosition = currentPlaybackPosition,
                    onPlayPauseToggle = { isPlaying = !isPlaying },
                    onPrevClick = {
                        if (currentTrackIndex > 0) {
                            currentTrackIndex--
                        } else {
                            currentTrackIndex = trackList.lastIndex
                        }
                        currentPlaybackPosition = 0
                    },
                    onNextClick = {
                        if (currentTrackIndex < trackList.lastIndex) {
                            currentTrackIndex++
                        } else {
                            currentTrackIndex = 0
                        }
                        currentPlaybackPosition = 0
                    },
                    onSeek = { currentPlaybackPosition = it }
                )
                1 -> LibraryTab(
                    tracks = trackList,
                    currentTrackIndex = currentTrackIndex,
                    isPlaying = isPlaying,
                    onTrackSelected = { index ->
                        currentTrackIndex = index
                        currentPlaybackPosition = 0
                        isPlaying = true
                        activeTab = 0
                    }
                )
                2 -> EqualizerTab(
                    selectedPresetIndex = selectedPresetIndex,
                    onPresetSelected = { selectedPresetIndex = it }
                )
            }
        }
    }
}

@Composable
fun NowPlayingTab(
    track: Track,
    isPlaying: Boolean,
    playbackPosition: Int,
    onPlayPauseToggle: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onSeek: (Int) -> Unit
) {
    // Rotation animation for the premium record visualizer
    val infiniteTransition = rememberInfiniteTransition(label = "VinylRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    val currentRotation = if (isPlaying) rotationAngle else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // App header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = TextGray,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "NOW PLAYING",
                style = TextStyle(
                    color = PremiumGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    shadow = Shadow(
                        color = PremiumAccentGlow,
                        offset = Offset(0f, 0f),
                        blurRadius = 8f
                    )
                )
            )
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                tint = TextGray,
                modifier = Modifier.size(24.dp)
            )
        }

        // Vinyl Disc Vinyl View
        Box(
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .background(PremiumCardBg)
                .border(2.dp, PremiumGold, CircleShape)
                .rotate(currentRotation),
            contentAlignment = Alignment.Center
        ) {
            // Grooves of the record
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                drawCircle(
                    color = Color.Black.copy(alpha = 0.5f),
                    radius = size.width / 2.3f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
                )
                drawCircle(
                    color = Color.Black.copy(alpha = 0.4f),
                    radius = size.width / 2.8f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
                )
                drawCircle(
                    color = Color.Black.copy(alpha = 0.3f),
                    radius = size.width / 3.8f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                )
            }

            // Central Premium Gold Label
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PremiumGoldDark, PremiumGold, PremiumGoldDark)
                        )
                    )
                    .border(3.dp, PremiumDeepObsidian, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = "Music",
                    tint = PremiumDeepObsidian,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Song details
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = track.title,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = track.artist,
                style = TextStyle(
                    color = PremiumGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            )
        }

        // Live premium soundwave visualizer (animated columns)
        SoundWaveVisualizer(isPlaying = isPlaying)

        // Progress bar / Slider
        Column(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = playbackPosition.toFloat(),
                onValueChange = { onSeek(it.toInt()) },
                valueRange = 0f..track.durationSeconds.toFloat(),
                colors = SliderDefaults.colors(
                    activeTrackColor = PremiumGold,
                    inactiveTrackColor = Color(0xFF222533),
                    thumbColor = PremiumGold
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(playbackPosition),
                    style = TextStyle(color = TextGray, fontSize = 12.sp)
                )
                Text(
                    text = track.duration,
                    style = TextStyle(color = TextGray, fontSize = 12.sp)
                )
            }
        }

        // Control Panel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPrevClick,
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = "Previous",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Big Play/Pause Button with outer Premium Glow Ring
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PremiumGold, PremiumGoldDark)
                        )
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                    .clickable { onPlayPauseToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "PlayPause",
                    tint = PremiumDeepObsidian,
                    modifier = Modifier.size(40.dp)
                )
            }

            IconButton(
                onClick = onNextClick,
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun LibraryTab(
    tracks: List<Track>,
    currentTrackIndex: Int,
    isPlaying: Boolean,
    onTrackSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "PREMIUM LIBRARY",
            style = TextStyle(
                color = PremiumGold,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(tracks) { index, track ->
                val isSelected = index == currentTrackIndex
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) PremiumCardBg else Color(0x99131520))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) PremiumGold.copy(alpha = 0.5f) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onTrackSelected(index) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) PremiumGold else Color(0xFF232535)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected && isPlaying) Icons.Filled.VolumeUp else Icons.Filled.MusicNote,
                            contentDescription = "Track Status",
                            tint = if (isSelected) PremiumDeepObsidian else PremiumGold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = track.title,
                            style = TextStyle(
                                color = if (isSelected) PremiumGold else Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = track.artist,
                            style = TextStyle(
                                color = TextGray,
                                fontSize = 13.sp
                            )
                        )
                    }

                    Text(
                        text = track.duration,
                        style = TextStyle(
                            color = if (isSelected) PremiumGold else TextGray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EqualizerTab(
    selectedPresetIndex: Int,
    onPresetSelected: (Int) -> Unit
) {
    val presets = listOf("Standard", "Pure Bass", "Vocal Booster", "3D Concert Hall", "Jazz Club")
    val bandFrequencies = listOf("60 Hz", "230 Hz", "910 Hz", "4 kHz", "14 kHz")
    val bandValues = remember { mutableStateListOf(0.6f, 0.4f, 0.75f, 0.5f, 0.8f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "EQUALIZER & EFFECTS",
            style = TextStyle(
                color = PremiumGold,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Custom Presets Selector
        Text(
            text = "PRESET SOUND PROFILES",
            style = TextStyle(color = TextGray, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.forEachIndexed { idx, preset ->
                val isSelected = idx == selectedPresetIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) PremiumGold else PremiumCardBg)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color.White else Color(0xFF2B2D3A),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            onPresetSelected(idx)
                            // Change slider values depending on dynamic preset mock configurations
                            when (idx) {
                                0 -> { bandValues[0]=0.5f; bandValues[1]=0.5f; bandValues[2]=0.5f; bandValues[3]=0.5f; bandValues[4]=0.5f }
                                1 -> { bandValues[0]=0.9f; bandValues[1]=0.75f; bandValues[2]=0.4f; bandValues[3]=0.3f; bandValues[4]=0.2f }
                                2 -> { bandValues[0]=0.3f; bandValues[1]=0.4f; bandValues[2]=0.85f; bandValues[3]=0.75f; bandValues[4]=0.5f }
                                3 -> { bandValues[0]=0.7f; bandValues[1]=0.6f; bandValues[2]=0.5f; bandValues[3]=0.65f; bandValues[4]=0.85f }
                                4 -> { bandValues[0]=0.5f; bandValues[1]=0.6f; bandValues[2]=0.55f; bandValues[3]=0.5f; bandValues[4]=0.7f }
                            }
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = preset,
                        style = TextStyle(
                            color = if (isSelected) PremiumDeepObsidian else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        maxLines = 1
                    )
                }
            }
        }

        // Equalizer bands
        Text(
            text = "FREQUENCY BANDS",
            style = TextStyle(color = TextGray, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = PremiumCardBg),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bandValues.forEachIndexed { idx, valItem ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = "${(valItem * 24 - 12).toInt()} dB",
                            style = TextStyle(color = PremiumGold, fontSize = 11.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .width(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Slider(
                                value = valItem,
                                onValueChange = { bandValues[idx] = it },
                                modifier = Modifier
                                    .graphicsLayer {
                                        rotationZ = -90f
                                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin.Center
                                    }
                                    .width(140.dp),
                                colors = SliderDefaults.colors(
                                    activeTrackColor = PremiumGold,
                                    inactiveTrackColor = Color(0xFF1F2231),
                                    thumbColor = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = bandFrequencies[idx],
                            style = TextStyle(color = TextGray, fontSize = 11.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SoundWaveVisualizer(isPlaying: Boolean) {
    val barCount = 14
    val animationStates = List(barCount) { index ->
        val duration = remember { (600..1200).random() }
        val infiniteTransition = rememberInfiniteTransition(label = "WaveBar$index")
        infiniteTransition.animateFloat(
            initialValue = 0.1f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "HeightScale"
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val scale = if (isPlaying) animationStates[i].value else 0.15f
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(scale)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(PremiumGoldLight, PremiumGold, PremiumGoldDark)
                        )
                    )
            )
        }
    }
}

@Composable
fun PremiumBottomNavigation(
    activeTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = PremiumCardBg,
        tonalElevation = 8.dp,
        modifier = Modifier.border(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.05f),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        )
    ) {
        NavigationBarItem(
            selected = activeTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Rounded.PlayArrow, contentDescription = "Now Playing") },
            label = { Text("Player", style = TextStyle(fontSize = 11.sp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PremiumGold,
                unselectedIconColor = TextGray,
                selectedTextColor = PremiumGold,
                unselectedTextColor = TextGray,
                indicatorColor = Color(0x11FFD700)
            )
        )
        NavigationBarItem(
            selected = activeTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Rounded.QueueMusic, contentDescription = "Tracks") },
            label = { Text("Tracks", style = TextStyle(fontSize = 11.sp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PremiumGold,
                unselectedIconColor = TextGray,
                selectedTextColor = PremiumGold,
                unselectedTextColor = TextGray,
                indicatorColor = Color(0x11FFD700)
            )
        )
        NavigationBarItem(
            selected = activeTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Rounded.GraphicEq, contentDescription = "Equalizer") },
            label = { Text("EQ", style = TextStyle(fontSize = 11.sp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PremiumGold,
                unselectedIconColor = TextGray,
                selectedTextColor = PremiumGold,
                unselectedTextColor = TextGray,
                indicatorColor = Color(0x11FFD700)
            )
        )
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}