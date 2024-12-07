package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.getAllNumbers

// https://adventofcode.com/2024/day/2
class Day2 : AbstractAocDay(year = 2024, day = 2) {

    override fun solvePart1(rawData: List<String>) =
        getNumberOfSafeReports(rawData, isProblemDampenerAvailable = false)

    private fun getNumberOfSafeReports(
        rawData: List<String>,
        isProblemDampenerAvailable: Boolean,
    ) = rawData.count { isReportSafe(it.getAllNumbers(), isProblemDampenerAvailable) }

    private fun isReportSafe(report: List<Long>, isProblemDampenerAvailable: Boolean): Boolean {
        return if (!isProblemDampenerAvailable) isReportSafe(report)
        else isAnyDampenedVersionOfReportSafe(report)
    }

    private fun isAnyDampenedVersionOfReportSafe(report: List<Long>) =
        report.indices.any { isReportSafe(report.toMutableList().apply { removeAt(it) }, false) }

    private fun isReportSafe(report: List<Long>) = areLevelsSafe(report.zipWithNext())

    private fun areLevelsSafe(levels: List<Pair<Long, Long>>) =
        levels.all { (firstLevel, secondLevel) -> areOrderAndDistancesValidForAscending(firstLevel, secondLevel) } ||
            levels.all { (firstLevel, secondLevel) -> areOrderAndDistancesValidForDescending(firstLevel, secondLevel) }

    private fun areOrderAndDistancesValidForAscending(firstLevel: Long, secondLevel: Long) =
        firstLevel < secondLevel && secondLevel - firstLevel in 1L..3L

    private fun areOrderAndDistancesValidForDescending(firstLevel: Long, secondLevel: Long) =
        firstLevel > secondLevel && firstLevel - secondLevel in 1L..3L

    override fun solvePart2(rawData: List<String>) =
        getNumberOfSafeReports(rawData, isProblemDampenerAvailable = true)
}


