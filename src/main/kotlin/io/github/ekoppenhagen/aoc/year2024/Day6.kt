package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Direction
import io.github.ekoppenhagen.aoc.common.Direction.DOWN
import io.github.ekoppenhagen.aoc.common.Direction.LEFT
import io.github.ekoppenhagen.aoc.common.Direction.RIGHT
import io.github.ekoppenhagen.aoc.common.Direction.UP
import io.github.ekoppenhagen.aoc.common.Location
import io.github.ekoppenhagen.aoc.extensions.toCharacterGrid

// https://adventofcode.com/2024/day/6
class Day6 : AbstractAocDay(year = 2024, day = 6) {

    override fun solvePart1(labMap: List<String>) =
        calculateNumberOfDistinctPositionsOfGuardRoute(labMap.toCharacterGrid())

    private fun calculateNumberOfDistinctPositionsOfGuardRoute(labGridMap: Array<CharArray>) =
        getAllPatrolTiles(labGridMap).size

    private fun getAllPatrolTiles(labGridMap: Array<CharArray>) =
        mutableSetOf(getStartingPositionOfGuard(labGridMap)).apply { runPatrol(labGridMap, this) }

    private fun runPatrol(labGridMap: Array<CharArray>, patrolTiles: MutableSet<Location>) {
        var currentGuardPosition = patrolTiles.first()
        var guardDirection = UP
        var nextGuardPosition = getNextPosition(currentGuardPosition, guardDirection)
        while (isInsideLab(nextGuardPosition, labGridMap)) {
            if (isObstacle(nextGuardPosition, labGridMap)) guardDirection = guardDirection.rotateClockwise()
            else currentGuardPosition = getNextPosition(currentGuardPosition, guardDirection).also { patrolTiles.add(it) }
            nextGuardPosition = getNextPosition(currentGuardPosition, guardDirection)
        }
    }

    @Suppress("UnnecessaryLet") // false positive
    private fun getStartingPositionOfGuard(labGridMap: Array<CharArray>): Location =
        labGridMap.let {
            it.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, column ->
                    if (column == '^') return Location(rowIndex, columnIndex)
                }
            }
            Location(-1, -1)
        }

    private fun isInsideLab(position: Location, labGridMap: Array<CharArray>) =
        position.row in 0..<labGridMap.size && position.column in 0..<labGridMap.first().size

    private fun getNextPosition(currentGuardPosition: Location, direction: Direction) =
        when (direction) {
            UP -> Location(currentGuardPosition.row - 1, currentGuardPosition.column)
            RIGHT -> Location(currentGuardPosition.row, currentGuardPosition.column + 1)
            DOWN -> Location(currentGuardPosition.row + 1, currentGuardPosition.column)
            LEFT -> Location(currentGuardPosition.row, currentGuardPosition.column - 1)
        }

    private fun isObstacle(position: Location, labGridMap: Array<CharArray>) =
        labGridMap[position.row][position.column] == '#'

    override fun solvePart2(labMap: List<String>) =
        "not implemented"
}
