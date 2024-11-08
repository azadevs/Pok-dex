package android.azadevs.pokedex.data.remote.api

import android.azadevs.pokedex.data.remote.responses.Pokemon
import android.azadevs.pokedex.data.remote.responses.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by : Azamat Kalmurzaev
 * 04/11/24
 */

interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): Pokemon

}