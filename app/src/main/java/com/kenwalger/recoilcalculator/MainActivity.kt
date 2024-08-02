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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kenwalger.recoilcalculator.ui.theme.Comfortable
import com.kenwalger.recoilcalculator.ui.theme.Enjoyable
import com.kenwalger.recoilcalculator.ui.theme.Medic
import com.kenwalger.recoilcalculator.ui.theme.RatherNot
import com.kenwalger.recoilcalculator.ui.theme.RecoilCalculatorTheme
import com.kenwalger.recoilcalculator.ui.theme.Uncomfortable

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

    var isNewShooter by remember { mutableStateOf(false) }

    val recoilEnergy = calRecoilEnergy(rifleWeight, bulletWeight, powderWeight, bulletVelocity)
    val rifleVelocity = calRifleVelocity(rifleWeight, bulletWeight, powderWeight, bulletVelocity)
    val bulletAngularVelocity =
        calBulletAngularVelocity(barrelTwist, bulletVelocity) * 30 / 3.14159265
    val bulletLinearMuzzleEnergy = calBulletLinearMuzzleEnergy(bulletWeight, bulletVelocity)
    val bulletSpinEnergy =
        calBulletSpinEnergy(bulletDiameter, bulletWeight, bulletVelocity, barrelTwist)
    val df = DecimalFormat("###,###.##")

    var comfortColor: Color = getComfortColor(recoilEnergy)

    if (isNewShooter) {
        comfortColor = getNewShooterComfortColor(recoilEnergy)
    }

    HeaderImage(modifier)
    Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)
                ) {
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
                    .background(color = Color.LightGray)) {
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
                    .background(color = Color.LightGray)) {
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
                    .background(color = Color.LightGray)) {
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
                    .background(color = Color.LightGray)) {
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
            Row(modifier = Modifier
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
                Row(modifier = Modifier
                    .width(200.dp)
                    .background(color = Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "New Shooter?")
                    Spacer(modifier = Modifier.padding(start = 18.dp))
                    Switch(
                        checked = isNewShooter,
                        onCheckedChange = {
                            isNewShooter = it
                        }
                    )

                }
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)){
            Row(modifier = Modifier.width(200.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .height(25.dp)
                        .width(25.dp)
                        .background(color = Enjoyable))
                Spacer(modifier = Modifier.padding(start = 9.dp))
                Text("Enjoyable")
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Row(modifier = Modifier.width(200.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .height(25.dp)
                        .width(25.dp)
                        .background(color = Comfortable))
                Spacer(modifier = Modifier.padding(start = 9.dp))
                Text("Comfortable")
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Row(modifier = Modifier.width(200.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .height(25.dp)
                        .width(25.dp)
                        .background(color = Uncomfortable))
                Spacer(modifier = Modifier.padding(start = 9.dp))
                Text("Uncomfortable")
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Row(modifier = Modifier.width(200.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .height(25.dp)
                        .width(25.dp)
                        .background(color = RatherNot))
                Spacer(modifier = Modifier.padding(start = 9.dp))
                Text("I'd Rather Not")
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Row(modifier = Modifier.width(200.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .height(25.dp)
                        .width(25.dp)
                        .background(color = Medic))
                Spacer(modifier = Modifier.padding(start = 9.dp))
                Text("Call a Medic!")
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)) {
            HyperLinkText(modifier = Modifier.fillMaxWidth().padding(10.dp),
                fullText = "Based on the DOS and Java Script Recoil Calculators from Varmint Al's Shooting Page.",
                linkText = listOf("Varmint Al's Shooting Page"),
                hyperlinks = listOf("https://www.varmintal.com/ashot.htm#Calculate_Recoil"))
        }
    }


// Function to make text clickable
@Composable
fun HyperLinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color = Color.Blue,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    hyperlinks: List<String>,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        linkText.forEachIndexed { index, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = hyperlinks[index],
                start = startIndex,
                end = endIndex
            )
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize
            ),
            start = 0,
            end = fullText.length
        )
    }

    val uriHandler = LocalUriHandler.current

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = {
            annotatedString.getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GetInputPreview(modifier: Modifier = Modifier) {
    RecoilCalculatorTheme {
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            GetInput(modifier)
        }
    }
}


@Composable
fun HeaderImage(modifier: Modifier = Modifier) {
    val headerImage = painterResource(R.drawable.bg_compose_recoil_calculator_logo_small)
    Image(modifier = modifier, painter = headerImage, contentDescription = null)

}

