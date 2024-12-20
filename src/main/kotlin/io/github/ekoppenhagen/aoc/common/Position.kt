package io.github.ekoppenhagen.aoc.common

data class Position(
    val row: Int,
    val column: Int,
) {

    fun rowUp() =
        Position(row = row - 1, column = column)

    fun diagonalUpRight() =
        Position(row = row - 1, column = column + 1)

    fun columnRight() =
        Position(row = row, column = column + 1)

    fun diagonalDownRight() =
        Position(row = row + 1, column = column + 1)

    fun rowDown() =
        Position(row = row + 1, column = column)

    fun diagonalDownLeft() =
        Position(row = row + 1, column = column - 1)

    fun columnLeft() =
        Position(row = row, column = column - 1)

    fun diagonalUpLeft() =
        Position(row = row - 1, column = column - 1)
}
