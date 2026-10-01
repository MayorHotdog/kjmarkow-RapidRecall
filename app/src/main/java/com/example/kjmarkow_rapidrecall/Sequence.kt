package com.example.kjmarkow_rapidrecall

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

enum class SequenceState {
    CORRECT, INCORRECT, COMPLETE
}
class Sequence(
    var sequenceLength: Int
) {

    public var sequenceArray: MutableList<Char> = generateSequence(sequenceLength)
    public  var sequenceAttempt: MutableList<Char> = mutableListOf()
    var currentSequenceState: SequenceState = SequenceState.CORRECT
    @OptIn(ExperimentalTime::class)
    private var FinishedTimestamp: Instant? = null

    fun attemptSequenceElement(attempt: Char): SequenceState {
        if (currentSequenceState != SequenceState.COMPLETE) {
            sequenceAttempt.add(attempt)
            currentSequenceState = updateSequenceState()
            return currentSequenceState
        } else {
            return currentSequenceState
        }
    }

    private fun generateSequence(sequenceLength: Int): MutableList<Char> {
        var generatedSequence: MutableList<Char> = mutableListOf()
        for (i in 0..<sequenceLength) {
            val randomDigit: Int = (0..9).random()
            generatedSequence.add(randomDigit.digitToChar())
        }
        return generatedSequence
    }

    @OptIn(ExperimentalTime::class)
    private fun updateSequenceState(): SequenceState {
        // If the two sequences match return CORRECT
        // If the two sequences match, and they are both of the final length return COMPLETE
        // If there is a discrepancy return INCORRECT
        val sequenceAttemptLength = sequenceAttempt.size
        if (sequenceAttempt == sequenceArray.subList(0, sequenceAttemptLength)) {
            if (sequenceAttemptLength == sequenceLength) {
                FinishedTimestamp = Clock.System.now()
                return SequenceState.COMPLETE
            } else {
                return SequenceState.CORRECT
            }
        } else {
            return SequenceState.INCORRECT
        }
    }
}