package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay

// https://adventofcode.com/2023/day/3
class Day3 : AbstractAocDay(year = 2023, day = 3) {

    override fun solvePart1(lines: List<String>) =
        analyzeEngineSchematic(lines)

    private fun analyzeEngineSchematic(lines: List<String>) =
        lines.mapIndexed { lineIndex, line ->
            getSumOfValidPartNumbersInLine(line, lineIndex, lines)
        }.sum()

    private fun getSumOfValidPartNumbersInLine(line: String, lineIndex: Int, lines: List<String>) =
        getValidPartNumbersInLine(line, getPreviousLine(lineIndex, lines), getNextLine(lineIndex, lines)).sum()

    private fun getPreviousLine(currentLineIndex: Int, lines: List<String>) =
        if (currentLineIndex == 0) ".".repeat(lines.first().length) else lines[currentLineIndex - 1]

    private fun getNextLine(currentLineIndex: Int, lines: List<String>) =
        if (currentLineIndex == lines.lastIndex) ".".repeat(lines.first().length) else lines[currentLineIndex + 1]

    private fun getValidPartNumbersInLine(line: String, previousLine: String, nextLine: String) =
        removeInvalidPartNumbers(getPartNumbersWithSurroundings(line, previousLine, nextLine))

    private fun removeInvalidPartNumbers(partNumbersWithSurroundings: List<Pair<Int, String>>) =
        partNumbersWithSurroundings
            .filter { it.first != 0 }
            .filter { isContainingSymbol(it.second) }
            .map(Pair<Int, String>::first)

    private fun isContainingSymbol(surrounding: String) =
        surrounding.any(::isSymbol)

    private fun isSymbol(character: Char) =
        !character.isDigit() && character != '.'

    private fun getPartNumbersWithSurroundings(
        line: String,
        previousLine: String,
        nextLine: String,
    ): List<Pair<Int, String>> {
        var surrounding = ""
        var partNumber = "0"

        val partNumbersWithSurroundings = line.mapIndexed { index, character ->
            if (isPartNumber(character)) {
                surrounding += "${previousLine[index]}${nextLine[index]}"
                partNumber += character
                null
            } else {
                val partNumberWithSurrounding = partNumber.toInt() to surrounding + "${previousLine[index]}${line[index]}${nextLine[index]}"
                surrounding = "${previousLine[index]}${line[index]}${nextLine[index]}"
                partNumber = "0"
                partNumberWithSurrounding
            }
        }.filterNotNull().toMutableList()
        if (partNumber != "0") partNumbersWithSurroundings += partNumber.toInt() to surrounding
        return partNumbersWithSurroundings
    }

    private fun isPartNumber(character: Char) =
        character.isDigit()

    override fun solvePart2(lines: List<String>) =
        lines.mapIndexed { lineIndex, line ->
            getGearRatioSum(line, lineIndex, lines)
        }.sum()

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
        "${getPartNumberToTheLeft(index, line) ?: ""}${line[index]}${getPartNumberToTheRight(index, line) ?: ""}"

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
}
