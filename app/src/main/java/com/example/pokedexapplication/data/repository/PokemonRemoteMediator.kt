package com.example.pokedexapplication.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pokedexapplication.data.local.PokemonEntity
import com.example.pokedexapplication.data.local.RemoteKeys
import com.example.pokedexapplication.data.local.database.PokemonDatabase
import com.example.pokedexapplication.data.network.PokemonApiService
import retrofit2.HttpException
import java.io.IOException

private const val POKEAPI_STARTING_PAGE_INDEX = 0
private const val MAX_ITEM_COUNT = 100 // Define the max item count
@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val apiService: PokemonApiService,
    private val database: PokemonDatabase
) : RemoteMediator<Int, PokemonEntity>() {

    private val pokemonDao = database.pokemonDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> POKEAPI_STARTING_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            if (page * state.config.pageSize >= MAX_ITEM_COUNT) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val response = apiService.getPokemonList(
                limit = state.config.pageSize,
                offset = page * state.config.pageSize
            )
            val pokemonList = response.results
            val endOfPaginationReached = pokemonList.isEmpty()

            val pokemonEntities = pokemonList.map { result ->
                val id = result.url.split("/").dropLast(1).last().toInt()
                PokemonEntity(
                    id = id,
                    name = result.name.replaceFirstChar { it.uppercase() },
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
                )
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonDao.clearAll()
                    remoteKeysDao.clearRemoteKeys()
                }
                val prevKey = if (page == POKEAPI_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys: List<RemoteKeys> = pokemonEntities.map {
                    RemoteKeys(pokemonId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeysDao.insertAll(keys)
                pokemonDao.insertAll(pokemonEntities)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { pokemon ->
            remoteKeysDao.remoteKeysPokemonId(pokemon.id) as RemoteKeys?
        }
    }
}