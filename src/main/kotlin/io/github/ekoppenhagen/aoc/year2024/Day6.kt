package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Direction
import io.github.ekoppenhagen.aoc.common.Direction.DOWN
import io.github.ekoppenhagen.aoc.common.Direction.LEFT
import io.github.ekoppenhagen.aoc.common.Direction.RIGHT
import io.github.ekoppenhagen.aoc.common.Direction.UP
import io.github.ekoppenhagen.aoc.common.Grid
import io.github.ekoppenhagen.aoc.common.Location

class Day6 : AbstractAocDay(day = 6) {

    override fun solvePart1(labMap: List<String>) =
        calculateNumberOfDistinctPositionsOfGuardRoute(Grid(labMap))

    private fun calculateNumberOfDistinctPositionsOfGuardRoute(labGridMap: Grid) =
        getAllPatrolTiles(labGridMap).size

    private fun getAllPatrolTiles(labGridMap: Grid) =
        mutableSetOf(getStartingPositionOfGuard(labGridMap)).apply { runPatrol(labGridMap, this) }

    private fun runPatrol(labGridMap: Grid, patrolTiles: MutableSet<Location>) {
        var currentGuardPosition = patrolTiles.first()
        var guardDirection = UP
        var nextGuardPosition = getNextPosition(currentGuardPosition, guardDirection)
        while (isInsideLab(nextGuardPosition, labGridMap)) {
            if (isObstacle(nextGuardPosition, labGridMap)) guardDirection = guardDirection.rotateClockwise()
            else currentGuardPosition = getNextPosition(currentGuardPosition, guardDirection).also { patrolTiles.add(it) }
            nextGuardPosition = getNextPosition(currentGuardPosition, guardDirection)
        }
    }

    private fun getStartingPositionOfGuard(labGridMap: Grid): Location =
        labGridMap.firstLocationOf('^') ?: Location(-1, -1)

    private fun isInsideLab(position: Location, labGridMap: Grid) =
        position.row in 0..<labGridMap.rows && position.column in 0..<labGridMap.columns

    private fun getNextPosition(currentGuardPosition: Location, direction: Direction) =
        when (direction) {
            UP -> Location(currentGuardPosition.row - 1, currentGuardPosition.column)
            RIGHT -> Location(currentGuardPosition.row, currentGuardPosition.column + 1)
            DOWN -> Location(currentGuardPosition.row + 1, currentGuardPosition.column)
            LEFT -> Location(currentGuardPosition.row, currentGuardPosition.column - 1)
        }

    private fun isObstacle(location: Location, labGridMap: Grid) =
        labGridMap.getOrNull(location) == '#'

    override fun solvePart2(labMap: List<String>) =
        "not implemented"
}
