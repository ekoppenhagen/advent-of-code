package io.github.ekoppenhagen.aoc

import java.io.File

abstract class AbstractAocDay(private val day: Int) {

    private val year = this.javaClass.packageName.dropWhile { !it.isDigit() }.toInt()

    private val adventOfCode = """
            ___       __                 __           ____  ______          __   
           /   | ____/ /   _____  ____  / /_   ____  / __/ / ____/___  ____/ /__ 
          / /| |/ __  / | / / _ \/ __ \/ __/  / __ \/ /_  / /   / __ \/ __  / _ \
         / ___ / /_/ /| |/ /  __/ / / / /_   / /_/ / __/ / /___/ /_/ / /_/ /  __/
        /_/  |_\__,_/ |___/\___/_/ /_/\__/   \____/_/    \____/\____/\__,_/\___/        
    """.trimIndent()

    init {
        printEmptyLine()
        printAdventOfCode()
        printDayInformation()
        printEmptyLine()
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

    private fun printAdventOfCode() = println(adventOfCode)

    private fun printDayInformation() {
        val dateText = "day $day of year $year"
        val urlText = "https://adventofcode.com/$year/day/$day"
        val existingTextLength = (dateText.length + urlText.length + 2)
        val fillingSpaces = " ".repeat(adventOfCode.indexOfFirst { it == '\n' } - existingTextLength)
        println("$dateText$fillingSpaces$urlText")
    }

    private fun printPart(part: Int) = println("part $part")

    private fun printExampleSolution(solution: Any) = println("example:\t$solution")

    private fun printPartSolution(solution: Any) = println("problem:\t$solution")

    private fun printEmptyLine() = println()

    private fun readExampleFile(part: Int) =
        if (File("src/main/resources/$year/Day${day}_example_$part").exists())
            javaClass.getResource("/$year/Day${day}_example_$part")?.readText().orEmpty()
        else javaClass.getResource("/$year/Day${day}_example")?.readText().orEmpty()

    private fun readInputFile() = javaClass.getResource("/$year/Day$day")?.readText().orEmpty()
}
