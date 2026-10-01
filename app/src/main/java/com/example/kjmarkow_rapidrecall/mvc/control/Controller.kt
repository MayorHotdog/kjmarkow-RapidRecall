package com.example.kjmarkow_rapidrecall.mvc.control

import com.example.kjmarkow_rapidrecall.mvc.model.MyModel

class MyController(
    private val model: MyModel
) {
    fun onUserAction() {
        // Validate Input and modify the model

    }
    fun changeScreen(screen: Int) {}

    fun submitSequence(sequence: String) {
        model.attemptSequence(sequence)
    }

    fun startSequence(sequenceLength: Int) {
        model.startSequence(sequenceLength)
    }
}