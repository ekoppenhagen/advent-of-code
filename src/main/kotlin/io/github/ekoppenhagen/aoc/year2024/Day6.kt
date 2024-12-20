package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Direction
import io.github.ekoppenhagen.aoc.common.Direction.DOWN
import io.github.ekoppenhagen.aoc.common.Direction.LEFT
import io.github.ekoppenhagen.aoc.common.Direction.RIGHT
import io.github.ekoppenhagen.aoc.common.Direction.UP
import io.github.ekoppenhagen.aoc.common.Grid
import io.github.ekoppenhagen.aoc.common.Position
import io.github.ekoppenhagen.aoc.common.filterParallel

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
        getUniquePositions(getAllPatrolTilesWithDirection(labGridMap)).size

    private fun getUniquePositions(positionWithDirections: Collection<PositionWithDirection>) =
        positionWithDirections.map { it.position }.toSet()

    private fun getAllPatrolTilesWithDirection(labGridMap: Grid) =
        mutableSetOf(getStartingPatrol(labGridMap))
            .apply { walkPatrol(labGridMap, this) }

    private fun getStartingPatrol(labGridMap: Grid) =
        PositionWithDirection(getStartingPositionOfGuard(labGridMap), UP)

    private fun getStartingPositionOfGuard(labGridMap: Grid) =
        labGridMap.firstPositionOf('^') ?: Position(-1, -1)

    private fun walkPatrol(labGridMap: Grid, patrolTiles: MutableSet<PositionWithDirection>): Boolean {
        var currentGuardPatrol = patrolTiles.first()
        var nextGuardPatrol = continuePatrol(currentGuardPatrol)

        while (isInsideLab(nextGuardPatrol.position, labGridMap)) {
            currentGuardPatrol = getNextGuardPatrol(currentGuardPatrol, nextGuardPatrol, labGridMap)
            if (patrolTiles.contains(currentGuardPatrol)) return true
            patrolTiles.add(currentGuardPatrol)
            nextGuardPatrol = continuePatrol(currentGuardPatrol)
        }
        return false // no loop
    }

    private fun continuePatrol(currentGuardPosition: PositionWithDirection) =
        when (currentGuardPosition.direction) {
            UP -> PositionWithDirection(currentGuardPosition.position.oneRowUp(), UP)
            RIGHT -> PositionWithDirection(currentGuardPosition.position.oneColumnRight(), RIGHT)
            DOWN -> PositionWithDirection(currentGuardPosition.position.oneRowDown(), DOWN)
            LEFT -> PositionWithDirection(currentGuardPosition.position.oneColumnLeft(), LEFT)
        }

    private fun isInsideLab(position: Position, labGridMap: Grid) =
        labGridMap.isInside(position)

    private fun getNextGuardPatrol(
        currentGuardPatrol: PositionWithDirection,
        nextGuardPatrol: PositionWithDirection,
        labGridMap: Grid
    ) =
        if (isObstacle(nextGuardPatrol.position, labGridMap)) {
            currentGuardPatrol.copy(direction = currentGuardPatrol.direction.rotateClockwise())
        } else nextGuardPatrol

    private fun isObstacle(position: Position, labGridMap: Grid) =
        labGridMap.getOrNull(position).let {
            it == '#' || it == 'O'
        }

    data class PositionWithDirection(val position: Position, val direction: Direction)

    override suspend fun solvePart2(labMap: List<String>) =
        calculateNumberOfUniquePositionsToCreateLoop(Grid(labMap))

    private suspend fun calculateNumberOfUniquePositionsToCreateLoop(labGridMap: Grid) =
        getUniquePositions(calculateNumberOfPositionsToCreateLoop(labGridMap)).size

    private suspend fun calculateNumberOfPositionsToCreateLoop(labGridMap: Grid) =
        getAllPatrolTilesWithDirection(labGridMap).let {
            val startPosition = it.first().position
            it.drop(1)
                .filterParallel { positionWithDirection -> isLoopPosition(positionWithDirection.position, labGridMap) }
                .filterNot { positionWithDirection -> positionWithDirection.position == startPosition }
        }

    private fun isLoopPosition(
        obstaclePosition: Position,
        labGridMap: Grid,
    ) = walkPatrol(
        createLapCopyWithNewObstacle(labGridMap, obstaclePosition),
        mutableSetOf(getStartingPatrol(labGridMap)),
    )

    private fun createLapCopyWithNewObstacle(
        labGridMap: Grid,
        obstaclePosition: Position
    ) = labGridMap.copyAndReplace(obstaclePosition, 'O')
}
