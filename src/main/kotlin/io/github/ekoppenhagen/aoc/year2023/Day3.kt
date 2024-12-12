package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Location
import io.github.ekoppenhagen.aoc.extensions.getAllNumbersWithIndex

class Day3 : AbstractAocDay(
    exampleResultPart1 = 4361,
    exampleResultPart2 = 467_835,
) {

    override fun solvePart1(engineSchematic: List<String>) =
        getSumOfPartNumbers(engineSchematic)

    private fun getSumOfPartNumbers(engineSchematic: List<String>) =
        getPartNumbers(engineSchematic).sum()

    private fun getPartNumbers(engineSchematic: List<String>) =
        filterPartNumbers(getAllNumbers(engineSchematic), getAllSymbols(engineSchematic)).map { it.first }

    private fun getAllNumbers(engineSchematic: List<String>): List<Pair<Long, Location>> =
        mutableListOf<Pair<Long, Location>>().apply {
            engineSchematic.forEachIndexed { rowIndex, row ->
                row.getAllNumbersWithIndex().map { Pair(it.first, Location(rowIndex, it.second)) }
            }
        }

    private fun getAllSymbols(engineSchematic: List<String>) =
        mutableListOf<Location>().apply {
            engineSchematic.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, character ->
                    if (isSymbol(character)) add(Location(rowIndex, columnIndex))
                }
            }
        }

    private fun isSymbol(character: Char) =
        !character.isDigit() && character != '.'

    private fun filterPartNumbers(numbers: List<Pair<Long, Location>>, symbols: List<Location>) =
        numbers.filter { isNextToSymbol(it, symbols) }

    private fun isNextToSymbol(numberWithCoordinates: Pair<Long, Location>, symbolCoordinates: List<Location>) =
        symbolCoordinates.any {
            it.row in (numberWithCoordinates.second.row - 1)..(numberWithCoordinates.second.row + 1) &&
                it.column in (numberWithCoordinates.second.column - 1)..(numberWithCoordinates.second.column + "${numberWithCoordinates.first}".length)
        }

    override fun solvePart2(lines: List<String>) =
        lines.mapIndexed { lineIndex, line -> getGearRatioSum(line, lineIndex, lines) }.sum()

    private fun getGearRatioSum(line: String, lineIndex: Int, lines: List<String>) =
        if (!line.contains("*")) 0
        else getGearRatiosInLine(line, getPreviousLine(lineIndex, lines), getNextLine(lineIndex, lines)).sum()

    private fun getGearRatiosInLine(line: String, previousLine: String, nextLine: String) =
        line.mapIndexed { index, character ->
            if (character != '*') 0
            else getGearRation(index, line, previousLine, nextLine)
        }

    private fun getGearRation(index: Int, line: String, previousLine: String, nextLine: String) =
        listOf(
            getPartNumbersOfCurrentLine(line, index),
            getPartNumbersOfAdjacentLine(previousLine, index),
            getPartNumbersOfAdjacentLine(nextLine, index),
        ).flatten().filterNotNull().let { if (it.size != 2) 0 else it[0].toInt() * it[1].toInt() }

    private fun getPartNumbersOfCurrentLine(line: String, index: Int) =
        listOf(getPartNumberToTheLeft(index, line), getPartNumberToTheRight(index, line))

    private fun getPartNumbersOfAdjacentLine(adjacentLine: String, index: Int) =
        if (isPartNumber(adjacentLine[index])) {
            listOf(getPartNumberToTheLeftAndRight(index, adjacentLine))
        } else {
            listOf(getPartNumberToTheLeft(index, adjacentLine), getPartNumberToTheRight(index, adjacentLine))
        }

    private fun getPartNumberToTheLeftAndRight(index: Int, line: String) =
        "${getPartNumberToTheLeft(index, line).orEmpty()}${line[index]}${getPartNumberToTheRight(index, line).orEmpty()}"

    private fun getPartNumberToTheRight(index: Int, line: String) =
        if (index != line.lastIndex && isPartNumber(line[index + 1])) {
            val rightSideSchematic = line.substring(index + 1)
            val indexOfFirstNonPartNumber = rightSideSchematic.indexOfFirst { !isPartNumber(it) }
            rightSideSchematic.take(if (indexOfFirstNonPartNumber == -1) rightSideSchematic.length else indexOfFirstNonPartNumber)
        } else null

    private fun getPartNumberToTheLeft(index: Int, line: String) =
        if (index != 0 && isPartNumber(line[index - 1])) {
            val leftSideSchematic = line.take(index)
            leftSideSchematic.substring(leftSideSchematic.indexOfLast { !isPartNumber(it) } + 1)
        } else null

    private fun getPreviousLine(currentLineIndex: Int, lines: List<String>) =
        if (currentLineIndex == 0) ".".repeat(lines.first().length) else lines[currentLineIndex - 1]

    private fun getNextLine(currentLineIndex: Int, lines: List<String>) =
        if (currentLineIndex == lines.lastIndex) ".".repeat(lines.first().length) else lines[currentLineIndex + 1]

    private fun isPartNumber(character: Char) =
        character.isDigit()
}
