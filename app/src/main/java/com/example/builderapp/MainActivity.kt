package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.sin

// --- THEME COLORS ---
val ObsidianDark = Color(0xFF09090C)
val DeepIndigo = Color(0xFF12111A)
val PremiumGold = Color(0xFFFFD700)
val NeonCyan = Color(0xFF00E5FF)
val NeonPurple = Color(0xFF8A2BE2)
val SoftGray = Color(0xFF9E9E9E)
val TranslucentGlass = Color(0x1AFFFFFF)
val MutedGold = Color(0x4DFFD700)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ObsidianDark
                ) {
                    PremiumMusicPlayerApp()
                }
            }
        }
    }
}

// --- DATA MODELS ---
data class Track(
    val id: Int,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Int, // in seconds
    val themeColor: Color,
    val lyrics: List<Pair<Int, String>> // Seconds to lyrics map
)

// --- MOCK DATABASE ---
val mockTracks = listOf(
    Track(
        id = 1,
        title = "Midnight Neon Symphony",
        artist = "Synthetix Aura",
        album = "Grid Runner",
        duration = 195,
        themeColor = NeonPurple,
        lyrics = listOf(
            0 to "Instrumental Intro...",
            8 to "Cruising through the cyber streets...",
            15 to "The neon lights reflect inside my eyes...",
            22 to "Feel the rhythm of the grid take control...",
            30 to "Can you feel the pulse? It's alive!",
            45 to "Synthesizers screaming in the dark...",
            60 to "[Chorus] We are the neon symphony...",
            75 to "Electric dreams that never fade..."
        )
    ),
    Track(
        id = 2,
        title = "Golden Hour Serenade",
        artist = "Acoustic Mirage",
        album = "Sunset Drive",
        duration = 240,
        themeColor = PremiumGold,
        lyrics = listOf(
            0 to "Soft acoustic guitar picking...",
            12 to "The sun is sinking low into the sea...",
            24 to "Gold dust in the air, warm winds blow...",
            36 to "Your hand in mine, we watch it go...",
            48 to "[Chorus] This is our golden hour serenade...",
            64 to "No regrets, let the memories never fade...",
            80 to "Time stands still under the amber sky..."
        )
    ),
    Track(
        id = 3,
        title = "Abyssal Echoes",
        artist = "Deep Ocean Ambient",
        album = "Mariana Trench",
        duration = 320,
        themeColor = NeonCyan,
        lyrics = listOf(
            0 to "Deep ocean sonar sounds...",
            20 to "Floating down into the dark abyss...",
            40 to "Where the sunlight cannot dare to reach...",
            60 to "Pressure rising, heart rate slowing down...",
            80 to "Echoes of ancient giants calling...",
            110 to "Lost in the pressure, found in the deep..."
        )
    ),
    Track(
        id = 4,
        title = "Cybernetic Horizon",
        artist = "Delta V",
        album = "Starfighter",
        duration = 180,
        themeColor = Color(0xFFFF2A6D),
        lyrics = listOf(
            0 to "Systems booting up... Check.",
            10 to "Engines firing at maximum rate...",
            20 to "Approaching the cybernetic horizon...",
            30 to "Hyper-drive initiated!",
            45 to "Stars blend into long lines of light...",
            60 to "[Chorus] Ride the cybernetic wave across the sky...",
            75 to "No gravity can hold us down tonight..."
        )
    )
)

// --- VIEWMODEL FOR STATE MANAGEMENT ---
class MusicViewModel : ViewModel() {
    private val _playlist = MutableStateFlow(mockTracks)
    val playlist: StateFlow<List<Track>> = _playlist.asStateFlow()

    private val _currentTrack = MutableStateFlow(mockTracks[0])
    val currentTrack: StateFlow<Track> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private val _isRepeat = MutableStateFlow(false)
    val isRepeat: StateFlow<Boolean> = _isRepeat.asStateFlow()

    private val _favoriteTracks = MutableStateFlow<Set<Int>>(setOf(1, 3))
    val favoriteTracks: StateFlow<Set<Int>> = _favoriteTracks.asStateFlow()

