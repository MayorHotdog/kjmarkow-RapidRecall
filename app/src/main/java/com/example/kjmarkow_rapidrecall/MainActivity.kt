package com.example.kjmarkow_rapidrecall

import android.R
import android.os.Bundle
import android.os.CountDownTimer
import android.view.RoundedCorner
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kjmarkow_rapidrecall.mvc.control.MyController
import com.example.kjmarkow_rapidrecall.mvc.control.Screens
import com.example.kjmarkow_rapidrecall.mvc.model.MyModel
import com.example.kjmarkow_rapidrecall.ui.theme.KjmarkowRapidRecallTheme
import kotlinx.coroutines.delay
import kotlin.concurrent.timer
import kotlin.math.round
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

class MainActivity : ComponentActivity() {
    private lateinit var controller: MyController
    val model = MyModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = MyController(model)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            KjmarkowRapidRecallTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        controller = controller,
                        model = model
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, controller: MyController, model: MyModel) {
    var sequenceInput by remember { mutableStateOf("") }
    var hasSequence by remember { mutableStateOf(model.hasSequence)}
    var sliderPosition by remember { mutableFloatStateOf(1f) }
    var sequenceArray: MutableList<Char>? by remember {  mutableStateOf(mutableListOf<Char>('a')) }
    var currentScreen by remember { mutableStateOf(controller.currentScreen)}
    var isTimer by remember { mutableStateOf(false) }
    var sequenceCharDisplay: Char by remember {  mutableStateOf(' ') }
    var countdownremaining by remember { mutableIntStateOf(1) }
    var index by remember { mutableIntStateOf(0) }

    val buttonBackground = Color(0xFFB22234)
    val buttonText = Color.Black
    val buttonBackgroundDisabled = Color(0xFF8F1F2B)
    val buttonTextDisabled = Color(0xFF2E2E2E)
    val buttonCornerRadius = 4.dp
    val buttonWidth = 100.dp

    val backgroundColor = Color(0xFFF2EAD9)

    if (currentScreen == Screens.MAIN) {
        Surface(modifier = Modifier.fillMaxSize(1f),
            color = backgroundColor
        )
        {
            Column(
                Modifier.fillMaxWidth(1f),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(20.dp))
                Row(
                    Modifier.align(Alignment.Start),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = {
                            controller.changeScreen(Screens.LOG)
                            currentScreen = controller.currentScreen
                        },
                        enabled = !isTimer,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonBackground, // Button background
                            contentColor = buttonText,          // Text and icon color
                            disabledContainerColor = buttonBackgroundDisabled, // Background when disabled
                            disabledContentColor = buttonTextDisabled // Disabled Text
                        ),
                        shape = RoundedCornerShape(buttonCornerRadius),
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text("Logs")
                    }
                    Button(
                        onClick = {
                            controller.changeScreen(Screens.SUMMARY)
                            currentScreen = controller.currentScreen
                        },
                        enabled = !isTimer,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonBackground, // Button background
                            contentColor = buttonText,          // Text and icon color
                            disabledContainerColor = buttonBackgroundDisabled, // Background when disabled
                            disabledContentColor = buttonTextDisabled // Disabled Text
                        ),
                        shape = RoundedCornerShape(buttonCornerRadius),
                    ) {
                        Text("Summary")
                    }
                }
                Box()
                {
                    Spacer(Modifier.height(20.dp))
                    if (isTimer) {
                        val sequence_char_start = "* ".repeat(index)
                        val sequence_char_end = "* ".repeat(model.SM.sequenceLength - index - 1)
                        Text("${sequence_char_start}$sequenceCharDisplay ${sequence_char_end}", color = buttonText)
                        LaunchedEffect(Unit) {
                            var test = countdownremaining * model.SM.sequenceLength
                            while (test > 0) {
                                delay(1000.milliseconds)
                                test -= 1
                                if (!sequenceArray.isNullOrEmpty()) {
                                    sequenceCharDisplay = sequenceArray!![index]
                                    index += 1
                                }
                            }
                            isTimer = false
                        }
                        sequenceCharDisplay = sequenceArray!![index]

                    } else {
                        sequenceCharDisplay = ' '
                        index = 0
                    }
                }
                Row() {
                    OutlinedTextField(
                        value = sequenceInput,
                        onValueChange = {
                            if (sequenceInput.length < sliderPosition.toInt()) {
                                sequenceInput = it
                            }
                        },
                        label = { Text("Sequence Input") },
                        enabled = hasSequence && !isTimer,
                        colors = TextFieldDefaults.colors(
                            disabledTextColor = buttonTextDisabled,
                            disabledContainerColor = buttonBackgroundDisabled,
                            focusedTextColor = buttonText,
                            unfocusedTextColor = buttonText,
                            focusedContainerColor = buttonBackground,
                            unfocusedContainerColor = buttonBackground,
                            focusedLabelColor = buttonTextDisabled,
                            disabledLabelColor = buttonTextDisabled,
                            focusedIndicatorColor = buttonBackgroundDisabled,
                            unfocusedIndicatorColor = buttonBackgroundDisabled

                        )
                    )

                }
                Row() {
                    Button(
                        onClick = {
                            controller.startSequence(sliderPosition.toInt())
                            hasSequence = true
                            sequenceArray = model.SM.currentSequence?.sequenceArray
                            isTimer = true
                        },
                        enabled = !hasSequence,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonBackground, // Button background
                            contentColor = buttonText,          // Text and icon color
                            disabledContainerColor = buttonBackgroundDisabled, // Background when disabled
                            disabledContentColor = buttonTextDisabled // Disabled Text
                        ),
                        shape = RoundedCornerShape(buttonCornerRadius),
                        modifier = Modifier.width(buttonWidth)

                    ) {
                        Text("Start")
                    }
                    Button(
                        onClick = {
                            controller.submitSequence(sequenceInput)
                            sequenceInput = ""
                            hasSequence = model.hasSequence
                        },
                        enabled = hasSequence && (sliderPosition.toInt() == sequenceInput.length),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonBackground, // Button background
                            contentColor = buttonText,          // Text and icon color
                            disabledContainerColor = buttonBackgroundDisabled, // Background when disabled
                            disabledContentColor = buttonTextDisabled // Disabled Text
                        ),
                        shape = RoundedCornerShape(buttonCornerRadius),
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text("Submit")
                    }
                }
