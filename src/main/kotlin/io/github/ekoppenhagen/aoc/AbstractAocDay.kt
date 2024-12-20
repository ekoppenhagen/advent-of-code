package io.github.ekoppenhagen.aoc

import java.io.File
import kotlin.reflect.KSuspendFunction1
import kotlin.system.measureTimeMillis

abstract class AbstractAocDay(
    private val exampleResultPart1: Number? = null,
    private val exampleResultPart2: Number? = null,
    private val solveExamples: Boolean = true,
    private val solveParts: Boolean = true,
) {

    private val day = this.javaClass.simpleName.dropWhile { !it.isDigit() }.toInt()
    private val year = this.javaClass.packageName.dropWhile { !it.isDigit() }.toInt()

    private val disabled = "disabled"

    private val adventOfCode = """
            ___       __                 __           ____   ______          __   
           /   | ____/ /   _____  ____  / /_   ____  / __/  / ____/___  ____/ /__ 
          / /| |/ __  / | / / _ \/ __ \/ __/  / __ \/ /_   / /   / __ \/ __  / _ \
         / ___ / /_/ /| |/ /  __/ / / / /_   / /_/ / __/  / /___/ /_/ / /_/ /  __/
        /_/  |_\__,_/ |___/\___/_/ /_/\__/   \____/_/     \____/\____/\__,_/\___/        
    """.trimIndent()

    init {
        printAdventOfCodeHeader()
    }

    suspend fun solve() {
        printCompletePart(1, this::solvePart1, exampleResultPart1)
        printCompletePart(2, this::solvePart2, exampleResultPart2)
    }

    protected abstract suspend fun solvePart1(lines: List<String>): Any

    protected abstract suspend fun solvePart2(lines: List<String>): Any

    private fun printAdventOfCodeHeader() {
        printEmptyLine()
        printAdventOfCode()
        printDayInformation()
        printEmptyLine()
    }

    private fun printEmptyLine() = println()

    private fun printAdventOfCode() = println(adventOfCode)

    private fun printDayInformation() {
        val dateText = "day $day of year $year"
        val urlText = "https://adventofcode.com/$year/day/$day"
        val existingTextLength = (dateText.length + urlText.length + 2)
        val fillingSpaces = " ".repeat(adventOfCode.indexOfFirst { it == '\n' } - existingTextLength)
        println("$dateText$fillingSpaces$urlText")
    }

    private suspend fun printCompletePart(partNumber: Int, solvingAlgorithm: KSuspendFunction1<List<String>, Any>, expectedExampleResult: Number?) {
        printPart(partNumber)
        expectedExampleResult
            ?.also { printExampleAndPartSolution(partNumber, solvingAlgorithm, it) }
            ?: printPartSolutionWithRuntime(solvingAlgorithm)
        printEmptyLine()
    }

    private fun printPart(part: Int) = println("part $part")

    private suspend fun printExampleAndPartSolution(partNumber: Int, solvingAlgorithm: KSuspendFunction1<List<String>, Any>, expectedExampleResult: Number?) {
        (if (solveExamples) solvingAlgorithm(readExampleFile(partNumber).lines()) else disabled)
            .also {
                printExampleSolution("$it ${validateResult(it, expectedExampleResult)}")
                printPartSolutionWithRuntime(solvingAlgorithm)
            }
    }

    private fun validateResult(actual: Any, expected: Number?) =
        if (actual is Number && actual.toLong() == expected?.toLong()) "✔"
        else "✘ ($expected)"

    private fun printExampleSolution(solution: Any) = println("- example:\t$solution")

    private suspend fun printPartSolutionWithRuntime(solvingAlgorithm: KSuspendFunction1<List<String>, Any>) =
        measureTimeMillis {
            printPartSolution(
                if (!solveParts) disabled
                else solvingAlgorithm(readInputFile().lines())
            )
        }.also { println(" (${it}ms)") }

    private fun printPartSolution(solution: Any) = print("- problem:\t$solution")

    private fun readExampleFile(part: Int) =
        if (File("src/main/resources/$year/Day${day}_example_$part").exists())
            javaClass.getResource("/$year/Day${day}_example_$part")?.readText().orEmpty()
        else javaClass.getResource("/$year/Day${day}_example")?.readText().orEmpty()

    private fun readInputFile() = javaClass.getResource("/$year/Day$day")?.readText().orEmpty()
}
