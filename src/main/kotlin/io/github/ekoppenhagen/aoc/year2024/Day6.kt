package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Direction
import io.github.ekoppenhagen.aoc.common.Direction.DOWN
import io.github.ekoppenhagen.aoc.common.Direction.LEFT
import io.github.ekoppenhagen.aoc.common.Direction.RIGHT
import io.github.ekoppenhagen.aoc.common.Direction.UP
import io.github.ekoppenhagen.aoc.common.Grid
import io.github.ekoppenhagen.aoc.common.Position

suspend fun main() {
    Day6().solve()
}

class Day6 : AbstractAocDay(
    exampleResultPart1 = 41,
    exampleResultPart2 = 6,
) {

    override suspend fun solvePart1(labMap: List<String>) =
        calculateNumberOfDistinctPositionsOfGuardRoute(Grid(labMap))

    private fun calculateNumberOfDistinctPositionsOfGuardRoute(labGridMap: Grid) =
        getAllPatrolTiles(labGridMap).size

    private fun getAllPatrolTiles(labGridMap: Grid) =
        mutableSetOf(getStartingPositionOfGuard(labGridMap)).apply { runPatrol(labGridMap, this) }

    private fun runPatrol(labGridMap: Grid, patrolTiles: MutableSet<Position>) {
        var currentGuardPosition = patrolTiles.first()
        var guardDirection = UP
        var nextGuardPosition = getNextPosition(currentGuardPosition, guardDirection)
        while (isInsideLab(nextGuardPosition, labGridMap)) {
            if (isObstacle(nextGuardPosition, labGridMap)) guardDirection = guardDirection.rotateClockwise()
            else currentGuardPosition = getNextPosition(currentGuardPosition, guardDirection).also { patrolTiles.add(it) }
            nextGuardPosition = getNextPosition(currentGuardPosition, guardDirection)
        }
    }

    private fun getStartingPositionOfGuard(labGridMap: Grid): Position =
        labGridMap.firstLocationOf('^') ?: Position(-1, -1)

    private fun isInsideLab(position: Position, labGridMap: Grid) =
        position.row in 0 until labGridMap.rows && position.column in 0 until labGridMap.columns

    private fun getNextPosition(currentGuardPosition: Position, direction: Direction) =
        when (direction) {
            UP -> Position(currentGuardPosition.row - 1, currentGuardPosition.column)
            RIGHT -> Position(currentGuardPosition.row, currentGuardPosition.column + 1)
            DOWN -> Position(currentGuardPosition.row + 1, currentGuardPosition.column)
            LEFT -> Position(currentGuardPosition.row, currentGuardPosition.column - 1)
        }

    private fun isObstacle(position: Position, labGridMap: Grid) =
        labGridMap.getOrNull(position) == '#'

    override suspend fun solvePart2(labMap: List<String>) =
        "not implemented"
}
