package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.getAllNumbers

class Day11 : AbstractAocDay(day = 11) {

    override fun solvePart1(stoneEngravings: List<String>) =
        getTotalNumberOfEvolvingStones(getInitialStoneEngravingNumbers(stoneEngravings, blinks = 25), createCache())

    override fun solvePart2(stoneEngravings: List<String>) =
        getTotalNumberOfEvolvingStones(getInitialStoneEngravingNumbers(stoneEngravings, blinks = 75), createCache())

    private fun getInitialStoneEngravingNumbers(stoneEngravings: List<String>, blinks: Int) =
        stoneEngravings.first().getAllNumbers().map { Stone(it, blinks) }

    private fun createCache() = mutableMapOf<Stone, Long>()

    private fun getTotalNumberOfEvolvingStones(initialStones: List<Stone>, stoneEvolveCache: MutableMap<Stone, Long>) =
        initialStones.sumOf { getNumberOfEvolvingStones(it, stoneEvolveCache) }

    private fun getNumberOfEvolvingStones(stone: Stone, stoneEvolveCache: MutableMap<Stone, Long>): Long =
        when {
            stone.blinksLeft == 0 -> 1L
            stoneEvolveCache[stone] != null -> stoneEvolveCache[stone]!!
            else -> applyRules(stone, stoneEvolveCache).also { stoneEvolveCache[stone] = it }
        }

    private fun applyRules(stone: Stone, stoneEvolveCache: MutableMap<Stone, Long>) =
        when {
            stone.number == 0L -> getNumberOfEvolvingStones(Stone(1, stone.blinksLeft - 1), stoneEvolveCache)
            hasEvenDigits(stone.number) -> splitStoneAndGetNumberOfEvolvingStones(stone, stoneEvolveCache)
            else -> getNumberOfEvolvingStones(Stone(stone.number * 2024, stone.blinksLeft - 1), stoneEvolveCache)
        }

    private fun hasEvenDigits(number: Long) =
        number.toString().length % 2 == 0

    private fun splitStoneAndGetNumberOfEvolvingStones(stone: Stone, stoneEvolveCache: MutableMap<Stone, Long>) =
        getTotalNumberOfEvolvingStones(
            listOf(
                Stone(getFirstHalfOfNumber(stone.number), stone.blinksLeft - 1),
                Stone(getSecondHalfOfNumber(stone.number), stone.blinksLeft - 1),
            ),
            stoneEvolveCache,
        )

    private fun getFirstHalfOfNumber(number: Long) =
        number.toString().take(number.toString().length / 2).toLong()

    private fun getSecondHalfOfNumber(number: Long) =
        number.toString().substring(number.toString().length / 2).toLong()

    data class Stone(val number: Long, val blinksLeft: Int)
}
