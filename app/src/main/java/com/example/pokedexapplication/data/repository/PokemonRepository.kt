package com.example.pokedexapplication.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.pokedexapplication.data.local.database.PokemonDatabase
import com.example.pokedexapplication.data.local.PokemonEntity
import com.example.pokedexapplication.data.network.PokemonApiService
import com.example.pokedexapplication.domain.model.PokemonDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val PAGE_SIZE = 20

class PokemonRepository @Inject constructor(
    private val apiService: PokemonApiService,
    private val database: PokemonDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPokemonList(query: String): Flow<PagingData<PokemonEntity>> {
        // Format the query for SQL LIKE to find any matches
        val formattedQuery = "%${query.trim()}%"

        val pagingSourceFactory: () -> PagingSource<Int, PokemonEntity> = { database.pokemonDao().pagingSource(formattedQuery) }

        // Only use the RemoteMediator if the user is NOT searching.
        // This allows browsing/pagination to fetch from network,
        // but search will only filter the local cache.
        val useRemoteMediator = query.isBlank()

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = if (useRemoteMediator) {
                PokemonRemoteMediator(
                    apiService = apiService,
                    database = database
                )
            } else {
                null // Disable network fetching when searching
            },
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getPokemonDetails(name: String): Flow<Result<PokemonDetails>> = flow {
        // ... (This function remains unchanged)
        try {
            val response = apiService.getPokemonDetails(name.lowercase())
            val details = PokemonDetails(
                id = response.id,
                name = response.name.replaceFirstChar { it.uppercase() },
                imageUrl = response.sprites.frontDefault,
                types = response.types.map { it.type.name.replaceFirstChar { char -> char.uppercase() } },
                abilities = response.abilities.map { it.ability.name.replaceFirstChar { char -> char.uppercase() } },
                stats = response.stats.map { PokemonDetails.Stat(it.stat.name, it.baseStat) }
            )
            emit(Result.success(details))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