//                if (hasSequence) {
//                    Text(sequenceArray.toString(), color = buttonText)
//                }

                Column() {
                    Slider(
                        modifier = Modifier.size(width = 250.dp, height = 50.dp),
                        value = round(sliderPosition),
                        onValueChange = { sliderPosition = round(it) },
                        colors = SliderDefaults.colors(
                            thumbColor = buttonBackground,
                            disabledThumbColor = buttonBackgroundDisabled,
                            activeTrackColor = buttonBackground,
                            disabledActiveTrackColor = buttonBackgroundDisabled,
                            inactiveTrackColor = buttonBackgroundDisabled,
                            disabledInactiveTrackColor = buttonBackgroundDisabled,
                            disabledInactiveTickColor = buttonText,
                            activeTickColor = buttonText,
                            inactiveTickColor = buttonTextDisabled,
                            disabledActiveTickColor = buttonTextDisabled
                        ),
                        steps = 8,
                        valueRange = 1f..10f,
                        enabled = !hasSequence
                    )
                    Text(text = "Sequence Length: ${sliderPosition.toInt()}", color = buttonText)
                }
                if (!hasSequence && (model.SL.loggedSequences.isNotEmpty())) {
                    if (model.SL.loggedSequences.last().isCorrect) {
                        Text("YOUR ANSWER WAS RIGHT!", color = buttonText)
                    } else {
                        Text("Your answer was wrong :(", color = buttonText)
                        Text("You got ${model.SL.loggedSequences.last().sequenceLength - model.SL.loggedSequences.last().correctAmount} digits incorrect", color = buttonText)
                    }
                }

            }
        }
    } else if (currentScreen == Screens.LOG) {
        /*  val buttonBackground = Color(0xFFB22234)
            val buttonText = Color.Black
            val buttonBackgroundDisabled = Color(0xFF8F1F2B)
            val buttonTextDisabled = Color(0xFF2E2E2E)
            val buttonCornerRadius = 4.dp
            val buttonWidth = 100.dp */
        Surface(modifier = Modifier.fillMaxSize(1f),
            color = backgroundColor
        )
        {
            Spacer(Modifier.height(5.dp))
            Column(Modifier.fillMaxWidth(1f),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(20.dp))
                Row(Modifier.align(Alignment.Start),
                    horizontalArrangement = Arrangement.Start) {
                    Button(
                        onClick = {
                            controller.changeScreen(Screens.MAIN)
                            currentScreen = controller.currentScreen
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonBackground, // Button background
                            contentColor = buttonText,          // Text and icon color
                            disabledContainerColor = buttonBackgroundDisabled, // Background when disabled
                            disabledContentColor = buttonTextDisabled // Disabled Text
                        ),
                        shape = RoundedCornerShape(buttonCornerRadius),
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text("Back")
                    }
                }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(model.SL.loggedSequences) { sequence ->
                        Card(Modifier.fillMaxWidth(0.8f), colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFC9C9C1)
                        )) { log_row(sequence) }
                    }
                }

            }
        }
    } else if (currentScreen == Screens.SUMMARY) {
        Surface(modifier = Modifier.fillMaxSize(1f),
            color = backgroundColor
        )
        {
            Column() {
            Spacer(Modifier.height(20.dp))
            Column(Modifier.fillMaxWidth(1f),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.align(Alignment.Start),
                    horizontalArrangement = Arrangement.Start) {
                    Button(
                        onClick = {
                            controller.changeScreen(Screens.MAIN)
                            currentScreen = controller.currentScreen
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonBackground, // Button background
                            contentColor = buttonText,          // Text and icon color
                            disabledContainerColor = buttonBackgroundDisabled, // Background when disabled
                            disabledContentColor = buttonTextDisabled // Disabled Text
                        ),
                        shape = RoundedCornerShape(buttonCornerRadius),
                        modifier = Modifier.width(buttonWidth)
                    ) {
                        Text("Back")
                    }

                }
                Surface(
                    color = Color(0xFFC9C9C1),
                    shape = RoundedCornerShape(8.dp)
                )
                {
                    Column()
                    {
                        Text("Total Attempts: ${model.SL.totalAttempts}", color = buttonText)
                        Text("Total Correct: ${model.SL.totalCorrectAttempts}", color = buttonText)
                        Text("Total Accuracy: ${model.SL.totalAccuracy}%", color = buttonText)
                    }
                }

            }
        }}
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun log_row(sequence: Sequence) {
    val buttonBackground = Color(0xFFB22234)
    val buttonText = Color.Black
    val buttonBackgroundDisabled = Color(0xFF8F1F2B)
    val buttonTextDisabled = Color(0xFF2E2E2E)
    val buttonCornerRadius = 4.dp
    val buttonWidth = 100.dp
    Spacer(Modifier.height(5.dp))
    Row() {
        Column() {
            Text("Correct: ${sequence.isCorrect}", color = buttonTextDisabled)
            Text("Sequence Length: ${sequence.sequenceLength}", color = buttonTextDisabled)
        }
        Spacer(Modifier.width(50.dp))
        Column() {
            Text("Attempted Sequence:\t${sequence.sequenceAttempt}", color = buttonTextDisabled)
            Text("Actual Sequence:\t${sequence.sequenceArray}",  color = buttonTextDisabled)
            Text("TimeStamp:${sequence.FinishedTimestamp}",  color = buttonTextDisabled)
        }

    }


}