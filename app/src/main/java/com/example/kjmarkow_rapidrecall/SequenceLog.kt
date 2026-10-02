package com.example.kjmarkow_rapidrecall

class SequenceLog {
    val loggedSequences: MutableList<Sequence> = mutableListOf()
    var totalAttempts: Int = 0
    var totalCorrectAttempts: Int = 0
    var totalAccuracy: Float = 0f
    fun addSequence(sequence: Sequence) {
        loggedSequences.add(sequence)
        if (sequence.isCorrect) {
            totalCorrectAttempts += 1
        }
        totalAttempts += 1
        recalculateAccuracy()

    }
    fun removeSequence(sequence: Sequence) {
        loggedSequences.remove(sequence)
    }

    private fun recalculateAccuracy() {
        totalAccuracy = (totalCorrectAttempts/totalAttempts.toFloat()*100)
    }


}