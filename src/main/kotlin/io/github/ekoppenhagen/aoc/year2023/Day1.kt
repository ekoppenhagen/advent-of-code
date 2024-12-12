package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.concatenateToNumber
import io.github.ekoppenhagen.aoc.extensions.toListOfDigits
import io.github.ekoppenhagen.aoc.extensions.toListOfDigitsWithSpelledOutNumbers
import io.github.ekoppenhagen.aoc.extensions.toPairOfFirstAndLastElement

class Day1 : AbstractAocDay(
    exampleResultPart1 = 142,
    exampleResultPart2 = 281,
) {

    override fun solvePart1(lines: List<String>) =
        getEachCalibrationValue(lines).sum()

    private fun getEachCalibrationValue(lines: List<String>) =
        lines.map(::getCalibrationValueFromLine)

    private fun getCalibrationValueFromLine(line: String) =
        line.toListOfDigits()
            .toPairOfFirstAndLastElement()
            .concatenateToNumber() ?: 0

    override fun solvePart2(lines: List<String>) =
        getEachCalibrationValueWithSpelledOutNumbers(lines).sum()

    private fun getEachCalibrationValueWithSpelledOutNumbers(lines: List<String>) =
        lines.map(::getCalibrationValueFromLineWithSpelledOutNumbers)

    private fun getCalibrationValueFromLineWithSpelledOutNumbers(line: String) =
        line.toListOfDigitsWithSpelledOutNumbers()
            .toPairOfFirstAndLastElement()
            .concatenateToNumber() ?: 0
}
