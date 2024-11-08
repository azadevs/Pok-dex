package android.azadevs.pokedex.util

/**
 * Created by : Azamat Kalmurzaev
 * 04/11/24
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : Resource<T>(data)

    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

}