    // EQ Bands State (Hz -> Gain value (0.0 to 2.0))
    private val _eqBands = MutableStateFlow(listOf(1.0f, 1.2f, 0.8f, 1.5f, 1.1f))
    val eqBands: StateFlow<List<float>> = _eqBands.asStateFlow()

    private val _activeTab = MutableStateFlow("home")
    val activeTab: StateFlow<String> = _activeTab.asStateFlow()

    init {
        // Continuous playback timer simulation
        kotlinx.coroutines.GlobalScope.launch {
            while (true) {
                delay(1000)
                if (_isPlaying.value) {
                    if (_currentPosition.value < _currentTrack.value.duration) {
                        _currentPosition.value += 1
                    } else {
                        nextTrack()
                    }
                }
            }
        }
    }

    fun setTab(tab: String) {
        _activeTab.value = tab
    }

    fun playTrack(track: Track) {
        _currentTrack.value = track
        _currentPosition.value = 0
        _isPlaying.value = true
    }

    fun togglePlay() {
        _isPlaying.value = !_isPlaying.value
    }

    fun seekTo(seconds: Int) {
        _currentPosition.value = seconds.coerceIn(0, _currentTrack.value.duration)
    }

    fun nextTrack() {
        val currentIndex = _playlist.value.indexOf(_currentTrack.value)
        val nextIndex = if (_isShuffle.value) {
            (0 until _playlist.value.size).random()
        } else {
            (currentIndex + 1) % _playlist.value.size
        }
        playTrack(_playlist.value[nextIndex])
    }

    fun prevTrack() {
        val currentIndex = _playlist.value.indexOf(_currentTrack.value)
        var prevIndex = currentIndex - 1
        if (prevIndex < 0) prevIndex = _playlist.value.size - 1
        playTrack(_playlist.value[prevIndex])
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
    }

    fun toggleRepeat() {
        _isRepeat.value = !_isRepeat.value
    }

    fun toggleFavorite(trackId: Int) {
        val currentFavs = _favoriteTracks.value.toMutableSet()
        if (currentFavs.contains(trackId)) {
            currentFavs.remove(trackId)
        } else {
            currentFavs.add(trackId)
        }
        _favoriteTracks.value = currentFavs
    }

    fun updateEqBand(index: Int, gain: Float) {
        val updated = _eqBands.value.toMutableList()
        updated[index] = gain
        _eqBands.value = updated
    }
}

// --- MAIN CONTAINER ---
@Composable
fun PremiumMusicPlayerApp(viewModel: MusicViewModel = viewModel()) {
    val activeTab by viewModel.activeTab.collectAsState()
    var isPlayerExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ObsidianDark,
                        DeepIndigo,
                        ObsidianDark
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Main Content Area with conditional views based on tabs
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    "home" -> HomeScreen(viewModel, onExpandPlayer = { isPlayerExpanded = true })
                    "library" -> LibraryScreen(viewModel)
                    "eq" -> EqualizerScreen(viewModel)
                }
            }

            // Glassmorphic Mini Player
            MiniPlayer(
                viewModel = viewModel,
                onClick = { isPlayerExpanded = true }
            )

            // Premium Bottom Navigation Bar
            PremiumNavigationBar(
                activeTab = activeTab,
                onTabSelected = { viewModel.setTab(it) }
            )
        }

        // Animated Full Screen Premium Player Overlay
        AnimatedVisibility(
            visible = isPlayerExpanded,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            )
        ) {
            FullPlayerScreen(
                viewModel = viewModel,
                onCollapse = { isPlayerExpanded = false }
            )
        }
    }
}

