package com.example.kjmarkow_rapidrecall.mvc.control

import com.example.kjmarkow_rapidrecall.mvc.model.MyModel
enum class Screens {
    MAIN, LOG, SUMMARY
}
class MyController(
    private val model: MyModel
) {
    fun onUserAction() {
        // Validate Input and modify the model
    }

    var currentScreen: Screens = Screens.MAIN
    fun changeScreen(screen: Screens) {
        currentScreen = screen
    }

    fun submitSequence(sequence: String) {
        model.attemptSequence(sequence)
    }

    fun startSequence(sequenceLength: Int) {
        model.startSequence(sequenceLength)
    }
}