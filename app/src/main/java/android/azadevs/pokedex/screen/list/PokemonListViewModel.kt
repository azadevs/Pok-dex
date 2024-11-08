package android.azadevs.pokedex.screen.list

import android.azadevs.pokedex.data.models.PokedexListEntry
import android.azadevs.pokedex.data.repository.PokemonRepository
import android.azadevs.pokedex.util.Constants.PAGE_SIZE
import android.azadevs.pokedex.util.Resource
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 04/11/24
 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var currentPage = 0

    var pokemonListState = mutableStateOf(PokemonListState())

    private var cachedPokemonList = listOf<PokedexListEntry>()
    private var isSearchStarting = true

    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if (isSearchStarting) {
            pokemonListState.value.pokemonList
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonListState.value = pokemonListState.value.copy(
                    pokemonList = cachedPokemonList,
                    isSearching = false
                )
                isSearchStarting = true
                return@launch
            }
            val result = listToSearch.filter {
                it.name.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if (isSearchStarting) {
                cachedPokemonList = pokemonListState.value.pokemonList
                isSearchStarting = false
            }
            pokemonListState.value =
                pokemonListState.value.copy(pokemonList = result, isSearching = true)
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            pokemonListState.value = pokemonListState.value.copy(isLoading = true)
            val result =
                repository.getPokemonList(
                    limit = PAGE_SIZE,
                    offset = currentPage * PAGE_SIZE
                )
            when (result) {
                is Resource.Error -> {
                    pokemonListState.value =
                        pokemonListState.value.copy(error = result.message!!, isLoading = false)
                }

                is Resource.Success -> {
                    pokemonListState.value =
                        pokemonListState.value.copy(endReached = currentPage * PAGE_SIZE >= result.data!!.count)
                    val pokedexEntries = result.data.results.map { data ->
                        val number = if (data.url.endsWith("/")) {
                            data.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            data.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(
                            number.toInt(),
                            data.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            url
                        )
                    }
                    currentPage++
                    pokemonListState.value = pokemonListState.value.copy(
                        pokemonList = pokemonListState.value.pokemonList + pokedexEntries,
                        isLoading = false,
                        error = ""
                    )
                }
            }
        }
    }

    fun calculateDominantColor(bitmap: Bitmap, onFinish: (Color) -> Unit) {
        val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}

data class PokemonListState(
    val isLoading: Boolean = false,
    val error: String = "",
    val pokemonList: List<PokedexListEntry> = emptyList(),
    val endReached: Boolean = false,
    val isSearching: Boolean = false
)