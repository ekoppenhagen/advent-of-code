package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.toGrid

// https://adventofcode.com/2024/day/4
class Day4 : AbstractAocDay(year = 2024, day = 4) {

    override fun solvePart1(wordSearch: List<String>) =
        countAllAppearancesOfXmas(wordSearch)

    private fun countAllAppearancesOfXmas(wordSearch: List<String>) =
        findAllAppearancesOfXmas(wordSearch).size

    private fun findAllAppearancesOfXmas(wordSearch: List<String>) =
        findAllWordsWithFourLetters(wordSearch.toGrid()).filter { it == "XMAS" || it == "SAMX" }

    private fun findAllWordsWithFourLetters(wordSearch: Array<Array<String>>) =
        wordSearch.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, _ ->
                getFourLetterWordsInEachDirection(rowIndex, columnIndex, wordSearch)
            }.flatten()
        }.flatten()

    private fun getFourLetterWordsInEachDirection(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Array<Array<String>>
    ) = listOfNotNull(
        getWordToTheRight(rowIndex, columnIndex, wordSearch),
        getWordFromDiagonalRight(rowIndex, columnIndex, wordSearch),
        getWordFromBelow(rowIndex, columnIndex, wordSearch),
        getWordFromDiagonalLeft(rowIndex, columnIndex, wordSearch),
    ).filter { it.length == 4 }

    private fun getWordToTheRight(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Array<Array<String>>,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex)?.getOrNull(columnIndex),
        wordSearch.getOrNull(rowIndex)?.getOrNull(columnIndex + 1),
        wordSearch.getOrNull(rowIndex)?.getOrNull(columnIndex + 2),
        wordSearch.getOrNull(rowIndex)?.getOrNull(columnIndex + 3),
    ).joinToString("")

    private fun getWordFromDiagonalRight(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Array<Array<String>>,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex)?.getOrNull(columnIndex),
        wordSearch.getOrNull(rowIndex + 1)?.getOrNull(columnIndex + 1),
        wordSearch.getOrNull(rowIndex + 2)?.getOrNull(columnIndex + 2),
        wordSearch.getOrNull(rowIndex + 3)?.getOrNull(columnIndex + 3),
    ).joinToString("")

    private fun getWordFromBelow(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Array<Array<String>>
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex)?.getOrNull(columnIndex),
        wordSearch.getOrNull(rowIndex + 1)?.getOrNull(columnIndex),
        wordSearch.getOrNull(rowIndex + 2)?.getOrNull(columnIndex),
        wordSearch.getOrNull(rowIndex + 3)?.getOrNull(columnIndex),
    ).joinToString("")

    private fun getWordFromDiagonalLeft(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Array<Array<String>>,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex)?.getOrNull(columnIndex),
        wordSearch.getOrNull(rowIndex + 1)?.getOrNull(columnIndex - 1),
        wordSearch.getOrNull(rowIndex + 2)?.getOrNull(columnIndex - 2),
        wordSearch.getOrNull(rowIndex + 3)?.getOrNull(columnIndex - 3),
    ).joinToString("")

    override fun solvePart2(wordSearch: List<String>) =
        countAllAppearancesOfXmasCross(wordSearch)

    private fun countAllAppearancesOfXmasCross(wordSearch: List<String>) =
        findAllAppearancesOfXmasCross(wordSearch).size

    private fun findAllAppearancesOfXmasCross(wordSearch: List<String>) =
        findAllCrossWordsWithThreeLetters(wordSearch.toGrid()).filter { isXmasCross(it) }

    private fun isXmasCross(cross: Pair<String, String>) =
        (cross.first == "MAS" || cross.first == "SAM") &&
            (cross.second == "MAS" || cross.second == "SAM")

    private fun findAllCrossWordsWithThreeLetters(wordSearch: Array<Array<String>>) =
        wordSearch.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, _ ->
                getThreeLetterCrossWords(rowIndex, columnIndex, wordSearch)
            }.filter { haveBothWordsThreeLetters(it) }
        }.flatten()

    private fun haveBothWordsThreeLetters(wordPair: Pair<String, String>) =
        wordPair.first.length == 3 && wordPair.second.length == 3

    private fun getThreeLetterCrossWords(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Array<Array<String>>
    ) = Pair(
        getCrossWordDiagonalRightDown(rowIndex, columnIndex, wordSearch),
        getCrossWordDiagonalRightUp(rowIndex, columnIndex, wordSearch),
    )

    private fun getCrossWordDiagonalRightDown(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Array<Array<String>>,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex)?.getOrNull(columnIndex),
        wordSearch.getOrNull(rowIndex + 1)?.getOrNull(columnIndex + 1),
        wordSearch.getOrNull(rowIndex + 2)?.getOrNull(columnIndex + 2),
    ).joinToString("")

    private fun getCrossWordDiagonalRightUp(
        rowIndex: Int,
        columnIndex: Int,
        wordSearch: Array<Array<String>>,
    ) = listOfNotNull(
        wordSearch.getOrNull(rowIndex + 2)?.getOrNull(columnIndex),
        wordSearch.getOrNull(rowIndex + 1)?.getOrNull(columnIndex + 1),
        wordSearch.getOrNull(rowIndex)?.getOrNull(columnIndex + 2),
    ).joinToString("")
}
