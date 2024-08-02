package com.kenwalger.recoilcalculator

import androidx.compose.ui.graphics.Color
import com.kenwalger.recoilcalculator.ui.theme.Comfortable
import com.kenwalger.recoilcalculator.ui.theme.Enjoyable
import com.kenwalger.recoilcalculator.ui.theme.Medic
import com.kenwalger.recoilcalculator.ui.theme.Purple80
import com.kenwalger.recoilcalculator.ui.theme.RatherNot
import com.kenwalger.recoilcalculator.ui.theme.Uncomfortable

fun getComfortColor(recoilEnergy: Double): Color {
    return if (recoilEnergy > 49.999999999) {
        Medic
    } else if (recoilEnergy > 29.999999999) {
        RatherNot
    } else if (recoilEnergy > 19.999999999) {
        Uncomfortable
    } else if (recoilEnergy > 9.999999999) {
        Comfortable
    } else if (recoilEnergy > 0) {
        Enjoyable
    } else {
        Purple80
    }
}

fun getNewShooterComfortColor(recoilEnergy: Double): Color {
    return if (recoilEnergy > 49.999999999/2) {
        Medic
    } else if (recoilEnergy > 29.999999999/2) {
        RatherNot
    } else if (recoilEnergy > 19.999999999/2) {
        Uncomfortable
    } else if (recoilEnergy > 9.999999999/2) {
        Comfortable
    } else if (recoilEnergy > 0) {
        Enjoyable
    } else {
        Purple80
    }
}