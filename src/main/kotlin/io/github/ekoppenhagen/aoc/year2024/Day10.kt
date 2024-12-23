package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Grid
import io.github.ekoppenhagen.aoc.common.Position

suspend fun main() {
    Day10().solve()
}

class Day10 : AbstractAocDay(
    exampleResultPart1 = 36,
    exampleResultPart2 = 81,
) {

    override suspend fun solvePart1(topographicMap: List<String>) =
        calculateTrailheadScores(Grid(topographicMap)).flatten().sum()

    private fun calculateTrailheadScores(topographicMap: Grid) =
        topographicMap.mapIndexed { rowIndex, columnIndex, height ->
            if (!isTrailhead(height)) 0
            else calculateTrailheadScore(rowIndex, columnIndex, topographicMap)
        }

    private fun isTrailhead(height: Char) =
        height.digitToInt() == 0

    private fun calculateTrailheadScore(rowIndex: Int, columnIndex: Int, topographicMap: Grid) =
        findHikingPathsToReachablePeaks(rowIndex, columnIndex, topographicMap).map { it.last() }.toSet().size

    private fun findHikingPathsToReachablePeaks(rowIndex: Int, columnIndex: Int, topographicMap: Grid) =
        mutableListOf<List<Position>>().apply { findHikingPathsToReachablePeaks(0, -1, rowIndex, columnIndex, topographicMap) }

    @Suppress("LongParameterList", "CanBeNonNullable") // false positive due to recursion
    private fun MutableList<List<Position>>.findHikingPathsToReachablePeaks(
        currentHeight: Int?,
        previousHeight: Int,
        rowIndex: Int,
        columnIndex: Int,
        topographicMap: Grid,
        currentRoute: List<Position> = emptyList(),
    ) {
        when {
            isOutsideOfMap(currentHeight) -> return
            !isGradualUphill(previousHeight, currentHeight!!) -> return
            isPeak(currentHeight) -> this.add(currentRoute + Position(rowIndex, columnIndex))
            else -> walkInAllDirections(currentHeight, rowIndex, columnIndex, topographicMap, currentRoute + Position(rowIndex, columnIndex))
        }
    }

    private fun isOutsideOfMap(height: Int?) =
        height == null

    private fun isGradualUphill(previousHeight: Int, currentHeight: Int) =
        currentHeight == previousHeight + 1

    private fun isPeak(height: Int) =
        height == 9

    @Suppress("LongMethod")
    private fun MutableList<List<Position>>.walkInAllDirections(
        currentHeight: Int,
        rowIndex: Int,
        columnIndex: Int,
        topographicMap: Grid,
        currentRoute: List<Position>,
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

    override suspend fun solvePart2(topographicMap: List<String>) =
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
}
