package com.example.pokedexapplication.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO for the paginated list of Pokemon
@Serializable
data class PokemonListResponse(
    @SerialName("results") val results: List<PokemonResult>
)

@Serializable
data class PokemonResult(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

// DTO for detailed Pokemon information
@Serializable
data class PokemonDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("sprites") val sprites: Sprites,
    @SerialName("types") val types: List<TypeEntry>,
    @SerialName("abilities") val abilities: List<AbilityEntry>,
    @SerialName("stats") val stats: List<StatEntry>
)

@Serializable
data class Sprites(
    @SerialName("front_default") val frontDefault: String
)

@Serializable
data class TypeEntry(
    @SerialName("type") val type: Type
)

@Serializable
data class Type(
    @SerialName("name") val name: String
)

@Serializable
data class AbilityEntry(
    @SerialName("ability") val ability: Ability
)

@Serializable
data class Ability(
    @SerialName("name") val name: String
)

@Serializable
data class StatEntry(
    @SerialName("base_stat") val baseStat: Int,
    @SerialName("stat") val stat: Stat
)

@Serializable
data class Stat(
    @SerialName("name") val name: String
)
