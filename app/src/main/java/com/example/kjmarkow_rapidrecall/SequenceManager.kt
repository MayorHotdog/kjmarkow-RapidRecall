package com.example.kjmarkow_rapidrecall

class SequenceManager(
    val sequenceLog: SequenceLog
) {
    var sequenceLength: Int = -1
    var currentSequence: Sequence? = null


    private var currentSequenceState: SequenceState = SequenceState.CORRECT
    fun attemptSequence(attempt: String) {
        for (char in attempt) {
            currentSequenceState = currentSequence?.attemptSequenceElement(char)!!
        }
        currentSequence?.sequenceCleanUp()
        sequenceLog.addSequence(currentSequence!!)
        currentSequence = null

    }

    // Returns True when a new sequence was generated
    fun generateNewSequence(sequenceLengthPassed: Int): Boolean {
        sequenceLength = sequenceLengthPassed
        if (currentSequence == null) {
            currentSequence = Sequence(sequenceLength)
            currentSequenceState = currentSequence?.currentSequenceState!!
            return true
        }
        // State is CORRECT which means it is not finished
        return false
    }

}