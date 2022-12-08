package com.example.save_your_train.ui.exercises.activeExercises

data class ExercisesCaseModel(
    var exec: Boolean = false,
    var repet: Boolean = false,
    var rest: Boolean = false,
    var weight: Boolean = false,
) {

    fun compare(first: ExercisesCaseModel, second: ExercisesCaseModel) : Boolean {
        return first.exec == second.exec && first.repet == second.repet && first.rest == second.rest && first.weight == second.weight
    }
}
