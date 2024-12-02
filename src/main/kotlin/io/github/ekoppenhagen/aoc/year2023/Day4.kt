package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay

// https://adventofcode.com/2023/day/4
class Day4 : AbstractAocDay(year = 2023, day = 4) {

    override fun solvePart1(lines: List<String>) =
        lines.sumOf(::getPointsForScratchCards)

    private fun getPointsForScratchCards(line: String) =
        calculatePoints(getWinningNumbers(line), getDrawnNumbers(line))

    private fun getWinningNumbers(line: String) =
        getNumberSegmentOfScratchCard(line, 0)

    private fun getDrawnNumbers(line: String) =
        getNumberSegmentOfScratchCard(line, 1)

    private fun getNumberSegmentOfScratchCard(line: String, segment: Int) =
        line.substringAfter(": ")
            .split(" | ")[segment]
            .split(" ")
            .filter { it != "" }
            .map(String::toInt)

    private fun calculatePoints(winningNumbers: List<Int>, drawnNumbers: List<Int>): Int {
        var points = 1
        drawnNumbers.forEach { if (winningNumbers.contains(it)) points *= 2 }
        return if (points == 1) 0 else points / 2
    }

    override fun solvePart2(lines: List<String>) =
        getAllScratchCards(lines).size

    private fun getAllScratchCards(lines: List<String>): List<String> {
        val completeScratchCardList = lines.toMutableList()
        var currentScratchCards = lines
        var newScratchCards: List<String>

        while (getAdditionalWonCards(currentScratchCards, lines).also { newScratchCards = it }.isNotEmpty()) {
            currentScratchCards = newScratchCards
            completeScratchCardList.addAll(newScratchCards)
        }

        return completeScratchCardList
    }

    private fun getAdditionalWonCards(currentCards: List<String>, referenceCards: List<String>): List<String> {
        val additionalCards = mutableListOf<String>()
        currentCards.forEach {
            var currentCardNumberIndex = getCardNumber(it) - 1
            repeat(calculateWinnings(getWinningNumbers(it), getDrawnNumbers(it))) {
                currentCardNumberIndex++
                additionalCards.add(referenceCards[currentCardNumberIndex])
            }
        }
        return additionalCards
    }

    private fun getCardNumber(line: String) =
        line.substringAfter("Card").substringBefore(":").trim().toInt()

    private fun calculateWinnings(winningNumbers: List<Int>, drawnNumbers: List<Int>) =
        drawnNumbers.map { if (winningNumbers.contains(it)) 1 else 0 }.sum()
}
