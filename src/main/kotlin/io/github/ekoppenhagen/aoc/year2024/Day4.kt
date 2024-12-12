package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Grid

class Day4 : AbstractAocDay(day = 4) {

    override fun solvePart1(wordSearch: List<String>) =
        countAllAppearancesOfXmas(wordSearch)

    private fun countAllAppearancesOfXmas(wordSearch: List<String>) =
        findAllAppearancesOfXmas(wordSearch).size

    private fun findAllAppearancesOfXmas(wordSearch: List<String>) =
        findAllWordsWithFourLetters(Grid(wordSearch)).filter { it == "XMAS" || it == "SAMX" }

    private fun findAllWordsWithFourLetters(wordSearch: Grid) =
        wordSearch.mapIndexed { rowIndex, columnIndex, _ ->
            getFourLetterWordsInEachDirection(rowIndex, columnIndex, wordSearch)
        }.flatten().flatten()

    private fun getFourLetterWordsInEachDirection(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Grid
    ) = listOf(
        getWordToTheRight(rowIndex, columnIndex, wordSearch),
        getWordFromDiagonalRight(rowIndex, columnIndex, wordSearch),
        getWordFromBelow(rowIndex, columnIndex, wordSearch),
        getWordFromDiagonalLeft(rowIndex, columnIndex, wordSearch),
    ).filter { it.length == 4 }

    private fun getWordToTheRight(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Grid,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex, columnIndex),
        wordSearch.getOrNull(rowIndex, columnIndex + 1),
        wordSearch.getOrNull(rowIndex, columnIndex + 2),
        wordSearch.getOrNull(rowIndex, columnIndex + 3),
    ).joinToString("")

    private fun getWordFromDiagonalRight(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Grid,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex, columnIndex),
        wordSearch.getOrNull(rowIndex + 1, columnIndex + 1),
        wordSearch.getOrNull(rowIndex + 2, columnIndex + 2),
        wordSearch.getOrNull(rowIndex + 3, columnIndex + 3),
    ).joinToString("")

    private fun getWordFromBelow(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Grid
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex, columnIndex),
        wordSearch.getOrNull(rowIndex + 1, columnIndex),
        wordSearch.getOrNull(rowIndex + 2, columnIndex),
        wordSearch.getOrNull(rowIndex + 3, columnIndex),
    ).joinToString("")

    private fun getWordFromDiagonalLeft(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Grid,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex, columnIndex),
        wordSearch.getOrNull(rowIndex + 1, columnIndex - 1),
        wordSearch.getOrNull(rowIndex + 2, columnIndex - 2),
        wordSearch.getOrNull(rowIndex + 3, columnIndex - 3),
    ).joinToString("")

    override fun solvePart2(wordSearch: List<String>) =
        countAllAppearancesOfXmasCross(wordSearch)

    private fun countAllAppearancesOfXmasCross(wordSearch: List<String>) =
        findAllAppearancesOfXmasCross(wordSearch).size

    private fun findAllAppearancesOfXmasCross(wordSearch: List<String>) =
        findAllCrossWordsWithThreeLetters(Grid(wordSearch)).filter { isXmasCross(it) }

    private fun isXmasCross(cross: Pair<String, String>) =
        (cross.first == "MAS" || cross.first == "SAM") &&
            (cross.second == "MAS" || cross.second == "SAM")

    private fun findAllCrossWordsWithThreeLetters(wordSearch: Grid) =
        wordSearch.mapIndexed { rowIndex, columnIndex, _ ->
            getThreeLetterCrossWords(rowIndex, columnIndex, wordSearch)
        }.flatten().filter { haveBothWordsThreeLetters(it) }

    private fun haveBothWordsThreeLetters(wordPair: Pair<String, String>) =
        wordPair.first.length == 3 && wordPair.second.length == 3

    private fun getThreeLetterCrossWords(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Grid,
    ) = getCrossWordDiagonalRightDown(rowIndex, columnIndex, wordSearch) to getCrossWordDiagonalRightUp(rowIndex, columnIndex, wordSearch)

    private fun getCrossWordDiagonalRightDown(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Grid,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex, columnIndex),
        wordSearch.getOrNull(rowIndex + 1, columnIndex + 1),
        wordSearch.getOrNull(rowIndex + 2, columnIndex + 2),
    ).joinToString("")

    private fun getCrossWordDiagonalRightUp(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Grid,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex + 2, columnIndex),
        wordSearch.getOrNull(rowIndex + 1, columnIndex + 1),
        wordSearch.getOrNull(rowIndex, columnIndex + 2),
    ).joinToString("")
}
