package com.example.kjmarkow_rapidrecall

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kjmarkow_rapidrecall.mvc.control.MyController
import com.example.kjmarkow_rapidrecall.mvc.control.Screens
import com.example.kjmarkow_rapidrecall.mvc.model.MyModel
import com.example.kjmarkow_rapidrecall.ui.theme.KjmarkowRapidRecallTheme
import kotlinx.coroutines.delay
import kotlin.concurrent.timer
import kotlin.math.round
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : ComponentActivity() {
    private lateinit var controller: MyController
    val model = MyModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = MyController(model)
        enableEdgeToEdge()
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



    if (currentScreen == Screens.MAIN) {
        Column() {
            Button(
                onClick = {
                    controller.changeScreen(Screens.LOG)
                    currentScreen = controller.currentScreen
                }
            ) {
                Text("Logs")
            }
            if (isTimer) {
                Text("$sequenceCharDisplay")
                LaunchedEffect(Unit) {
                    var test = countdownremaining* model.SM.sequenceLength
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
            Row() {
                OutlinedTextField(
                    value = sequenceInput,
                    onValueChange = {
                        if (sequenceInput.length < sliderPosition.toInt()) {
                            sequenceInput = it
                        }
                    },
                    label = { Text("Sequence Input") },
                    enabled = hasSequence
                )
                Button(
                    onClick = {
                        controller.startSequence(sliderPosition.toInt())
                        hasSequence = true
                        sequenceArray = model.SM.currentSequence?.sequenceArray
                        isTimer = true
                    },
                    enabled = !hasSequence

                ) {
                    Text("Start")
                }
                Button(
                    onClick = {
                        controller.submitSequence(sequenceInput)
                        sequenceInput = ""
                        hasSequence = model.hasSequence
                    },
                    enabled = hasSequence && (sliderPosition.toInt() == sequenceInput.length)
                ) {
                    Text("Submit")
                }
            }
            if (hasSequence) {
                Text(sequenceArray.toString())
            }

            Row() {
                Slider(
                    modifier = Modifier.size(width = 250.dp, height = 50.dp),
                    value = round(sliderPosition),
                    onValueChange = { sliderPosition = round(it) },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 8,
                    valueRange = 1f..10f,
                    enabled = !hasSequence
                )
                Text(text = "Sequence Length: $sliderPosition")
            }
        }
    } else if (currentScreen == Screens.LOG) {
        Column() {
            Button(
                onClick = {
                    controller.changeScreen(Screens.MAIN)
                    currentScreen = controller.currentScreen
                }
            ) {
                Text("Back")
            }
            for (sequence in model.SL.loggedSequences) {
                Text("${sequence.sequenceArray}")
            }
        }
    }
}