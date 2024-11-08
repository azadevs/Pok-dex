package android.azadevs.pokedex.screen.detail

import android.azadevs.pokedex.data.remote.responses.Pokemon
import android.azadevs.pokedex.data.repository.PokemonRepository
import android.azadevs.pokedex.util.Resource
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 06/11/24
 */
@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    var pokemonDetailState = mutableStateOf(PokemonDetailState())

    fun getPokemonInfo(pokemonName: String) {
        pokemonDetailState.value = pokemonDetailState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = repository.getPokemonInfo(pokemonName)) {
                is Resource.Error -> {
                    pokemonDetailState.value = pokemonDetailState.value.copy(
                        error = result.message ?: "Unknown error",
                        isLoading = false
                    )
                }

                is Resource.Success -> {
                    pokemonDetailState.value = pokemonDetailState.value.copy(
                        pokemon = result.data,
                        isLoading = false
                    )
                }
            }
        }

    }
}

data class PokemonDetailState(
    val isLoading: Boolean = false,
    val pokemon: Pokemon? = null,
    val error: String = ""
)