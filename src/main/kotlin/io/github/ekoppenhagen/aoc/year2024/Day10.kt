package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Location
import io.github.ekoppenhagen.aoc.extensions.toGrid

// https://adventofcode.com/2024/day/10
class Day10 : AbstractAocDay(year = 2024, day = 10) {

    override fun solvePart1(topographicMap: List<String>) =
        calculateTrailheadScores(topographicMap.toGrid()).flatten().sum()

    private fun calculateTrailheadScores(topographicMap: Array<Array<String>>) =
        topographicMap.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, height ->
                if (!isTrailhead(height.toInt())) 0
                else calculateTrailheadScore(rowIndex, columnIndex, topographicMap)
            }
        }

    private fun calculateTrailheadScore(rowIndex: Int, columnIndex: Int, topographicMap: Array<Array<String>>) =
        findHikingPathsToReachablePeaks(rowIndex, columnIndex, topographicMap).map { it.last() }.toSet().size

    private fun getNextHeight(topographicMap: Array<Array<String>>, rowIndex: Int, columnIndex: Int) =
        topographicMap.getOrNull(rowIndex)?.getOrNull(columnIndex)?.toInt()

    private fun isOutsideOfMap(height: Int?) =
        height == null

    private fun isTrailhead(height: Int) =
        height == 0

    private fun isPeak(height: Int) =
        height == 9

    private fun isGradualUphill(previousHeight: Int, currentHeight: Int) =
        currentHeight == previousHeight + 1

    override fun solvePart2(topographicMap: List<String>) =
        calculateNumberOfPathsForAllTrailheads(topographicMap.toGrid()).flatten().sum()

    private fun calculateNumberOfPathsForAllTrailheads(topographicMap: Array<Array<String>>) =
        topographicMap.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, height ->
                if (!isTrailhead(height.toInt())) 0
                else calculateNumberOfPaths(rowIndex, columnIndex, topographicMap)
            }
        }

    private fun calculateNumberOfPaths(rowIndex: Int, columnIndex: Int, topographicMap: Array<Array<String>>) =
        findHikingPathsToReachablePeaks(rowIndex, columnIndex, topographicMap).size

    private fun findNumberOfPaths(currentHeight: Int?, previousHeight: Int, rowIndex: Int, columnIndex: Int, topographicMap: Array<Array<String>>): Int =
        when {
            isOutsideOfMap(currentHeight) -> 0
            !isGradualUphill(previousHeight, currentHeight!!) -> 0
            isPeak(currentHeight) -> 1
            else -> findNumberOfPaths(getNextHeight(topographicMap, rowIndex + 1, columnIndex), currentHeight, rowIndex + 1, columnIndex, topographicMap) +
                findNumberOfPaths(getNextHeight(topographicMap, rowIndex - 1, columnIndex), currentHeight, rowIndex - 1, columnIndex, topographicMap) +
                findNumberOfPaths(getNextHeight(topographicMap, rowIndex, columnIndex + 1), currentHeight, rowIndex, columnIndex + 1, topographicMap) +
                findNumberOfPaths(getNextHeight(topographicMap, rowIndex, columnIndex - 1), currentHeight, rowIndex, columnIndex - 1, topographicMap)
        }

    private fun findHikingPathsToReachablePeaks(rowIndex: Int, columnIndex: Int, topographicMap: Array<Array<String>>) =
        mutableListOf<List<Location>>().apply { findHikingPathsToReachablePeaks(-1, 0, rowIndex, columnIndex, topographicMap) }

    @Suppress("LongParameterList")
    private fun MutableList<List<Location>>.findHikingPathsToReachablePeaks(
        currentHeight: Int?,
        previousHeight: Int,
        rowIndex: Int,
        columnIndex: Int,
        topographicMap: Array<Array<String>>,
        currentRoute: List<Location> = emptyList(),
    ) {
        when {
            isOutsideOfMap(currentHeight) -> return
            !isGradualUphill(previousHeight, currentHeight!!) -> return
            isPeak(currentHeight) -> this.add(currentRoute + Location(rowIndex, columnIndex))
            else -> walkInAllDirections(currentHeight, rowIndex, columnIndex, topographicMap, currentRoute + Location(rowIndex, columnIndex))
        }
    }

    private fun MutableList<List<Location>>.walkInAllDirections(
        currentHeight: Int,
        rowIndex: Int,
        columnIndex: Int,
        topographicMap: Array<Array<String>>,
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
