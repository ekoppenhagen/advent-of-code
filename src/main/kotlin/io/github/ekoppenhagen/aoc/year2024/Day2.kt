package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay

// https://adventofcode.com/2024/day/2
class Day2 : AbstractAocDay(year = 2024, day = 2) {

    override fun solvePart1(rawData: List<String>) =
        getNumberOfSafeReports(rawData, isProblemDampenerAvailable = false)

    private fun getNumberOfSafeReports(
        rawData: List<String>,
        isProblemDampenerAvailable: Boolean,
    ) = rawData.count { isReportSafe(toReport(it), isProblemDampenerAvailable) }

    private fun toReport(line: String) = line.split(" ").map { it.toInt() }

    private fun isReportSafe(report: List<Int>, isProblemDampenerAvailable: Boolean): Boolean {
        return if (!isProblemDampenerAvailable) isReportSafe(report)
        else report.indices.any { isReportSafe(report.toMutableList().apply { removeAt(it) }, false) }
    }

    private fun isReportSafe(report: List<Int>) = areLevelsSafe(report.zipWithNext())

    private fun areLevelsSafe(levels: List<Pair<Int, Int>>) =
        levels.all { (firstLevel, secondLevel) -> areOrderAndDistancesValidForAscending(firstLevel, secondLevel) } ||
            levels.all { (firstLevel, secondLevel) -> areOrderAndDistancesValidForDescending(firstLevel, secondLevel) }

    private fun areOrderAndDistancesValidForAscending(firstLevel: Int, secondLevel: Int) =
        firstLevel < secondLevel && secondLevel - firstLevel in 1..3

    private fun areOrderAndDistancesValidForDescending(firstLevel: Int, secondLevel: Int) =
        firstLevel > secondLevel && firstLevel - secondLevel in 1..3

    override fun solvePart2(rawData: List<String>) =
        getNumberOfSafeReports(rawData, isProblemDampenerAvailable = true)
}


