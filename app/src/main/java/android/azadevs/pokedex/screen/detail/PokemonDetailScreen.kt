package android.azadevs.pokedex.screen.detail

import android.azadevs.pokedex.R
import android.azadevs.pokedex.data.remote.responses.Pokemon
import android.azadevs.pokedex.data.remote.responses.Type
import android.azadevs.pokedex.util.parseStatToAbbr
import android.azadevs.pokedex.util.parseStatToColor
import android.azadevs.pokedex.util.parseTypeToColor
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.util.Locale
import kotlin.math.round

/**
 * Created by : Azamat Kalmurzaev
 * 06/11/24
 */
@Composable
fun PokemonDetailScreen(
    modifier: Modifier = Modifier,
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp,
    pokemonImageSize: Dp = 180.dp,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.getPokemonInfo(pokemonName)
    }

    val state = viewModel.pokemonDetailState.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        PokemonDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .fillMaxHeight(0.2f),
            topPadding = topPadding.plus(4.dp)
        )

        PokemonDetailStateWrapper(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, shape = RoundedCornerShape(10.dp))
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center),
            state = state
        )
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
        ) {
            if (state.pokemon != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.pokemon.sprites.frontDefault).crossfade(true).build(),
                    contentDescription = state.pokemon.name,
                    modifier = Modifier
                        .size(pokemonImageSize)
                        .offset(y = topPadding),
                )
            }
        }
    }
}

@Composable
fun PokemonDetailTopSection(
    modifier: Modifier = Modifier, navController: NavController,
    topPadding: Dp
) {
    Box(
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                listOf(Color.Black, Color.Transparent)
            )
        ), contentAlignment = Alignment.TopStart
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(30.dp)
                .offset(16.dp, topPadding)
                .clickable {
                    navController.popBackStack()
                })
    }
}

@Composable
fun PokemonDetailStateWrapper(
    modifier: Modifier = Modifier, loadingModifier: Modifier = Modifier, state: PokemonDetailState
) {
    if (state.isLoading) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary, modifier = loadingModifier
        )
    }
    if (state.error.isNotEmpty()) {
        Text(
            text = state.error, fontSize = 20.sp, modifier = modifier
        )
    }
    if (state.pokemon != null) {
        PokemonDetailSection(
            pokemon = state.pokemon,
            modifier = modifier.offset(y = ((-20).dp))
        )
    }
}


@Composable
fun PokemonDetailSection(
    modifier: Modifier = Modifier,
    pokemon: Pokemon
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokemon.id} ${
                pokemon.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
            }",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        PokemonTypeSection(types = pokemon.types)

        PokemonDetailDataSection(pokemonWeight = pokemon.weight, pokemonHeight = pokemon.height)

        PokemonBaseStats(pokemon = pokemon)
    }

}

@Composable
fun PokemonTypeSection(
    types: List<Type>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        types.forEach { type ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(color = parseTypeToColor(type))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    },
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    modifier: Modifier = Modifier,
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 70.dp
) {
    val pokemonWeightInKg = remember {
        round(pokemonWeight * 100 / 1000f)
    }
    val pokemonHeightInMeters = remember {
        round(pokemonHeight * 100 / 1000f)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )

        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)

        )

        PokemonDetailDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "$dataValue $dataUnit", color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    statHeight: Dp = 30.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPer = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f, label = "",
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(statHeight)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(currentPer.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(text = statName, fontWeight = FontWeight.Bold)
            Text(
                text = (currentPer.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PokemonBaseStats(
    pokemon: Pokemon,
    animDelayPerItem: Int = 100
) {
    val maxBaseStat = remember {
        pokemon.stats.maxOf { it.baseStat }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        pokemon.stats.forEachIndexed { index, stat ->
            PokemonStat(
                statName = parseStatToAbbr(stat),
                statValue = stat.baseStat,
                statMaxValue = maxBaseStat,
                statColor = parseStatToColor(stat),
                animDelay = index * animDelayPerItem
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

}