// --- PROCEDURAL PREMIUM ALBUM ART COMPOSABLE ---
@Composable
fun PremiumAlbumArt(
    track: Track,
    modifier: Modifier = Modifier,
    isRotating: Boolean = false,
    glowColor: Color = track.themeColor
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Art Rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotationAngle"
    )

    val currentRotation = if (isRotating) angle else 0f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .shadow(
                elevation = 24.dp,
                shape = CircleShape,
                clip = false,
                ambientColor = glowColor,
                spotColor = glowColor
            )
    ) {
        // Outer Glowing Aura
        Box(
            modifier = Modifier
                .fillMaxSize(0.95f)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(glowColor.copy(alpha = 0.4f), Color.Transparent),
                            center = center,
                            radius = size.width / 1.8f
                        )
                    )
                }
        )

        // Vinyl Body / Album Core Container
        Box(
            modifier = Modifier
                .fillMaxSize(0.85f)
                .rotate(currentRotation)
                .background(Color(0xFF0F0F14), shape = CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.08f), CircleShape)
        ) {
            // Procedural Vinyl Grooves & Decorative Art
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                // Draw multiple micro grooves
                for (i in 4..12) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.03f),
                        radius = (size.width / 2) * (i / 14f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
                    )
                }

                // Inner Holographic Accent Shapes
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(glowColor, Color.Transparent, glowColor.copy(alpha = 0.7f), glowColor),
                        center = center
                    ),
                    radius = size.width * 0.28f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                )
            }

            // Middle Rich Art Cover Circle
            Box(
                modifier = Modifier
                    .fillMaxSize(0.55f)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.8f),
                                ObsidianDark,
                                glowColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            ) {
                // Procedural Geometric Album Graphic
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    // Modern glowing triangular/poly lines
                    drawCircle(
                        color = Color.White.copy(alpha = 0.15f),
                        radius = w * 0.4f
                    )
                    drawLine(
                        brush = Brush.linearGradient(listOf(Color.White, glowColor)),
                        start = Offset(0f, h * 0.5f),
                        end = Offset(w, h * 0.5f),
                        strokeWidth = 3f
                    )
                    drawLine(
                        brush = Brush.linearGradient(listOf(glowColor, Color.Transparent)),
                        start = Offset(w * 0.3f, h * 0.1f),
                        end = Offset(w * 0.7f, h * 0.9f),
                        strokeWidth = 2f
                    )
                }
            }

            // Center Spindle Hole representing authentic record
            Box(
                modifier = Modifier
                    .fillMaxSize(0.08f)
                    .align(Alignment.Center)
                    .background(ObsidianDark, CircleShape)
                    .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape)
            )
        }
    }
}

