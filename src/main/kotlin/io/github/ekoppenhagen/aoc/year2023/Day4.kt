package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay

class Day4 : AbstractAocDay(
    exampleResultPart1 = 13,
    exampleResultPart2 = 30,
) {

    override fun solvePart1(scratchCards: List<String>) =
        scratchCards.sumOf(::getPointsForScratchCards)

    private fun getPointsForScratchCards(scratchCard: String) =
        calculatePoints(getWinningNumbers(scratchCard), getDrawnNumbers(scratchCard))

    private fun getWinningNumbers(scratchCard: String) =
        getNumberSegmentOfScratchCard(scratchCard, 0)

    private fun getDrawnNumbers(scratchCard: String) =
        getNumberSegmentOfScratchCard(scratchCard, 1)

    private fun getNumberSegmentOfScratchCard(scratchCard: String, segment: Int) =
        scratchCard.substringAfter(": ")
            .split(" | ")[segment]
            .split(" ")
            .filter { it != "" }
            .map(String::toInt)

    private fun calculatePoints(winningNumbers: List<Int>, drawnNumbers: List<Int>): Int {
        var points = 1
        drawnNumbers.forEach { if (winningNumbers.contains(it)) points *= 2 }
        return if (points == 1) 0 else points / 2
    }

    override fun solvePart2(scratchCards: List<String>) =
        getTotalNumberOfScratchCards(scratchCards.reversed(), createCache())

    private fun createCache() = mutableMapOf<Int, Int>()

    private fun getTotalNumberOfScratchCards(scratchCards: List<String>, gameWinningsCache: MutableMap<Int, Int>) =
        scratchCards.mapIndexed { gameIdOffset, scratchCard ->
            getNumberOfWinningCards(scratchCard, scratchCards.size - gameIdOffset, gameWinningsCache)
        }.sum()

    private fun getNumberOfWinningCards(scratchCard: String, gameId: Int, gameWinningsCache: MutableMap<Int, Int>): Int =
        gameWinningsCache[gameId] ?: calculateNumberOfWinnings(getWinningNumbers(scratchCard), getDrawnNumbers(scratchCard)).let {
            if (hasNoWinnings(it)) 1
            else getWinningsOfOtherCards(gameId + 1..gameId + it, gameWinningsCache)
        }.also { gameWinningsCache[gameId] = it }

    private fun calculateNumberOfWinnings(winningNumbers: List<Int>, drawnNumbers: List<Int>): Int =
        drawnNumbers.map { if (winningNumbers.contains(it)) 1 else 0 }.sum()

    private fun hasNoWinnings(numberOfWinnings: Int) =
        numberOfWinnings == 0

    private fun getWinningsOfOtherCards(gameIds: IntRange, gameWinningsCache: MutableMap<Int, Int>) =
        gameIds.sumOf { gameWinningsCache[it]!! } + 1
}
