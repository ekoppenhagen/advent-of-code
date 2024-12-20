package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Grid
import io.github.ekoppenhagen.aoc.common.Position
import io.github.ekoppenhagen.aoc.extensions.getAllNumbers

suspend fun main() {
    Day14().solve()
}

class Day14 : AbstractAocDay(
    exampleResultPart1 = 12,
    exampleResultPart2 = null,
) {

    override suspend fun solvePart1(rawRobotData: List<String>) =
        calculateSafetyFactor(rawRobotData, areaHeight = 103, areaWidth = 101, seconds = 100)
    // example: calculateSafetyFactor(rawRobotData, areaHeight = 7, areaWidth = 11, seconds = 100)

    private fun calculateSafetyFactor(rawRobotData: List<String>, areaHeight: Int, areaWidth: Int, seconds: Int) =
        getRobotPositionsAfterSeconds(rawRobotData, areaHeight, areaWidth, seconds)
            .let { groupInQuadrants(it, areaHeight, areaWidth) }
            .map { it.robotsInQuadrant }
            .reduce { product, factor -> factor * product }

    private fun groupInQuadrants(positions: List<Position>, areaHeight: Int, areaWidth: Int): List<Quadrant> {
        val quadrant1 = Quadrant(0, areaHeight / 2 - 1, 0, areaWidth / 2 - 1)
        val quadrant2 = Quadrant(areaHeight / 2 + 1, areaHeight - 1, 0, areaWidth / 2 - 1)
        val quadrant3 = Quadrant(0, areaHeight / 2 - 1, areaWidth / 2 + 1, areaWidth - 1)
        val quadrant4 = Quadrant(areaHeight / 2 + 1, areaHeight - 1, areaWidth / 2 + 1, areaWidth - 1)
        positions.forEach {
            quadrant1.countIfInQuadrant(it)
            quadrant2.countIfInQuadrant(it)
            quadrant3.countIfInQuadrant(it)
            quadrant4.countIfInQuadrant(it)
        }
        return listOf(quadrant1, quadrant2, quadrant3, quadrant4)
    }

    private fun getRobotPositionsAfterSeconds(rawRobotData: List<String>, areaHeight: Int, areaWidth: Int, seconds: Int) =
        rawRobotData.map { toRobot(it) }.map { calculatePositionAfterSeconds(it, areaHeight, areaWidth, seconds) }

    private fun toRobot(rawRobotData: String) =
        rawRobotData.getAllNumbers(includeNegativeNumbers = true).let {
            Robot(
                startPostion = Position(row = it[1].toInt(), column = it[0].toInt()),
                rowsPerSecond = it[3].toInt(),
                columnsPerSecond = it[2].toInt(),
            )
        }

    private fun calculatePositionAfterSeconds(robot: Robot, areaHeight: Int, areaWidth: Int, seconds: Int) =
        Position(
            row = calculatePosition(robot.startPostion.row, robot.rowsPerSecond, seconds, areaHeight),
            column = calculatePosition(robot.startPostion.column, robot.columnsPerSecond, seconds, areaWidth),
        )

    private fun calculatePosition(coordinate: Int, movementPerSecond: Int, seconds: Int, areaLimit: Int) =
        calculateMovement(coordinate, movementPerSecond, seconds, areaLimit)
            .let {
                when {
                    movementPerSecond >= 0 -> it
                    movementPerSecond < 0 -> (areaLimit + it) % areaLimit
                    else -> error("this should never happen")
                }
            }

    private fun calculateMovement(coordinate: Int, movementPerSecond: Int, seconds: Int, areaLimit: Int) =
        (coordinate + movementPerSecond * seconds) % areaLimit

    private data class Robot(val startPostion: Position, val rowsPerSecond: Int, val columnsPerSecond: Int)

    private data class Quadrant(
        private val minimumRow: Int,
        private val maximumRow: Int,
        private val minimumColumn: Int,
        private val maximumColumn: Int,
    ) {

        var robotsInQuadrant: Int = 0

        fun countIfInQuadrant(position: Position) {
            if (isInBounds(position.row, minimumRow, maximumRow) &&
                isInBounds(position.column, minimumColumn, maximumColumn)
            ) robotsInQuadrant++
        }

        private fun isInBounds(value: Int, minimum: Int, maximum: Int) =
            minimum <= value && value <= maximum
    }

    override suspend fun solvePart2(rawRobotData: List<String>) =
        findAppearanceOfChristmasTree(rawRobotData)

    private fun findAppearanceOfChristmasTree(rawRobotData: List<String>) =
        generateSequence(0) { it + 1 }
            .first { doRobotsShowChristmasTree(rawRobotData, it) }

    private fun doRobotsShowChristmasTree(rawRobotData: List<String>, second: Int): Boolean {
        val resultingGrid = createGridWithRobotsAfterMovement(rawRobotData, second)
        val areas = resultingGrid.findAllAreas(ignoredCharacter = '.')
        // not given in the requirements, but a Christmas tree consists of 229 robots
        return areas.any { it.size == 229 }
    }

    private fun createGridWithRobotsAfterMovement(rawRobotData: List<String>, second: Int): Grid =
        createGrid(getRobotPositionsAfterSeconds(rawRobotData, areaHeight = 103, areaWidth = 101, seconds = second))

    private fun createGrid(positions: List<Position>) =
        Grid(
            createGridList(
                positions.maxBy { it.row }.row + 1,
                positions.maxBy { it.column }.column + 1,
                positions,
            )
        )

    private fun createGridList(rows: Int, columns: Int, positions: List<Position>) =
        mutableListOf<String>().apply {
            repeat(rows) { rowIndex ->
                val row = StringBuilder()
                repeat(columns) { columnIndex ->
                    if (positions.contains(Position(rowIndex, columnIndex))) row.append('#')
                    else row.append('.')
                }
                this.add(row.toString())
            }
        }
}
