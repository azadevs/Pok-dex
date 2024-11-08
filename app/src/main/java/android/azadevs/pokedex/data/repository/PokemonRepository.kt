package android.azadevs.pokedex.data.repository

import android.azadevs.pokedex.data.remote.api.PokeApi
import android.azadevs.pokedex.data.remote.responses.PokemonListResponse
import android.azadevs.pokedex.data.remote.responses.Pokemon
import android.azadevs.pokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.io.IOException
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 04/11/24
 */
@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApi
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonListResponse> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: IOException) {
            return Resource.Error("No internet connection")
        } catch (e: Exception) {
            return Resource.Error("Unknown error")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(pokemonName)
        } catch (e: IOException) {
            return Resource.Error("No internet connection")
        } catch (e: Exception) {
            return Resource.Error("Unknown error")
        }
        return Resource.Success(response)
    }

}