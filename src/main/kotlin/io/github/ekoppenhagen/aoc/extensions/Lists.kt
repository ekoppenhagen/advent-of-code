package io.github.ekoppenhagen.aoc.extensions

fun <E> List<E>.toPairOfFirstAndLastElement() =
    if (this.isEmpty()) null to null else this[0] to this[this.lastIndex]

fun <E> List<List<E>>.rotateClockwise(): MutableList<MutableList<E>> {
    val reversedList = this.reversed()
    return MutableList(this.firstOrNull()?.size ?: 0) { rowIndex ->
        MutableList(size) { columnIndex ->
            reversedList[columnIndex][rowIndex]
        }
    }
}

fun List<String>.toGrid() =
    Array(size) { rowIndex ->
        Array(this.firstOrNull()?.length ?: 0) { columnIndex ->
            "${this[rowIndex][columnIndex]}"
        }
    }
