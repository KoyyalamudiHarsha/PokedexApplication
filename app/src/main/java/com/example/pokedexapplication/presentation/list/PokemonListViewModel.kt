package com.example.pokedexapplication.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokedexapplication.data.local.PokemonEntity
import com.example.pokedexapplication.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    // Private state for the search query
    private val _searchQuery = MutableStateFlow("")
    // Public, read-only state flow for the UI to observe
    val searchQuery = _searchQuery.asStateFlow()

    /**
     * A flow that reactively responds to changes in the search query.
     * - debounce(300L): Waits for 300ms of inactivity before triggering a new search.
     * This prevents excessive database queries while the user is typing.
     * - flatMapLatest: Cancels the previous paging flow and creates a new one
     * with the latest query.
     * - cachedIn(viewModelScope): Caches the PagingData in the ViewModel's scope.
     */
    val pokemonList: Flow<PagingData<PokemonEntity>> = _searchQuery
        .debounce(300L) // Debounce to avoid rapid queries
        .flatMapLatest { query ->
            repository.getPokemonList(query)
        }
        .cachedIn(viewModelScope)

    /**
     * Called by the UI when the user types in the search bar.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
