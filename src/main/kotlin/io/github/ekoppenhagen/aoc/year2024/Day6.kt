package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Direction
import io.github.ekoppenhagen.aoc.common.Direction.DOWN
import io.github.ekoppenhagen.aoc.common.Direction.LEFT
import io.github.ekoppenhagen.aoc.common.Direction.RIGHT
import io.github.ekoppenhagen.aoc.common.Direction.UP
import io.github.ekoppenhagen.aoc.extensions.toGrid

// https://adventofcode.com/2024/day/6
class Day6 : AbstractAocDay(year = 2024, day = 6) {

    override fun solvePart1(labMap: List<String>) =
        calculateNumberOfDistinctPositionsOfGuardRoute(labMap.toGrid())

    private fun calculateNumberOfDistinctPositionsOfGuardRoute(labGridMap: Array<Array<String>>) =
        getAllPatrolTiles(labGridMap).size

    private fun getAllPatrolTiles(labGridMap: Array<Array<String>>) =
        mutableSetOf(getStartingPositionOfGuard(labGridMap)).apply { runPatrol(labGridMap, this) }

    private fun runPatrol(labGridMap: Array<Array<String>>, patrolTiles: MutableSet<Pair<Int, Int>>) {
        var currentGuardPosition = patrolTiles.first()
        var guardDirection = UP
        var nextGuardPosition = getNextPosition(currentGuardPosition, guardDirection)
        while (isInsideLab(nextGuardPosition, labGridMap)) {
            if (isObstacle(nextGuardPosition, labGridMap)) guardDirection = guardDirection.rotateClockwise()
            else currentGuardPosition = getNextPosition(currentGuardPosition, guardDirection).also { patrolTiles.add(it) }
            nextGuardPosition = getNextPosition(currentGuardPosition, guardDirection)
        }
    }

    private fun getStartingPositionOfGuard(labGridMap: Array<Array<String>>): Pair<Int, Int> =
        labGridMap.let {
            it.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, column ->
                    if (column == "^") return Pair(rowIndex, columnIndex)
                }
            }
            Pair(-1, -1)
        }

    private fun isInsideLab(position: Pair<Int, Int>, labGridMap: Array<Array<String>>) =
        position.first in 0..<labGridMap.size && position.second in 0..<labGridMap.first().size

    private fun getNextPosition(currentGuardPosition: Pair<Int, Int>, direction: Direction) =
        when (direction) {
            UP -> Pair(currentGuardPosition.first - 1, currentGuardPosition.second)
            RIGHT -> Pair(currentGuardPosition.first, currentGuardPosition.second + 1)
            DOWN -> Pair(currentGuardPosition.first + 1, currentGuardPosition.second)
            LEFT -> Pair(currentGuardPosition.first, currentGuardPosition.second - 1)
        }

    private fun isObstacle(position: Pair<Int, Int>, labGridMap: Array<Array<String>>) =
        labGridMap[position.first][position.second] == "#"

    override fun solvePart2(labMap: List<String>) =
        "not implemented"
}
