package com.example.kjmarkow_rapidrecall

import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kjmarkow_rapidrecall.mvc.control.MyController
import com.example.kjmarkow_rapidrecall.mvc.model.MyModel
import com.example.kjmarkow_rapidrecall.ui.theme.KjmarkowRapidRecallTheme
import kotlin.math.round

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
                    MainScreen(modifier = Modifier.padding(innerPadding),
                        controller = controller)
//                    Log.d("MyTag", "Your message goes here")
//                    Log.d("MyTag", sequence.sequenceArray.toString())
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, controller: MyController) {
    var sequenceInput by remember { mutableStateOf("") }
    var
    Column() {
        Button(
            onClick = {
                controller.changeScreen(1)
            }
        ) {
            Text("Logs")
        }
        Text("THE SEQUENCE")
        Row() {
            OutlinedTextField(
                value = sequenceInput,
                onValueChange = {sequenceInput=it},
                label = { Text("Sequence Input") },
                enabled = true
            )
            Button(
                onClick = {controller.startSequence()}
            ) {
                Text("Start")
            }
            Button(
                onClick = {controller.submitSequence()}
            ) {
                Text("Submit")
            }
        }

        var sliderPosition by remember { mutableFloatStateOf(0f) }
        Slider(
            modifier = Modifier.size(width=250.dp, height = 50.dp),
            value = round(sliderPosition),
            onValueChange = { sliderPosition = round(it) },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 8,
            valueRange = 1f..10f
        )
//            Text(text = sliderPosition.toString())


    }
}