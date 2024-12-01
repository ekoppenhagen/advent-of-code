package io.github.ekoppenhagen.aoc.extensions

fun <E> List<E>.toPairOfFirstAndLastElement() =
    if (this.isEmpty()) null to null else this[0] to this[this.lastIndex]
