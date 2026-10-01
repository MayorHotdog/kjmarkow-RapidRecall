package com.example.kjmarkow_rapidrecall

class SequenceManager(
    val sequenceLog: SequenceLog
) {
    var sequenceLength: Int = -1
    private var currentSequence: Sequence? = null
    private var currentSequenceState: SequenceState = SequenceState.CORRECT
    fun attemptSequenceElement(attempt: Int) {
        currentSequenceState = currentSequence?.attemptSequenceElement(attempt)!!
        if (currentSequenceState == SequenceState.COMPLETE) {
            sequenceLog.addSequence(currentSequence!!)
        }
    }

    // Returns True when a new sequence was generated
    fun generateNewSequence(): Boolean {
        if (currentSequenceState != SequenceState.CORRECT) {
            currentSequence = Sequence(sequenceLength)
            currentSequenceState = currentSequence?.currentSequenceState!!
            return true
        }
        // State is CORRECT which means it is not finished
        return false
    }

}