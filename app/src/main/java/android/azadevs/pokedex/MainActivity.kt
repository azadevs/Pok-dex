package android.azadevs.pokedex

import android.azadevs.pokedex.screen.detail.PokemonDetailScreen
import android.azadevs.pokedex.screen.list.PokemonListScreen
import android.azadevs.pokedex.ui.theme.PokédexTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            PokédexTheme {
                Scaffold { paddingValues ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "pokemon_list_screen",
                    ) {
                        composable(route = "pokemon_list_screen") {
                            PokemonListScreen(
                                navController = navController,
                                topPadding = paddingValues.calculateTopPadding()
                            )
                        }
                        composable(
                            route = "pokemon_detail_screen/{dominantColor}/{pokemonName}",
                            arguments = listOf(
                                navArgument("dominantColor") {
                                    type = NavType.IntType
                                },
                                navArgument("pokemonName") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val dominantColor = remember {
                                val color = it.arguments?.getInt("dominantColor")
                                color?.let { Color(it) } ?: Color.White
                            }
                            val pokemonName = remember {
                                it.arguments?.getString("pokemonName")
                            }
                            PokemonDetailScreen(
                                dominantColor = dominantColor,
                                pokemonName = pokemonName?.lowercase(java.util.Locale.ROOT) ?: "",
                                navController = navController,
                                topPadding = paddingValues.calculateTopPadding()
                            )
                        }
                    }
                }
            }
        }
    }
}