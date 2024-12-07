package io.github.ekoppenhagen.aoc.common

enum class Direction {

    UP,
    RIGHT,
    DOWN,
    LEFT;

    fun rotateClockwise() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }
}