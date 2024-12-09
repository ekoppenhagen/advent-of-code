package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.removeLastWhile
import io.github.ekoppenhagen.aoc.extensions.toNumber
import java.util.*

// https://adventofcode.com/2024/day/9
class Day9 : AbstractAocDay(year = 2024, day = 9) {

    override fun solvePart1(discMap: List<String>) =
        calculateFilesystemChecksumOfRearrangedFiles(discMap.first())

    private fun calculateFilesystemChecksumOfRearrangedFiles(discMap: String) =
        calculateFilesystemChecksum(makeContiguousFreeSpace(getBlockRepresentation(discMap)))

    private fun getBlockRepresentation(string: String) =
        LinkedList<String>().apply {
            var isFile = true
            var fileId = 0
            string.forEach { blockCounter ->
                if (isFile) addFileBlocks(blockCounter, fileId, this).also { fileId++ }
                else addFreeSpaceBlocks(blockCounter, this)
                isFile = !isFile
            }
        }

    private fun addFileBlocks(blockCounter: Char, fileId: Int, targetList: LinkedList<String>) =
        repeat(blockCounter.toNumber()) { targetList.add("$fileId") }

    private fun addFreeSpaceBlocks(blockCounter: Char, targetList: LinkedList<String>) =
        repeat(blockCounter.toNumber()) { targetList.add(".") }

    private fun makeContiguousFreeSpace(fileBlockRepresentation: LinkedList<String>): LinkedList<String> =
        rearrangeFiles(ignoreEmptySpaceAtTheEnd(fileBlockRepresentation))

    private fun rearrangeFiles(fileBlockRepresentation: LinkedList<String>) =
        fileBlockRepresentation.also { strings ->
            while (fileBlockRepresentation.contains(".")) {
                fileBlockRepresentation[fileBlockRepresentation.indexOfFirst { it == "." }] = fileBlockRepresentation.last()
                fileBlockRepresentation.removeAt(fileBlockRepresentation.size - 1)
            }
        }

    private fun ignoreEmptySpaceAtTheEnd(fileBlockRepresentation: LinkedList<String>) =
        fileBlockRepresentation.removeLastWhile { it == "." }

    private fun calculateFilesystemChecksum(rearrangedFiles: LinkedList<String>) =
        rearrangedFiles.mapIndexed { index, fileId -> index * fileId.toLong() }.sum()

    override fun solvePart2(discMap: List<String>) =
        "not implemented"
}
