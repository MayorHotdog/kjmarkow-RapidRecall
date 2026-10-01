package com.example.kjmarkow_rapidrecall.mvc.model

import com.example.kjmarkow_rapidrecall.SequenceLog
import com.example.kjmarkow_rapidrecall.SequenceManager

class MyModel : ObservableModel<MyModel>() {
    fun update() {
        // Do some data stuff
        notifyObservers(this)
    }

    val SL = SequenceLog()
    val SM = SequenceManager(SL)

    fun attemptSequence(attempt: String) {
        SM.attemptSequence(attempt)
    }

    fun hasSequence(): Boolean {
        if (SM.currentSequence == null) {
            return false
        } else {
            return true
        }
    }

    fun startSequence(sequenceLength: Int) {
        SM.generateNewSequence(sequenceLength)
    }
}