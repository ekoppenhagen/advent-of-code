package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay

suspend fun main() {
    Day3().solve()
}

class Day3 : AbstractAocDay(
    exampleResultPart1 = 161,
    exampleResultPart2 = 48,
) {

    override suspend fun solvePart1(corruptedMemoryDump: List<String>) =
        calculateSumOfAllMultiplicationResults(corruptedMemoryDump)

    private fun calculateSumOfAllMultiplicationResults(corruptedMemoryDump: List<String>) =
        findAllMultiplications(corruptedMemoryDump).sumOf(::addMultiplicationsOfCorruptedMemory)

    private fun addMultiplicationsOfCorruptedMemory(corruptedMemory: List<Pair<Int, Int>>) =
        corruptedMemory.sumOf { it.first * it.second }

    private fun findAllMultiplications(corruptedMemoryDump: List<String>) =
        corruptedMemoryDump.map(::findMultiplications)

    private fun findMultiplications(corruptedMemory: String) =
        getMultiplicationMatcher().findAll(corruptedMemory).map { extractNumbers(it.value) }.toList()

    private fun getMultiplicationMatcher() =
        "mul\\([0-9]{1,3},[0-9]{1,3}\\)".toRegex()

    private fun extractNumbers(multiplication: String) =
        multiplication.substringAfter("(").substringBefore(")")
            .split(",").map { it.toInt() }
            .zipWithNext().first()

    override suspend fun solvePart2(corruptedMemoryDump: List<String>) =
        calculateSumOfAllMultiplicationResultsWithConditionals(corruptedMemoryDump)

    private var isDisabled = false

    private fun calculateSumOfAllMultiplicationResultsWithConditionals(corruptedMemoryDump: List<String>) =
        findAllMultiplicationsWithConditionals(corruptedMemoryDump).sumOf(::addMultiplicationsOfCorruptedMemory)

    private fun findAllMultiplicationsWithConditionals(corruptedMemoryDump: List<String>) =
        corruptedMemoryDump.map { filterDisabledMultiplications(findMultiplicationsWithConditionals(it)) }

    private fun findMultiplicationsWithConditionals(corruptedMemory: String) =
        getMultiplicationMatcherWithConditionals().findAll(corruptedMemory).map { it.value }.toList()

    private fun getMultiplicationMatcherWithConditionals() =
        "(mul\\([0-9]{1,3},[0-9]{1,3}\\)|don't\\(\\)|do\\(\\))".toRegex()

    private fun filterDisabledMultiplications(multiplicationsAndConditionals: List<String>) =
        multiplicationsAndConditionals.filter { toEvaluate ->
            (isMultiplication(toEvaluate) && !isDisabled).also {
                when {
                    isEnablingConditional(toEvaluate) -> isDisabled = false
                    isDisablingConditional(toEvaluate) -> isDisabled = true
                }
            }
        }.map(::extractNumbers)

    private fun isMultiplication(string: String) = string.contains("mul")

    private fun isEnablingConditional(string: String) = string.contains("do()")

    private fun isDisablingConditional(string: String) = string.contains("don't()")
}