// --- SUB-SCREEN: HOME VIEW ---
@Composable
fun HomeScreen(viewModel: MusicViewModel, onExpandPlayer: () -> Unit) {
    val playlist by viewModel.playlist.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)
    ) {
        // App header with premium branding
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Between,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WELCOME TO",
                        style = TextStyle(
                            color = SoftGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    )
                    Text(
                        text = "AURA PREMIUM",
                        style = TextStyle(
                            brush = Brush.horizontalGradient(listOf(PremiumGold, Color.White)),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .background(TranslucentGlass, CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "Profile",
                        tint = PremiumGold
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        // Live Visualizer Display Box
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                currentTrack.themeColor.copy(alpha = 0.15f),
                                TranslucentGlass
                            )
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
                    .clickable { onExpandPlayer() }
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NOW TUNED INTO",
                            style = TextStyle(
                                color = currentTrack.themeColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentTrack.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = currentTrack.artist,
                            style = MaterialTheme.typography.bodyMedium.copy(color = SoftGray)
                        )
                    }

                    // Little live visualizer
                    LiveAudioVisualizer(
                        isPlaying = isPlaying,
                        color = currentTrack.themeColor,
                        modifier = Modifier.width(60.dp).height(50.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        // Featured horizontal list
        item {
            Text(
                text = "Premium Highlights",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(14.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(playlist) { track ->
                    FeaturedTrackCard(track = track, onClick = { viewModel.playTrack(track) })
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        // Popular Tracks List
        item {
            Text(
                text = "Most Streamed Today",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        itemsIndexed(playlist) { index, track ->
            TrackListItem(
                index = index + 1,
                track = track,
                isCurrent = track.id == currentTrack.id,
                onClick = { viewModel.playTrack(track) }
            )
        }
    }
}

// --- SUB-SCREEN: LIBRARY VIEW ---
@Composable
fun LibraryScreen(viewModel: MusicViewModel) {
    val playlist by viewModel.playlist.collectAsState()
    val favoriteTracks by viewModel.favoriteTracks.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)
    ) {
        item {
            Text(
                text = "YOUR LIBRARY",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Interactive Fast Playlists Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.linearGradient(listOf(NeonPurple.copy(alpha = 0.4f), ObsidianDark)))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                        Icon(Icons.Rounded.Favorite, contentDescription = null, tint = NeonPurple)
                        Column {
                            Text("Favorites", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("${favoriteTracks.size} Tracks", color = SoftGray, fontSize = 12.sp)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.linearGradient(listOf(PremiumGold.copy(alpha = 0.4f), ObsidianDark)))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                        Icon(Icons.AutoMirrored.Rounded.List, contentDescription = null, tint = PremiumGold)
                        Column {
                            Text("My Playlists", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("1 Custom", color = SoftGray, fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        item {
            Text(
                text = "Favorite Tracks Only",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        val favList = playlist.filter { favoriteTracks.contains(it.id) }
        if (favList.isEmpty()) {
            item {
                Text(
                    text = "No favorites marked yet. Press heart inside full player!",
                    color = SoftGray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        } else {
            items(favList) { track ->
                TrackListItem(
                    index = playlist.indexOf(track) + 1,
                    track = track,
                    isCurrent = false,
                    onClick = { viewModel.playTrack(track) }
                )
            }
        }
    }
}

// --- SUB-SCREEN: EQUALIZER VIEW ---
@Composable
fun EqualizerScreen(viewModel: MusicViewModel) {
    val eqBands by viewModel.eqBands.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            Text(
                text = "AUDIO ENGINE",
                style = TextStyle(
                    color = SoftGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            )
            Text(
                text = "Equalizer Pro",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Live Audio Visualizer Plate
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(TranslucentGlass)
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                .padding(20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Interactive complex visuals
                eqBands.forEachIndexed { index, gain ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .fillMaxHeight(gain.coerceIn(0.1f, 1.0f))
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        currentTrack.themeColor,
                                        currentTrack.themeColor.copy(alpha = 0.2f)
                                    )
                                )
                            )
                    )
                }
            }
        }

        // Interactive Equalizer Sliders
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val hertzLabels = listOf("60 Hz", "230 Hz", "910 Hz", "4 kHz", "14 kHz")
            eqBands.forEachIndexed { index, gain ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = hertzLabels[index],
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.width(65.dp)
                    )

                    Slider(
                        value = gain,
                        onValueChange = { viewModel.updateEqBand(index, it) },
                        valueRange = 0.0f..2.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = currentTrack.themeColor,
                            activeTrackColor = currentTrack.themeColor,
                            inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = String.format("+%.1f dB", (gain - 1.0f) * 12f),
                        color = if (gain > 1.0f) currentTrack.themeColor else SoftGray,
                        fontSize = 11.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(55.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

// --- FLOATING MINI PLAYER ---
@Composable
fun MiniPlayer(
    viewModel: MusicViewModel,
    onClick: () -> Unit
) {
    val currentTrack by viewModel.currentTrack.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()

    val progress = currentPosition.toFloat() / currentTrack.duration.toFloat()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(68.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xE6100F15))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Spinning Disk in Mini Player
            PremiumAlbumArt(
                track = currentTrack,
                isRotating = isPlaying,
                modifier = Modifier.size(46.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = currentTrack.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentTrack.artist,
                    color = SoftGray,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Simple micro controls
            IconButton(onClick = { viewModel.togglePlay() }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Rounded.PlayArrow else Icons.Rounded.PlayArrow, // Fallback swap
                    contentDescription = "Play/Pause",
                    tint = Color.White
                )
                Icon(
                    imageVector = if (isPlaying) Icons.Rounded.Menu /* Actually representing pause customly here */ else Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = if (isPlaying) currentTrack.themeColor else Color.White
                )
            }

            IconButton(onClick = { viewModel.nextTrack() }) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow, // Standard skip icon representation
                    contentDescription = "Next Track",
                    tint = Color.White,
                    modifier = Modifier.rotate(0f)
                )
            }
        }

        // Subtly embedded high-fidelity linear progress at bottom border of card
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(currentTrack.themeColor)
            )
        }
    }
}

// --- FULL SCREEN PREMIUM MUSIC PLAYER ---
@Composable
fun FullPlayerScreen(
    viewModel: MusicViewModel,
    onCollapse: () -> Unit
) {
    val currentTrack by viewModel.currentTrack.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val favoriteTracks by viewModel.favoriteTracks.collectAsState()
    val isShuffle by viewModel.isShuffle.collectAsState()
    val isRepeat by viewModel.isRepeat.collectAsState()

    var showLyrics by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        currentTrack.themeColor.copy(alpha = 0.35f),
                        ObsidianDark,
                        ObsidianDark
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCollapse) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Collapse",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "AURA ULTRA PLAY",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                )

                IconButton(onClick = { showLyrics = !showLyrics }) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = "Lyrics Mode",
                        tint = if (showLyrics) currentTrack.themeColor else Color.White
                    )
                }
            }

            // Middle Display Component (Either Album Art or synchronized Lyrics view)
            Box(
                modifier = Modifier
                    .weight(1.3f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (!showLyrics) {
                    PremiumAlbumArt(
                        track = currentTrack,
                        isRotating = isPlaying,
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )
                } else {
                    LyricsViewer(
                        lyrics = currentTrack.lyrics,
                        currentPosition = currentPosition,
                        themeColor = currentTrack.themeColor
                    )
                }
            }

            // Song Info Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentTrack.title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        shadow = Shadow(color = currentTrack.themeColor, blurRadius = 10f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = currentTrack.artist,
                    style = TextStyle(
                        color = SoftGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                )
            }

            // Timeline Progress Slider
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                val progress = currentPosition.toFloat() / currentTrack.duration.toFloat()

                // Slider with custom glow/track aesthetics
                Slider(
                    value = currentPosition.toFloat(),
                    onValueChange = { viewModel.seekTo(it.toInt()) },
                    valueRange = 0f..currentTrack.duration.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = currentTrack.themeColor,
                        inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        color = SoftGray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = formatTime(currentTrack.duration),
                        color = SoftGray,
                        fontSize = 12.sp
                    )
                }
            }

            // Premium Controls Board
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle Button
                IconButton(onClick = { viewModel.toggleShuffle() }) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Shuffle",
                        tint = if (isShuffle) currentTrack.themeColor else Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Previous Button
                IconButton(onClick = { viewModel.prevTrack() }) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .rotate(180f) // Simple reverse arrow representation
                    )
                }

                // Glassmorphic Glowing Play/Pause Circle
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    currentTrack.themeColor,
                                    currentTrack.themeColor.copy(alpha = 0.6f)
                                )
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                        .clickable { viewModel.togglePlay() }
                        .shadow(
                            elevation = 16.dp,
                            shape = CircleShape,
                            clip = false,
                            ambientColor = currentTrack.themeColor,
                            spotColor = currentTrack.themeColor
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.Close else Icons.Rounded.PlayArrow,
                        contentDescription = "Play/Pause Toggle",
                        tint = Color.White,
                        modifier = Modifier.size(38.dp)
                    )
                }

                // Next Button
                IconButton(onClick = { viewModel.nextTrack() }) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Favorite Toggle
                val isFav = favoriteTracks.contains(currentTrack.id)
                IconButton(onClick = { viewModel.toggleFavorite(currentTrack.id) }) {
                    Icon(
                        imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFav) Color.Red else Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

// --- LYRICS VIEWER WITH AUTO-SCROLL/HIGHLIGHT ---
@Composable
fun LyricsViewer(
    lyrics: List<Pair<Int, String>>,
    currentPosition: Int,
    themeColor: Color
) {
    val scrollState = rememberScrollState()

    // Determine currently highlighting line index
    var currentHighlightIndex = -1
    for (i in lyrics.indices) {
        if (currentPosition >= lyrics[i].first) {
            currentHighlightIndex = i
        }
    }

    // Auto-scroll logic as lyrics advance
    LaunchedEffect(currentHighlightIndex) {
        if (currentHighlightIndex != -1) {
            // Scroll to center animatedly
            scrollState.animateScrollTo(currentHighlightIndex * 140)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            lyrics.forEachIndexed { index, pair ->
                val isSelected = index == currentHighlightIndex
                val alpha by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0.35f,
                    label = "LyricAlpha"
                )
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.1f else 0.95f,
                    label = "LyricScale"
                )

                Text(
                    text = pair.second,
                    style = TextStyle(
                        color = if (isSelected) themeColor else Color.White,
                        fontSize = 18.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(scaleX = scale, scaleY = scale)
                        .padding(horizontal = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(180.dp))
        }
    }
}

// --- VISUAL ELEMENTS: FEATURED CARD ---
@Composable
fun FeaturedTrackCard(track: Track, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(210.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(TranslucentGlass)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            // Procedural Cover inside Card
            PremiumAlbumArt(
                track = track,
                isRotating = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            Column {
                Text(
                    text = track.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = track.artist,
                    color = SoftGray,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// --- VISUAL ELEMENTS: TRACK LIST ITEM ---
@Composable
fun TrackListItem(
    index: Int,
    track: Track,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isCurrent) Color.White.copy(alpha = 0.05f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Serial Index / Status Visualizer
        Box(modifier = Modifier.width(36.dp), contentAlignment = Alignment.CenterStart) {
            if (isCurrent) {
                LiveAudioVisualizer(
                    isPlaying = true,
                    color = track.themeColor,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = String.format("%02d", index),
                    color = SoftGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Circular Minimalistic Thumbnail representing vinyl disc
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(Color.DarkGray, CircleShape)
        ) {
            PremiumAlbumArt(track = track, isRotating = false, modifier = Modifier.fillMaxSize())
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                color = if (isCurrent) track.themeColor else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                color = SoftGray,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = formatTime(track.duration),
            color = SoftGray,
            fontSize = 12.sp
        )
    }
}

// --- CORE UTILITY: LIVE AUDIO VISUALIZER COMPOSABLE ---
@Composable
fun LiveAudioVisualizer(
    isPlaying: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Visualizer")

    // Dynamic wave calculations based on current playing state
    @Composable
    fun animatedHeight(initialValue: Float, targetValue: Float, duration: Int): Float {
        if (!isPlaying) return initialValue
        val anim by infiniteTransition.animateFloat(
            initialValue = initialValue,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "BarHeight"
        )
        return anim
    }

    val bar1 = animatedHeight(0.2f, 0.9f, 600)
    val bar2 = animatedHeight(0.3f, 0.7f, 400)
    val bar3 = animatedHeight(0.1f, 1.0f, 750)
    val bar4 = animatedHeight(0.2f, 0.8f, 500)

    val waveHeights = listOf(bar1, bar2, bar3, bar4)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        waveHeights.forEach { heightMultiplier ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(heightMultiplier)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}

// --- PREMIUM BOTTOM NAVIGATION BAR ---
@Composable
fun PremiumNavigationBar(
    activeTab: String,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color(0xFF0C0B10))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val navItems = listOf(
            Triple("home", Icons.Rounded.Home, "Discover"),
            Triple("library", Icons.Rounded.List, "Library"),
            Triple("eq", Icons.Rounded.Settings, "Equalizer")
        )

        navItems.forEach { item ->
            val isSelected = activeTab == item.first
            val scale by animateFloatAsState(if (isSelected) 1.15f else 1.0f, label = "TabScale")
            val iconColor = if (isSelected) PremiumGold else SoftGray

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTabSelected(item.first) }
                    )
                    .graphicsLayer(scaleX = scale, scaleY = scale),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.second,
                    contentDescription = item.third,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.third,
                    color = iconColor,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

// --- UTILS ---
fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%d:%02d", m, s)
}