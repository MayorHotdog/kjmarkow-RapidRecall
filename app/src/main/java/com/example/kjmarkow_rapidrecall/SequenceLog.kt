package com.example.kjmarkow_rapidrecall

class SequenceLog {
    val loggedSequences: MutableList<Sequence> = mutableListOf()
    fun addSequence(sequence: Sequence) {
        loggedSequences.add(sequence)
    }
    fun removeSequence(sequence: Sequence) {
        loggedSequences.remove(sequence)
    }
}