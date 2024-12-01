package io.github.ekoppenhagen.aoc.extensions

fun Pair<Int?, Int?>.concatenateToNumber(): Int? =
    when {
        first == null && second == null -> null
        first == null && second != null -> second
        first != null && second == null -> first
        else -> "$first$second".toInt()
    }
