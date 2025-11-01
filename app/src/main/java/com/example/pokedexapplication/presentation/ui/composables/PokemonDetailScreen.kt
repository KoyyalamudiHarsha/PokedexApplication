package com.example.pokedexapplication.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedexapplication.domain.model.PokemonDetails
import com.example.pokedexapplication.presentation.detail.PokemonDetailUiState
import com.example.pokedexapplication.presentation.detail.PokemonDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is PokemonDetailUiState.Success) {
                        Text((uiState as PokemonDetailUiState.Success).details.name)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is PokemonDetailUiState.Loading -> CircularProgressIndicator()
                is PokemonDetailUiState.Success -> PokemonDetailsContent(details = state.details)
                is PokemonDetailUiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun PokemonDetailsContent(details: PokemonDetails) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(details.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "${details.name} image",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "#${details.id} ${details.name}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Types
            SectionTitle("Types")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                details.types.forEach { type ->
                    Chip(label = type)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Abilities
            SectionTitle("Abilities")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                details.abilities.forEach { ability ->
                    Chip(label = ability)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Stats
            SectionTitle("Base Stats")
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                details.stats.forEach { stat ->
                    StatRow(statName = stat.name, statValue = stat.value)
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
fun Chip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
    }
}

@Composable
fun StatRow(statName: String, statValue: Int) {
    val maxStat = 255f // Max base stat value in Pokemon games
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName.replaceFirstChar { it.uppercase() },
            modifier = Modifier.weight(0.3f),
            fontSize = 14.sp
        )
        LinearProgressIndicator(
            progress = { statValue / maxStat },
            modifier = Modifier
                .weight(0.7f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = if (statValue >= 100) Color(0xFF4CAF50) else if (statValue >= 50) Color(0xFFFFC107) else Color(0xFFF44336)
        )
        Text(
            text = "$statValue",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}