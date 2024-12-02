package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.rotateClockwise
import kotlin.math.abs

// https://adventofcode.com/2024/day/1
class Day1 : AbstractAocDay(year = 2024, day = 1) {

    override fun solvePart1(lines: List<String>) =
        calculateTotalDistances(toSortedListOfLocationIds(lines))

    private fun toSortedListOfLocationIds(lines: List<String>) =
        toListsOfLocationIds(lines).apply { forEach { it.sort() } }

    private fun toListsOfLocationIds(lines: List<String>) =
        lines.map { toLocationIdsOfBothSides(it) }
            .rotateClockwise()

    private fun toLocationIdsOfBothSides(string: String) =
        string.split("   ").map { it.toInt() }

    private fun calculateTotalDistances(lists: MutableList<MutableList<Int>>) =
        lists[0].zip(lists[1]).sumOf { abs(it.first - it.second) }

    override fun solvePart2(lines: List<String>) =
        calculateSimilarityScore(toListsOfLocationIds(lines))

    private fun calculateSimilarityScore(lists: MutableList<MutableList<Int>>) =
        lists[0].sumOf { leftLocationId ->
            leftLocationId *
                lists[1].count { rightLocationId ->
                    rightLocationId == leftLocationId
                }
        }
}
