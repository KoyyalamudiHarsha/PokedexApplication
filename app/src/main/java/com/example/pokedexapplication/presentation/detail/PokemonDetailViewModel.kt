package com.example.pokedexapplication.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapplication.data.repository.PokemonRepository
import com.example.pokedexapplication.domain.model.PokemonDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState
    data class Success(val details: PokemonDetails) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonName: String = checkNotNull(savedStateHandle["pokemonName"])

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchPokemonDetails()
    }

    private fun fetchPokemonDetails() {
        viewModelScope.launch {
            repository.getPokemonDetails(pokemonName).collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { PokemonDetailUiState.Success(it) },
                    onFailure = { PokemonDetailUiState.Error(it.message ?: "An unknown error occurred") }
                )
            }
        }
    }
}