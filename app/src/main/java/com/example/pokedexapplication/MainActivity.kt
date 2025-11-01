package com.example.pokedexapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedexapplication.ui.theme.PokedexTheme
import com.example.pokedexapplication.presentation.ui.composables.PokemonDetailScreen
import com.example.pokedexapplication.presentation.ui.composables.PokemonListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "pokemon_list"
                    ) {
                        composable("pokemon_list") {
                            PokemonListScreen(
                                onPokemonClick = { pokemonName ->
                                    navController.navigate("pokemon_detail/$pokemonName")
                                }
                            )
                        }
                        composable(
                            route = "pokemon_detail/{pokemonName}",
                            arguments = listOf(navArgument("pokemonName") { type = NavType.StringType })
                        ) {
                            PokemonDetailScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}