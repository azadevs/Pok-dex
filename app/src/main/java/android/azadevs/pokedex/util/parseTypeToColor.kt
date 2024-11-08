package android.azadevs.pokedex.util

import android.azadevs.pokedex.data.remote.responses.Stat
import android.azadevs.pokedex.data.remote.responses.Type
import android.azadevs.pokedex.ui.theme.AtkColor
import android.azadevs.pokedex.ui.theme.DefColor
import android.azadevs.pokedex.ui.theme.HPColor
import android.azadevs.pokedex.ui.theme.SpAtkColor
import android.azadevs.pokedex.ui.theme.SpDefColor
import android.azadevs.pokedex.ui.theme.SpdColor
import android.azadevs.pokedex.ui.theme.TypeBug
import android.azadevs.pokedex.ui.theme.TypeDark
import android.azadevs.pokedex.ui.theme.TypeDragon
import android.azadevs.pokedex.ui.theme.TypeElectric
import android.azadevs.pokedex.ui.theme.TypeFairy
import android.azadevs.pokedex.ui.theme.TypeFighting
import android.azadevs.pokedex.ui.theme.TypeFire
import android.azadevs.pokedex.ui.theme.TypeFlying
import android.azadevs.pokedex.ui.theme.TypeGhost
import android.azadevs.pokedex.ui.theme.TypeGrass
import android.azadevs.pokedex.ui.theme.TypeGround
import android.azadevs.pokedex.ui.theme.TypeIce
import android.azadevs.pokedex.ui.theme.TypeNormal
import android.azadevs.pokedex.ui.theme.TypePoison
import android.azadevs.pokedex.ui.theme.TypePsychic
import android.azadevs.pokedex.ui.theme.TypeRock
import android.azadevs.pokedex.ui.theme.TypeSteel
import android.azadevs.pokedex.ui.theme.TypeWater
import androidx.compose.ui.graphics.Color
import java.util.Locale

fun parseTypeToColor(type: Type): Color {
    return when (type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when (stat.stat.name.lowercase(Locale.ROOT)) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when (stat.stat.name.lowercase(Locale.ROOT)) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}