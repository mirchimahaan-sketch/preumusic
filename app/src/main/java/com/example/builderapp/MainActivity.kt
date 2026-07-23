package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Premium Color Palette
val ObsidianDark = Color(0 planetaryDark = 0xFF0D0E15)
val DeepViolet = Color(0xFF161726)
val ElectricPink = Color(0xFFFF2E93)
val CyberCyan = Color(0xFF00F5D4)
val PlatinumGrey = Color(0xFFE2E8F0)
val GlassyOverlay = Color(0x1AFFFFFF)

data class Track(
    val title: String,
    val artist: String,
    val duration: String,
    val durationSec: Int,
    val primaryColor: Color,
    val secondaryColor: Color
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuraMusicTheme {
                PremiumMusicPlayerApp()
            }
        }
    }
}

@Composable
fun AuraMusicTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = ObsidianDark,
            surface = DeepViolet,
            primary = ElectricPink,
            secondary = CyberCyan
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PremiumMusicPlayerApp() {
    val playlist = remember {
        listOf(
            Track("Neon Dreams", "Synthwave Sunset", "03:45", 225, ElectricPink, CyberCyan),
            Track("Midnight Chase", "Cyberpunk Odyssey", "04:12", 252, CyberCyan, Color(0xFF7B2CBF)),
            Track("Stardust Echoes", "Celestial Vibes", "02:58", 178, Color(0xFF9D4EDD), ElectricPink),
            Track("Electric Pulse", "Tokyo Drift", "03:30", 210, Color(0xFFFF9F1C), ElectricPink),
            Track("Velvet Horizon", "Chill Hop Cafe", "03:15", 195, Color(0xFF2EC4B6), Color(0xFF118AB2)),
            Track("Cosmic Resonance", "Astral Waves", "05:01", 301, Color(0xFFE0A96D), Color(0xFF201E20))
        )
    }

    var currentTrackIndex by remember { mutableStateOf(0) }
    val currentTrack = playlist[currentTrackIndex]
    
    var isPlaying by remember { mutableStateOf(false) }
    var trackProgressSec by remember { mutableStateOf(0) }
    var isFavourite by remember { mutableStateOf(false) }
    var isShuffleActive by remember { mutableStateOf(false) }
    var isRepeatActive by remember { mutableStateOf(false) }

    // Audio progress simulation loop
    LaunchedEffect(isPlaying, currentTrackIndex) {
        if (isPlaying) {
            while (trackProgressSec < currentTrack.durationSec) {
                delay(1000)
                trackProgressSec++
            }
            // Auto transition or loop behavior
            if (isRepeatActive) {
                trackProgressSec = 0
            } else {
                if (currentTrackIndex < playlist.lastIndex) {
                    currentTrackIndex++
                    trackProgressSec = 0
                } else {
                    isPlaying = false
                    trackProgressSec = 0
                }
            }
        }
    }

    // Dynamic color matching current album art scheme
    val animatedPrimary by animateColorAsState(targetValue = currentTrack.primaryColor, label = "primaryColor")
    val animatedSecondary by animateColorAsState(targetValue = currentTrack.secondaryColor, label = "secondaryColor")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ObsidianDark, DeepViolet, ObsidianDark)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // Elegant Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Go Back */ }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = PlatinumGrey
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "NOW PLAYING",
                        color = PlatinumGrey.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Aura Premium",
                        color = animatedPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                IconButton(onClick = { /* Settings */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = PlatinumGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Premium Animated Glowing Vinyl/Disk
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer glow shadow
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.85f)
                        .shadow(
                            elevation = 24.dp,
                            shape = CircleShape,
                            ambientColor = animatedPrimary,
                            spotColor = animatedSecondary
                        )
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(animatedPrimary, animatedSecondary, animatedPrimary)
                            ),
                            shape = CircleShape
                        )
                )

                // Rotation infinite transition
                val infiniteTransition = rememberInfiniteTransition(label = "rotation")
                val rotationAngle by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 10000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "rotate"
                )

                // Rotating Vinyl Body
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.78f)
                        .rotate(if (isPlaying) rotationAngle else 0f)
                        .background(Color(0xFF030303), shape = CircleShape)
                        .border(width = 3.dp, color = GlassyOverlay, shape = CircleShape)
                ) {
                    // Stylized Vinyl Grooves
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.8f)
                            .align(Alignment.Center)
                            .border(width = 1.dp, color = Color.White.copy(alpha = 0.1f), shape = CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.6f)
                            .align(Alignment.Center)
                            .border(width = 1.dp, color = Color.White.copy(alpha = 0.08f), shape = CircleShape)
                    )
                    // Disk Center Cover
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.4f)
                            .align(Alignment.Center)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(animatedSecondary, animatedPrimary)
                                ),
                                shape = CircleShape
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(ObsidianDark, shape = CircleShape)
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Premium Audio Equalizer / Visualizer Animation
            MusicVisualizer(isPlaying = isPlaying, activeColor = animatedPrimary)

            Spacer(modifier = Modifier.height(16.dp))

            // Track details with interactive Fav Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(0.8f)) {
                    Text(
                        text = currentTrack.title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentTrack.artist,
                        color = PlatinumGrey.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = { isFavourite = !isFavourite },
                    modifier = Modifier.background(GlassyOverlay, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavourite) ElectricPink else PlatinumGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Timeline Progress Slider
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = trackProgressSec.toFloat(),
                    onValueChange = { newValue ->
                        trackProgressSec = newValue.toInt()
                    },
                    valueRange = 0f..currentTrack.durationSec.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = animatedPrimary,
                        activeTrackColor = animatedPrimary,
                        inactiveTrackColor = PlatinumGrey.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(trackProgressSec),
                        color = PlatinumGrey.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = currentTrack.duration,
                        color = PlatinumGrey.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Advanced Media Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isShuffleActive = !isShuffleActive }) {
                    Icon(
                        imageVector = Icons.Default.Refresh, // Standard icon fallback for shuffle aesthetic
                        contentDescription = "Shuffle",
                        tint = if (isShuffleActive) animatedPrimary else PlatinumGrey.copy(alpha = 0.6f)
                    )
                }

                IconButton(
                    onClick = {
                        if (currentTrackIndex > 0) {
                            currentTrackIndex--
                        } else {
                            currentTrackIndex = playlist.lastIndex
                        }
                        trackProgressSec = 0
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = PlatinumGrey,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Eye-catching Play / Pause Button with Gradient Accent
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(animatedPrimary, animatedSecondary)
                            ),
                            shape = CircleShape
                        )
                        .clickable { isPlaying = !isPlaying },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "PlayPause",
                        tint = Color.White,
                        modifier = Modifier.size(38.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (currentTrackIndex < playlist.lastIndex) {
                            currentTrackIndex++
                        } else {
                            currentTrackIndex = 0
                        }
                        trackProgressSec = 0
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = PlatinumGrey,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = { isRepeatActive = !isRepeatActive }) {
                    Icon(
                        imageVector = Icons.Default.Share, // Standard icon fallback for dynamic action
                        contentDescription = "Repeat",
                        tint = if (isRepeatActive) animatedSecondary else PlatinumGrey.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Playlist Label
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AURA PREMIUM PLAYLIST",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp
                )
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Playlist",
                    tint = animatedPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Modern Glassmorphic Playlist Section
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(playlist) { index, track ->
                    val isSelected = index == currentTrackIndex
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) GlassyOverlay else Color.Transparent)
                            .clickable {
                                currentTrackIndex = index
                                trackProgressSec = 0
                                isPlaying = true
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Track index or playing indicator
                        if (isSelected && isPlaying) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Playing",
                                tint = animatedPrimary,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 8.dp)
                            )
                        } else {
                            Text(
                                text = (index + 1).toString().padStart(2, '0'),
                                color = PlatinumGrey.copy(alpha = 0.4f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(32.dp)
                            )
                        }

                        // Track Art representation
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(track.primaryColor, track.secondaryColor)
                                    )
                                )
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Title & Artist Name
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = track.title,
                                color = if (isSelected) animatedPrimary else Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = track.artist,
                                color = PlatinumGrey.copy(alpha = 0.5f),
                                fontSize = 13.sp
                            )
                        }

                        // Duration duration stamp
                        Text(
                            text = track.duration,
                            color = PlatinumGrey.copy(alpha = 0.6f),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

// Elegant simulated soundwave visualizer
@Composable
fun MusicVisualizer(isPlaying: Boolean, activeColor: Color) {
    val barCount = 15
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    
    val heightScaleList = List(barCount) { index ->
        val duration = remember { (600..1200).random() }
        if (isPlaying) {
            infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = duration, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bar_$index"
            )
        } else {
            remember { mutableStateOf(0.15f) }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val scale = heightScaleList[i].value
            val animatedHeight = 32.dp * scale
            val calculatedColor = activeColor.copy(alpha = (0.3f + (0.7f * scale)))

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(animatedHeight)
                    .background(calculatedColor, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}

// Helper formatting function
private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}