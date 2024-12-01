package io.github.ekoppenhagen.aoc

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

    fun solveTask1(): AbstractAocDay {
        printResult(1, solve1(getInputFile().lines()))
        return this
    }

    fun solveTask2(): AbstractAocDay {
        printResult(2, solve2(getInputFile().lines()))
        return this
    }

    private fun printResult(taskNumber: Int, result: Any) {
        println("task $taskNumber result: $result")
    }

    abstract fun solve1(lines: List<String>): Any

    abstract fun solve2(lines: List<String>): Any

    private fun getInputFile() = javaClass.getResource("/$year/Day$day")?.readText().orEmpty()
}
