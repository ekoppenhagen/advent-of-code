package io.github.ekoppenhagen.aoc.common

data class Grid(
    private val lines: List<String>
) {

    val rows = lines.size
    val columns = lines.firstOrNull()?.length ?: 0

    fun getOrNull(rowIndex: Int, columnIndex: Int) =
        lines.getOrNull(rowIndex)?.getOrNull(columnIndex)

    fun getOrNull(position: Position) =
        lines.getOrNull(position.row)?.getOrNull(position.column)

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

    fun replace(rowIndex: Int, columnIndex: Int, character: Char) =
        Grid(
            mutableListOf<String>().apply {
                this.addAll(lines.take(rowIndex))
                this.add(lines[rowIndex].substring(0, columnIndex) + character + lines[rowIndex].substring(columnIndex + 1))
                this.addAll(lines.drop(rowIndex + 1))
            }
        )

    fun print(): Grid {
        println()
        lines.forEach { println(it) }
        return this
    }
}
