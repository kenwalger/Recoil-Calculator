package com.kenwalger.recoilcalculator

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kenwalger.recoilcalculator.ui.theme.Comfortable
import com.kenwalger.recoilcalculator.ui.theme.Enjoyable
import com.kenwalger.recoilcalculator.ui.theme.Medic
import com.kenwalger.recoilcalculator.ui.theme.Purple80
import com.kenwalger.recoilcalculator.ui.theme.RatherNot
import com.kenwalger.recoilcalculator.ui.theme.RecoilCalculatorTheme
import com.kenwalger.recoilcalculator.ui.theme.Uncomfortable
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecoilCalculatorTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center))
                {
                    Column(modifier = Modifier, horizontalAlignment = Alignment.Start) {
                        GetInput(modifier = Modifier)
                    }
                }
            }
        }
    }
}

@Composable
fun GetInput(modifier: Modifier = Modifier) {

    var rifleWeightInput by remember {
        mutableStateOf("20")
    }
    val rifleWeight = rifleWeightInput.toDoubleOrNull() ?: 0.0

    var barrelTwistInput by remember {
        mutableStateOf("8")
    }
    val barrelTwist = barrelTwistInput.toIntOrNull() ?: 0

    var bulletDiameterInput by remember {
        mutableStateOf(".264")
    }
    val bulletDiameter = bulletDiameterInput.toDoubleOrNull() ?: 0.0

    var bulletVelocityInput by remember {
        mutableStateOf("2800")
    }
    val bulletVelocity = bulletVelocityInput.toIntOrNull() ?: 0

    var bulletWeightInput by remember {
        mutableStateOf("140")
    }
    val bulletWeight = bulletWeightInput.toIntOrNull() ?: 0

    var powderWeightInput by remember {
        mutableStateOf("44.3")
    }
    val powderWeight = powderWeightInput.toDoubleOrNull() ?: 0.0

    val recoilEnergy = calRecoilEnergy(rifleWeight, bulletWeight, powderWeight, bulletVelocity)
    val rifleVelocity = calRifleVelocity(rifleWeight, bulletWeight, powderWeight, bulletVelocity)
    val bulletAngularVelocity =
        calBulletAngularVelocity(barrelTwist, bulletVelocity) * 30 / 3.14159265
    val bulletLinearMuzzleEnergy = calBulletLinearMuzzleEnergy(bulletWeight, bulletVelocity)
    val bulletSpinEnergy =
        calBulletSpinEnergy(bulletDiameter, bulletWeight, bulletVelocity, barrelTwist)
    val df = DecimalFormat("###,###.##")

    val comfortColor: Color = if (recoilEnergy > 49.999999999) {
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



    HeaderImage(modifier)
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)) {
                Box(modifier = modifier.width(200.dp)) {
                    TextField(
                        value = rifleWeightInput,
                        textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                        onValueChange = { rifleWeightInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.rifle_weight_in_pounds)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier
                    .width(200.dp)
                    .background(color = comfortColor)) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = 12.sp,
                        text = stringResource(
                            R.string.rifle_recoil_energy_ft_lbs,
                            df.format(recoilEnergy)
                        )
                    )
                }
            }
            Row(modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)) {
                Box(modifier = modifier.width(200.dp)) {
                    TextField(
                        value = barrelTwistInput,
                        textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                        onValueChange = { barrelTwistInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.barrel_twist_in_revolutions_per_inch)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = modifier
                    .width(200.dp)
                    .background(color = Color.Gray)) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = 12.sp,
                        text = stringResource(R.string.rifle_velocity_fps, df.format(rifleVelocity))
                    )
                }
            }
            Row(modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)) {
                Box(modifier = modifier.width(200.dp)) {
                    TextField(
                        value = bulletDiameterInput,
                        textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                        onValueChange = { bulletDiameterInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.bullet_diameter_in_inches)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = modifier
                    .width(200.dp)
                    .background(color = Color.Gray)) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = 12.sp,
                        text = stringResource(
                            R.string.bullet_angular_velocity_rpm,
                            df.format(bulletAngularVelocity)
                        )
                    )
                }
            }
            Row(modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)) {
                Box(modifier = modifier.width(200.dp)) {
                    TextField(
                        value = bulletVelocityInput,
                        textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                        onValueChange = { bulletVelocityInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.bullet_velocity_fps)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = modifier
                    .width(200.dp)
                    .background(color = Color.Gray)) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = 12.sp,
                        text = stringResource(
                            R.string.bullet_linear_muzzle_energy_ft_lbs,
                            df.format(bulletLinearMuzzleEnergy)
                        )
                    )
                }
            }
            Row(modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)) {
                Box(modifier = modifier.width(200.dp)) {
                    TextField(
                        value = bulletWeightInput,
                        textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                        onValueChange = { bulletWeightInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.bullet_weight_in_grains)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = modifier
                    .width(200.dp)
                    .background(color = Color.Gray)) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = 12.sp,
                        text = stringResource(
                            R.string.bullet_spin_energy_ft_lbs,
                            df.format(bulletSpinEnergy)
                        )
                    )
                }
            }
            Row(modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)) {
                Box(modifier = modifier.width(200.dp)) {
                    TextField(
                        value = powderWeightInput,
                        textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                        onValueChange = { powderWeightInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.powder_weight_in_grains)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = modifier
                    .width(200.dp)
                    .background(color = Color.Transparent)) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = 12.sp,
                        text = " "
                    )
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun GetInputPreview(modifier: Modifier = Modifier) {
    RecoilCalculatorTheme {
        Column(modifier, horizontalAlignment = Alignment.Start) {
            GetInput(modifier)
        }
    }
}

@Composable
fun HeaderImage(modifier: Modifier = Modifier) {
    val headerImage = painterResource(R.drawable.bg_compose_recoil_calculator_logo_small)
    Image(painter = headerImage, contentDescription = null)

}

// Rifle Recoil Energy Calculation (ft-lb)
// RECOIL = .5 * (BulletMass * Bullet Velocity + Mass of Powder * Velocity of Gas) ^2 / Rifle Mass
fun calRecoilEnergy(rifleWeightPounds: Double,
                    bulletWeightGrains: Int,
                    powderWeightGrains: Double,
                    bulletVelocity: Int) : Double {
    val gravityForce = 32.16
    val bulletMass = bulletWeightGrains / (7000 * gravityForce)
    val powderMass = powderWeightGrains / (7000 * gravityForce)
    val velocityOfGas = 4700
    val rifleMass = rifleWeightPounds / gravityForce

    val recoilEnergy =
        .5 * (bulletMass * bulletVelocity + powderMass * velocityOfGas).pow(2) / rifleMass

    return recoilEnergy
}

// Rifle Velocity Calculation (fps)
fun calRifleVelocity(rifleWeightPounds: Double,
                     bulletWeightGrains: Int,
                     powderWeightGrains: Double,
                     bulletVelocity: Int): Double {
    val gravityForce = 32.16                                                                                       // 32.16 lbm*ft/(lbf*sec^2)
    val rifleMass = rifleWeightPounds / gravityForce                                                               // Lbm
    val bulletMass = bulletWeightGrains / (7000 * gravityForce)                                                    // Lbm
    val powderMass = powderWeightGrains / (7000 * gravityForce)                                                    // Lbm
    val velocityOfGas = 4700                                                                            // fps

    val rifleVelocity = (bulletMass * bulletVelocity + powderMass * velocityOfGas) / rifleMass

    return rifleVelocity
}

// Bullet Angular Velocity (Radians per second)
// VIN = bulletVelocity * 12
// TWIST = 1 / barrel twist
// Angular Velocity = VIN * TWIST * 2 * PI
fun calBulletAngularVelocity(twistRate: Int, velocity: Int): Double {
    val pi = 3.14159265

    val twist: Double= (1 / twistRate.toDouble())                                                                           // rev/inch
    val bulletVelocityInchesPerSecond = velocity * 12                                                            // in/sec
    val bulletAngularVelocity = bulletVelocityInchesPerSecond * twist * 2 * pi                                   // radians/sec

    return bulletAngularVelocity

}

// Bullet Linear Muzzle Energy Calculation (ft-lb)
// KE = .5 * BM * VIN ^ 2 / 12:     REM Bullet linear energy (ft-lb)
fun calBulletLinearMuzzleEnergy(bulletWeightGrains: Int, velocity: Int ): Double {
    val gravityForce = 386                                                                                  // Gravity 386 (lbm-in)/(lbf-sec^2)

    val bulletMass: Double = (bulletWeightGrains / (7000 * gravityForce.toDouble()))                                                      // lbm
    val bulletVelocityInchesPerSecond = velocity * 12                                                            // in/sec
    val bulletLinearMuzzleEnergy = .5 * bulletMass * (bulletVelocityInchesPerSecond * bulletVelocityInchesPerSecond) / 12       // ft-lb

    return bulletLinearMuzzleEnergy
}

// Bullet Spin Energy Calculation (ft-lb)
// Bullet Angular Energy (Spin Energy) = .5 * BulletMassMOI * Bullet Angular Velocity^2 / 12
fun calBulletSpinEnergy(diameter: Double, mass: Int, velocity: Int, twist: Int): Double {
    val gravityForce = 386
    val radius = diameter / 2
    val bulletMass = mass / (7000 * gravityForce.toDouble())
    val bulletMassMOI = .5 * bulletMass * radius.pow(2)

    val bulletAngularEnergy = calBulletAngularVelocity(twist, velocity)

    val bulletSpinEnergy = (.5 * bulletMassMOI * bulletAngularEnergy.pow(2)) / 12

    return bulletSpinEnergy
}

