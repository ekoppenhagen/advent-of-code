package io.github.ekoppenhagen.aoc.common

data class Position(
    val row: Int,
    val column: Int,
) {

    fun oneRowUp() =
        Position(row = row - 1, column = column)

    fun oneRowDown() =
        Position(row = row + 1, column = column)

    fun oneColumnLeft() =
        Position(row = row, column = column - 1)

    fun oneColumnRight() =
        Position(row = row, column = column + 1)
}
