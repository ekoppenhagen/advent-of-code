package io.github.ekoppenhagen.aoc.common

class Grid(
    private val lines: List<String>
) {

    val rows = lines.size
    val columns = lines.firstOrNull()?.length ?: 0

    fun getOrNull(position: Position) =
        getOrNull(rowIndex = position.row, columnIndex = position.column)

    fun getOrNull(rowIndex: Int, columnIndex: Int) =
        lines.getOrNull(rowIndex)?.getOrNull(columnIndex)

    fun <R> mapIndexed(transform: (rowIndex: Int, columnIndex: Int, Char) -> R): List<List<R>> {
        var rowIndex = 0
        var columnIndex = 0
        return lines.map { line ->
            line.map { character ->
                transform(rowIndex, columnIndex++, character)
            }.also {
                rowIndex++
                columnIndex = 0
            }
        }
    }

    fun firstPositionOf(character: Char): Position? {
        lines.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, value ->
                if (value == character) return Position(rowIndex, columnIndex)
            }
        }
        return null
    }

    fun forEachIndexed(action: (rowIndex: Int, columnIndex: Int, Char) -> Unit) {
        var rowIndex = 0
        var columnIndex = 0
        return lines.forEach { line ->
            line.forEach { character ->
                action(rowIndex, columnIndex++, character)
            }.also {
                rowIndex++
                columnIndex = 0
            }
        }
    }

    fun copyAndReplace(position: Position, character: Char) =
        copyAndReplace(rowIndex = position.row, columnIndex = position.column, character = character)

    fun copyAndReplace(rowIndex: Int, columnIndex: Int, character: Char) =
        Grid(
            mutableListOf<String>().apply {
                this.addAll(lines.take(rowIndex))
                this.add(lines[rowIndex].substring(0, columnIndex) + character + lines[rowIndex].substring(columnIndex + 1))
                this.addAll(lines.drop(rowIndex + 1))
            }
        )

    fun floodFill(position: Position) =
        mutableListOf<Position>().apply {
            if (isInside(position = position)) {
                floodFillRecursively(
                    character = getOrNull(position = position)!!,
                    currentPosition = position,
                    positions = this,
                )
                this.sortWith(compareBy({ it.row }, { it.column }))
            }
        }

    fun floodFill(rowIndex: Int, columnIndex: Int) =
        floodFill(position = Position(row = rowIndex, column = columnIndex))

    private fun floodFillRecursively(
        character: Char,
        currentPosition: Position,
        positions: MutableList<Position>,
    ) {
        if (
            positions.contains(currentPosition) ||
            !isInside(position = currentPosition) ||
            character != getOrNull(position = currentPosition)
        ) return
        positions.add(currentPosition)

        floodFillRecursively(character, currentPosition.oneRowUp(), positions)
        floodFillRecursively(character, currentPosition.oneRowDown(), positions)
        floodFillRecursively(character, currentPosition.oneColumnLeft(), positions)
        floodFillRecursively(character, currentPosition.oneColumnRight(), positions)
    }

    fun isInside(position: Position) =
        isInside(rowIndex = position.row, columnIndex = position.column)

    fun isInside(rowIndex: Int, columnIndex: Int) =
        getOrNull(rowIndex = rowIndex, columnIndex = columnIndex) != null
}
