package com.kenwalger.recoilcalculator

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kenwalger.recoilcalculator.ui.theme.Comfortable
import com.kenwalger.recoilcalculator.ui.theme.Enjoyable
import com.kenwalger.recoilcalculator.ui.theme.Medic
import com.kenwalger.recoilcalculator.ui.theme.RatherNot
import com.kenwalger.recoilcalculator.ui.theme.RecoilCalculatorTheme
import com.kenwalger.recoilcalculator.ui.theme.Uncomfortable
import com.kenwalger.recoilcalculator.ui.theme.appColor
import com.kenwalger.recoilcalculator.ui.theme.backgroundBoxColor
import com.kenwalger.recoilcalculator.ui.theme.buttonText
import com.kenwalger.recoilcalculator.ui.theme.glaucous
import com.kenwalger.recoilcalculator.ui.theme.gunmetalGray

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecoilCalculatorTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
                {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
        mutableStateOf("8")
    }
    val rifleWeight = rifleWeightInput.toDoubleOrNull() ?: 0.0

    var barrelTwistInput by remember {
        mutableStateOf("10")
    }
    val barrelTwist = barrelTwistInput.toIntOrNull() ?: 0

    var bulletDiameterInput by remember {
        mutableStateOf(".243")
    }
    val bulletDiameter = bulletDiameterInput.toDoubleOrNull() ?: 0.0

    var bulletVelocityInput by remember {
        mutableStateOf("2900")
    }
    val bulletVelocity = bulletVelocityInput.toIntOrNull() ?: 0

    var bulletWeightInput by remember {
        mutableStateOf("100")
    }
    val bulletWeight = bulletWeightInput.toIntOrNull() ?: 0

    var powderWeightInput by remember {
        mutableStateOf("42.0")
    }
    val powderWeight = powderWeightInput.toDoubleOrNull() ?: 0.0

    var isNewShooter by remember { mutableStateOf(false) }

    var recoilEnergy = calRecoilEnergy(rifleWeight, bulletWeight, powderWeight, bulletVelocity)
    val rifleVelocity = calRifleVelocity(rifleWeight, bulletWeight, powderWeight, bulletVelocity)
    val bulletAngularVelocity =
        calBulletAngularVelocity(barrelTwist, bulletVelocity) * 30 / 3.14159265
    val bulletLinearMuzzleEnergy = calBulletLinearMuzzleEnergy(bulletWeight, bulletVelocity)
    val bulletSpinEnergy =
        calBulletSpinEnergy(bulletDiameter, bulletWeight, bulletVelocity, barrelTwist)
    val df = DecimalFormat("###,###.##")

    // Dropdown Menu Variables
    val muzzleDeviceOptionList = listOf("None", "Brake", "Silencer")
    val expanded = remember { mutableStateOf(false) }
    val muzzleDeviceValue = remember { mutableStateOf(muzzleDeviceOptionList[0]) }
    val muzzleBrakeReductionValue = .45
    val silencerReductionValue = .25

    if (muzzleDeviceValue.value == "Brake") {
        recoilEnergy -= (recoilEnergy * muzzleBrakeReductionValue)
    } else if (muzzleDeviceValue.value == "Silencer") {
        recoilEnergy -= (recoilEnergy * silencerReductionValue)
    }

    // Sizes & fonts
    val calculationCardHeight = 485.dp
    val backgroundBoxSize = 18.dp
    val boxSize = 13.dp
    val descriptionFontSize = 16.sp
    val textBoxFontSize = 14.sp
    val leftColumn = 185.dp
    val rightColumn = 210.dp
    val descriptionCardHeight = 160.dp
    val descriptionRowWidth = 300.dp
    val descriptionRowPadding = 2.dp



    var comfortColor: Color = getComfortColor(recoilEnergy)

    if (isNewShooter) {
        comfortColor = getNewShooterComfortColor(recoilEnergy)
    }


    Header(modifier)
    Card(
        colors = CardColors(
            containerColor = gunmetalGray,
            contentColor = Color.Black,
            disabledContentColor = Color.Yellow,
            disabledContainerColor = Color.Yellow
        ),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(calculationCardHeight)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = modifier.width(leftColumn)) {
                    TextField(
                        value = rifleWeightInput,
                        textStyle = TextStyle.Default.copy(fontSize = textBoxFontSize),
                        onValueChange = { rifleWeightInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.rifle_weight_in_pounds) ) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .width(rightColumn)
                        .background(color = comfortColor)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = textBoxFontSize,
                        text = stringResource(
                            R.string.rifle_recoil_energy_ft_lbs,
                            df.format(recoilEnergy)
                        )
                    )
                }
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = modifier.width(leftColumn)) {
                    TextField(
                        value = barrelTwistInput,
                        textStyle = TextStyle.Default.copy(fontSize = textBoxFontSize),
                        onValueChange = { barrelTwistInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.barrel_twist_in_revolutions_per_inch)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = modifier
                        .width(rightColumn)
                        .background(color = Color.LightGray)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = textBoxFontSize,
                        text = stringResource(R.string.rifle_velocity_fps, df.format(rifleVelocity))
                    )
                }
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = modifier.width(leftColumn)) {
                    TextField(
                        value = bulletDiameterInput,
                        textStyle = TextStyle.Default.copy(fontSize = textBoxFontSize),
                        onValueChange = { bulletDiameterInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.bullet_diameter_in_inches)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = modifier
                        .width(rightColumn)
                        .background(color = Color.LightGray)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = textBoxFontSize,
                        text = stringResource(
                            R.string.bullet_angular_velocity_rpm,
                            df.format(bulletAngularVelocity)
                        )
                    )
                }
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = modifier.width(leftColumn)) {
                    TextField(
                        value = bulletVelocityInput,
                        textStyle = TextStyle.Default.copy(fontSize = textBoxFontSize),
                        onValueChange = { bulletVelocityInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.bullet_velocity_fps)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = modifier
                        .width(rightColumn)
                        .background(color = Color.LightGray)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = textBoxFontSize,
                        text = stringResource(
                            R.string.bullet_linear_muzzle_energy_ft_lbs,
                            df.format(bulletLinearMuzzleEnergy)
                        )
                    )
                }
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = modifier.width(leftColumn)) {
                    TextField(
                        value = bulletWeightInput,
                        textStyle = TextStyle.Default.copy(fontSize = textBoxFontSize),
                        onValueChange = { bulletWeightInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.bullet_weight_in_grains)) }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = modifier
                        .width(rightColumn)
                        .background(color = Color.LightGray)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontSize = textBoxFontSize,
                        text = stringResource(
                            R.string.bullet_spin_energy_ft_lbs,
                            df.format(bulletSpinEnergy)
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = modifier
                        .width(leftColumn)
                        .height(60.dp)
                ) {
                    TextField(
                        value = powderWeightInput,
                        textStyle = TextStyle.Default.copy(fontSize = textBoxFontSize),
                        onValueChange = { powderWeightInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.powder_weight_in_grains)) }
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            Row(
                modifier = Modifier
                    .height(40.dp)
                    .background(color = Color.Transparent),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                    Spacer(modifier = Modifier.padding(start = 10.dp))
                    Text(
                        modifier = Modifier,
                        text = "New Shooter?",
                        fontSize = textBoxFontSize,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(start = 5.dp))
                    Switch(
                        modifier = Modifier.scale(.90f),
                        checked = isNewShooter,
                        onCheckedChange = {
                            isNewShooter = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = glaucous,
                            checkedTrackColor = Color.White,
                            uncheckedThumbColor = Color.Black,
                            uncheckedTrackColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.padding(start = 18.dp))
                    Text(
                        modifier = Modifier,
                        text = "Muzzle Device",
                        fontSize = textBoxFontSize,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(start = 5.dp))
                    // muzzleDeviceMenu()

                    // Dropdown Menu for Muzzle Device
                    Surface(modifier = Modifier.fillMaxSize()) {

                        Box(modifier = Modifier.fillMaxWidth()) {

                            Row(modifier = Modifier
                                .clickable { expanded.value = !expanded.value }
                                .align(Alignment.Center)) {
                                Text(text = muzzleDeviceValue.value)
                                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)

                                DropdownMenu(expanded = expanded.value, onDismissRequest = {
                                    expanded.value = false
                                }) {
                                    muzzleDeviceOptionList.forEach {
                                        DropdownMenuItem(text = { Text(text = it) }, onClick = {
                                            muzzleDeviceValue.value = it
                                            expanded.value = false
                                        })
                                    }
                                }

                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(end = 10.dp))
            }
            Spacer(modifier = Modifier.padding(start = 5.dp))
        }
    }

    // Comfort Description Card
    if (!isNewShooter) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(descriptionCardHeight),

            colors = CardColors(
                containerColor = gunmetalGray,
                contentColor = Color.Black,
                disabledContentColor = Color.Yellow,
                disabledContainerColor = Color.Yellow
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = Enjoyable)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.enjoyable),
                        fontSize = descriptionFontSize
                    )
                }
                Spacer(modifier = Modifier.padding(descriptionRowPadding))
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = Comfortable)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.comfortable),
                        fontSize = descriptionFontSize
                    )
                }
                Spacer(modifier = Modifier.padding(descriptionRowPadding))
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = Uncomfortable)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.uncomfortable),
                        fontSize = descriptionFontSize
                    )
                }
                Spacer(modifier = Modifier.padding(descriptionRowPadding))
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = RatherNot)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.rather_not),
                        fontSize = descriptionFontSize
                    )
                }
                Spacer(modifier = Modifier.padding(descriptionRowPadding))
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = Medic)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.medic),
                        fontSize = descriptionFontSize
                    )
                }
            }
        }
    } else {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(descriptionCardHeight),
            colors = CardColors(
                containerColor = gunmetalGray,
                contentColor = Color.Black,
                disabledContentColor = Color.Yellow,
                disabledContainerColor = Color.Yellow
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = Enjoyable)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.enjoyable_new_shooter),
                        fontSize = descriptionFontSize
                    )
                }
                Spacer(modifier = Modifier.padding(descriptionRowPadding))
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = Comfortable)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.comfortable_new_shooter),
                        fontSize = descriptionFontSize
                    )
                }
                Spacer(modifier = Modifier.padding(descriptionRowPadding))
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = Uncomfortable)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.uncomfortable_new_shooter),
                        fontSize = descriptionFontSize
                    )
                }
                Spacer(modifier = Modifier.padding(descriptionRowPadding))
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = RatherNot)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.rather_not_new_shooter),
                        fontSize = descriptionFontSize
                    )
                }
                Spacer(modifier = Modifier.padding(descriptionRowPadding))
                Row(
                    modifier = Modifier.width(descriptionRowWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Box(
                            Modifier
                                .height(backgroundBoxSize)
                                .width(backgroundBoxSize)
                                .background(color = backgroundBoxColor)
                        )
                        Box(
                            Modifier
                                .height(boxSize)
                                .width(boxSize)
                                .background(color = Medic)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.padding(start = 9.dp))
                    Text(
                        stringResource(R.string.medic_new_shooter),
                        fontSize = descriptionFontSize
                    )
                }
            }
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        HyperLinkText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            fullText = stringResource(R.string.attribution),
            linkText = listOf("Varmint Al's Shooting Page"),
            hyperlinks = listOf("https://www.varmintal.com/ashot.htm#Calculate_Recoil")
        )
    }
    Spacer(modifier = Modifier.padding(descriptionRowPadding))
    val shouldShowDialog = remember { mutableStateOf(false) }

    if (shouldShowDialog.value) {
        DisclaimerDialog(shouldShowDialog = shouldShowDialog)
    }

    Button(
        onClick = { shouldShowDialog.value = true },
        modifier = Modifier.wrapContentSize(),
        colors = ButtonDefaults.buttonColors(
            containerColor = appColor,
            contentColor = buttonText
        ),
    ) {
        Text(text = stringResource(R.string.disclaimer))
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
    fontSize: TextUnit = TextUnit.Unspecified,
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
fun Header(modifier: Modifier = Modifier) {
    val castoroFont = FontFamily(
        Font(R.font.castoro_regular, FontWeight.Normal)
    )

    Spacer(modifier = Modifier.padding(20.dp))
    Card(modifier = Modifier) {
        Column(modifier) {
            Row(
                modifier = Modifier
                    .background(appColor)
                    .padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Rifle Recoil Calculator",
                    textAlign = TextAlign.Center,
                    fontFamily = castoroFont,
                    style = LocalTextStyle.current.copy(fontSize = 30.sp)

                )

            }
        }
    }
}


// Dialog Box
val dialogBoxHeight = 375.dp
val dialogBoxPadding = 16.dp
val dialogBoxCorner = 16.dp

@Composable
fun DisclaimerDialog(
    onDismissRequest: (() -> Unit)? = null,
    shouldShowDialog: MutableState<Boolean>
) {
    if (shouldShowDialog.value) {
        Dialog(onDismissRequest = {
            if (onDismissRequest != null) {
                onDismissRequest()
                shouldShowDialog.value = false
            }
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dialogBoxHeight)
                    .padding(dialogBoxPadding),
                shape = RoundedCornerShape(dialogBoxCorner),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.dialog_disclaimer),
                        modifier = Modifier.padding(dialogBoxPadding),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = {
                                shouldShowDialog.value = false
                            },
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = appColor,
                                contentColor = buttonText
                            ),
                        ) {
                            Text(stringResource(R.string.dismiss))
                        }
                    }
                }
            }
        }
    }
}