package com.example.pokedexapplication.domain.model

data class PokemonDetails(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val abilities: List<String>,
    val stats: List<Stat>
) {
    data class Stat(
        val name: String,
        val value: Int
    )
}