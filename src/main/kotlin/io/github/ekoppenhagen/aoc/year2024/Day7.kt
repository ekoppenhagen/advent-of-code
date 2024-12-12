package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.concat
import io.github.ekoppenhagen.aoc.extensions.getAllNumbers

class Day7 : AbstractAocDay(
    exampleResultPart1 = 3749,
    exampleResultPart2 = 11_387,
) {

    override fun solvePart1(calibrationEquations: List<String>) =
        getTestValuesOfValidEquations(calibrationEquations).sum()

    private fun getTestValuesOfValidEquations(calibrationEquations: List<String>, useMissingOperator: Boolean = false) =
        findValidEquations(calibrationEquations, useMissingOperator).map { it.first }

    private fun findValidEquations(calibrationEquations: List<String>, useMissingOperator: Boolean) =
        calibrationEquations.map { getEquationParts(it) }.filter { isValidEquation(it, useMissingOperator) }

    private fun getEquationParts(rawCalibrationEquation: String) =
        rawCalibrationEquation.getAllNumbers().let { it.first() to it.drop(1) }

    private fun isValidEquation(calibrationEquation: Pair<Long, List<Long>>, useMissingOperator: Boolean) =
        isPossibleCombination(calibrationEquation.first, useMissingOperator, calibrationEquation.second.first(), calibrationEquation.second.drop(1))

    private fun isPossibleCombination(testValue: Long, useMissingOperator: Boolean, combinationResult: Long, numbers: List<Long>): Boolean =
        when {
            combinationResult > testValue -> false
            numbers.isEmpty() -> testValue == combinationResult
            else -> isPossibleCombination(testValue, useMissingOperator, combinationResult + numbers.first(), numbers.drop(1)) ||
                isPossibleCombination(testValue, useMissingOperator, combinationResult * numbers.first(), numbers.drop(1)) ||
                if (!useMissingOperator) false
                else isPossibleCombination(testValue, true, combinationResult.concat(numbers.first()), numbers.drop(1))
        }

    override fun solvePart2(calibrationEquations: List<String>) =
        getTestValuesOfValidEquations(calibrationEquations, true).sum()
}
