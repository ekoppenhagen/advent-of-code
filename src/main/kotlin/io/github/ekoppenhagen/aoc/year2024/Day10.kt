package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Grid
import io.github.ekoppenhagen.aoc.common.Location

class Day10 : AbstractAocDay(day = 10) {

    override fun solvePart1(topographicMap: List<String>) =
        calculateTrailheadScores(Grid(topographicMap)).flatten().sum()

    private fun calculateTrailheadScores(topographicMap: Grid) =
        topographicMap.mapIndexed { rowIndex, columnIndex, height ->
            if (!isTrailhead(height)) 0
            else calculateTrailheadScore(rowIndex, columnIndex, topographicMap)
        }

    private fun calculateTrailheadScore(rowIndex: Int, columnIndex: Int, topographicMap: Grid) =
        findHikingPathsToReachablePeaks(rowIndex, columnIndex, topographicMap).map { it.last() }.toSet().size

    private fun findHikingPathsToReachablePeaks(rowIndex: Int, columnIndex: Int, topographicMap: Grid) =
        mutableListOf<List<Location>>().apply { findHikingPathsToReachablePeaks(-1, 0, rowIndex, columnIndex, topographicMap) }

    @Suppress("LongParameterList", "CanBeNonNullable") // false positive due to recursion
    private fun MutableList<List<Location>>.findHikingPathsToReachablePeaks(
        currentHeight: Int?,
        previousHeight: Int,
        rowIndex: Int,
        columnIndex: Int,
        topographicMap: Grid,
        currentRoute: List<Location> = emptyList(),
    ) {
        when {
            isOutsideOfMap(currentHeight) -> return
            !isGradualUphill(previousHeight, currentHeight!!) -> return
            isPeak(currentHeight) -> this.add(currentRoute + Location(rowIndex, columnIndex))
            else -> walkInAllDirections(currentHeight, rowIndex, columnIndex, topographicMap, currentRoute + Location(rowIndex, columnIndex))
        }
    }

    private fun isOutsideOfMap(height: Int?) =
        height == null

    private fun isTrailhead(height: Char) =
        height.digitToInt() == 0

    private fun isPeak(height: Int) =
        height == 9

    private fun isGradualUphill(previousHeight: Int, currentHeight: Int) =
        currentHeight == previousHeight + 1

    override fun solvePart2(topographicMap: List<String>) =
        calculateNumberOfPathsForAllTrailheads(Grid(topographicMap)).flatten().sum()

    private fun calculateNumberOfPathsForAllTrailheads(topographicMap: Grid) =
        topographicMap.mapIndexed { rowIndex, columnIndex, height ->
            if (!isTrailhead(height)) 0
            else calculateNumberOfPaths(rowIndex, columnIndex, topographicMap)
        }

    private fun calculateNumberOfPaths(rowIndex: Int, columnIndex: Int, topographicMap: Grid) =
        findHikingPathsToReachablePeaks(rowIndex, columnIndex, topographicMap).size

    @Suppress("CanBeNonNullable") // false positive due to recursion
    private fun findNumberOfPaths(currentHeight: Int?, previousHeight: Int, rowIndex: Int, columnIndex: Int, topographicMap: Grid): Int =
        when {
            isOutsideOfMap(currentHeight) -> 0
            !isGradualUphill(previousHeight, currentHeight!!) -> 0
            isPeak(currentHeight) -> 1
            else -> findNumberOfPaths(getNextHeight(topographicMap, rowIndex + 1, columnIndex), currentHeight, rowIndex + 1, columnIndex, topographicMap) +
                findNumberOfPaths(getNextHeight(topographicMap, rowIndex - 1, columnIndex), currentHeight, rowIndex - 1, columnIndex, topographicMap) +
                findNumberOfPaths(getNextHeight(topographicMap, rowIndex, columnIndex + 1), currentHeight, rowIndex, columnIndex + 1, topographicMap) +
                findNumberOfPaths(getNextHeight(topographicMap, rowIndex, columnIndex - 1), currentHeight, rowIndex, columnIndex - 1, topographicMap)
        }

    private fun getNextHeight(topographicMap: Grid, rowIndex: Int, columnIndex: Int) =
        topographicMap.getOrNull(rowIndex, columnIndex)?.digitToInt()

    @Suppress("LongMethod")
    private fun MutableList<List<Location>>.walkInAllDirections(
        currentHeight: Int,
        rowIndex: Int,
        columnIndex: Int,
        topographicMap: Grid,
        currentRoute: List<Location>,
    ) {
        findHikingPathsToReachablePeaks(
            currentHeight = getNextHeight(topographicMap, rowIndex + 1, columnIndex),
            previousHeight = currentHeight,
            rowIndex + 1,
            columnIndex,
            topographicMap,
            currentRoute = currentRoute,
        )
        findHikingPathsToReachablePeaks(
            currentHeight = getNextHeight(topographicMap, rowIndex - 1, columnIndex),
            previousHeight = currentHeight,
            rowIndex - 1,
            columnIndex,
            topographicMap,
            currentRoute = currentRoute,
        )
        findHikingPathsToReachablePeaks(
            currentHeight = getNextHeight(topographicMap, rowIndex, columnIndex + 1),
            previousHeight = currentHeight,
            rowIndex,
            columnIndex + 1,
            topographicMap,
            currentRoute = currentRoute,
        )
        findHikingPathsToReachablePeaks(
            currentHeight = getNextHeight(topographicMap, rowIndex, columnIndex - 1),
            previousHeight = currentHeight,
            rowIndex,
            columnIndex - 1,
            topographicMap,
            currentRoute = currentRoute,
        )
    }
}
