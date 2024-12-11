package io.github.ekoppenhagen.aoc.extensions

import java.util.*

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

fun List<String>.toCharacterGrid() =
    Array(size) { rowIndex ->
        Array(this.firstOrNull()?.length ?: 0) { columnIndex ->
            this[rowIndex][columnIndex]
        }.toCharArray()
    }

fun List<String>.toNumberGrid() =
    Array(size) { rowIndex ->
        Array(this.firstOrNull()?.length ?: 0) { columnIndex ->
            this[rowIndex][columnIndex].digitToInt()
        }.toIntArray()
    }

fun <E> LinkedList<E>.removeLastWhile(predicate: (E) -> Boolean): LinkedList<E> {
    if (!isEmpty()) {
        val iterator = listIterator(size)
        while (iterator.hasPrevious()) {
            if (!predicate(iterator.previous())) {
                return take(iterator.nextIndex() + 1)
            }
        }
    }
    return LinkedList()
}

private fun <T> Iterable<T>.take(n: Int): LinkedList<T> {
    val reducedList = LinkedList<T>()
    var count = 0
    for (item in this) {
        reducedList.add(item)
        if (++count == n) break
    }
    return reducedList
}
