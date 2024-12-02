package io.github.ekoppenhagen.aoc

import java.io.File

abstract class AbstractAocDay(
    val year: Int,
    val day: Int,
) {

    init {
        printAdventOfCode()
        printDayInfo()
    }

    private fun printAdventOfCode() {
        println("\n---Advent of Code---")
    }

    private fun printDayInfo() {
        println("day $day of year $year\n")
    }

    abstract fun solvePart1(lines: List<String>): Any

    abstract fun solvePart2(lines: List<String>): Any

    fun printResultsOfPart1(): AbstractAocDay {
        printSolution(1, this::solvePart1)
        return this
    }

    fun printResultsOfPart2(): AbstractAocDay {
        printSolution(2, this::solvePart2)
        return this
    }

    private fun printSolution(part: Int, solvingAlgorithm: (List<String>) -> Any) {
        printPart(part)
        printExampleSolution(solvingAlgorithm(readExampleFile(part).lines()))
        printPartSolution(solvingAlgorithm(readInputFile().lines()))
        printEmptyLine()
    }

    private fun printPart(part: Int) {
        println("part $part")
    }

    private fun printExampleSolution(solution: Any) {
        println("example:\t$solution")
    }

    private fun printPartSolution(solution: Any) {
        println("problem:\t$solution")
    }

    private fun printEmptyLine() = println()

    private fun readExampleFile(part: Int) =
        if (File("src/main/resources/$year/Day${day}_example_$part").exists())
            javaClass.getResource("/$year/Day${day}_example_$part")?.readText().orEmpty()
        else javaClass.getResource("/$year/Day${day}_example")?.readText().orEmpty()

    private fun readInputFile() = javaClass.getResource("/$year/Day$day")?.readText().orEmpty()
}
