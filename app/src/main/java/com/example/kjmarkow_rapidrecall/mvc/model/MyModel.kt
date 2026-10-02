package com.example.kjmarkow_rapidrecall.mvc.model

import com.example.kjmarkow_rapidrecall.SequenceLog
import com.example.kjmarkow_rapidrecall.SequenceManager
import com.example.kjmarkow_rapidrecall.SequenceState

class MyModel : ObservableModel<MyModel>() {
    fun update() {
        // Do some data stuff
        notifyObservers(this)
    }

    val SL = SequenceLog()
    val SM = SequenceManager(SL)

    var hasSequence: Boolean = false

    fun attemptSequence(attempt: String) {
        SM.attemptSequence(attempt)
        if (SM.currentSequence?.currentSequenceState != SequenceState.CORRECT) {
            hasSequence = false
        }
    }


    fun startSequence(sequenceLength: Int) {
        SM.generateNewSequence(sequenceLength)
    }
}