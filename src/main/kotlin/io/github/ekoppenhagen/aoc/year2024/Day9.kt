package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay

suspend fun main() {
    Day9().solve()
}

class Day9 : AbstractAocDay(
    exampleResultPart1 = 1928,
    exampleResultPart2 = 2858,
) {

    override suspend fun solvePart1(rawDiscMap: List<String>) =
        calculateFilesystemChecksum(getDefragmentedBlockMapWithoutFreeSpaces(rawDiscMap))

    private fun getDefragmentedBlockMapWithoutFreeSpaces(rawDiscMap: List<String>) =
        removeFragmentationWithoutLeavingFreeSpace(DiscMap(rawDiscMap.first()))

    @Suppress("CognitiveComplexMethod")
    private fun removeFragmentationWithoutLeavingFreeSpace(discMap: DiscMap) =
        discMap.blockMap.apply {
            var backBlockIndex = this.size - 1
            for (frontBlockIndex in 0 until this.size) {
                if (!isFreeSpace(this, frontBlockIndex)) continue
                while (isFreeSpace(this, backBlockIndex) && !areIndizesPassingEachOther(frontBlockIndex, backBlockIndex)) backBlockIndex--
                if (areIndizesPassingEachOther(frontBlockIndex, backBlockIndex)) return this
                this[frontBlockIndex] = this[backBlockIndex]
                this[backBlockIndex] = null
            }
        }

    private fun isFreeSpace(blockMap: List<Long?>, index: Int) = blockMap[index] == null

    private fun areIndizesPassingEachOther(head: Int, tail: Int) = head >= tail

    override suspend fun solvePart2(rawDiscMap: List<String>) =
        calculateFilesystemChecksum(getDefragmentedBlockMap(rawDiscMap))

    private fun getDefragmentedBlockMap(rawDiscMap: List<String>) =
        removeFragmentationWithoutSplittingFiles(DiscMap(rawDiscMap.first()))

    private fun removeFragmentationWithoutSplittingFiles(discMap: DiscMap) =
        discMap.blockMap.apply {
            discMap.files.reversed().forEach { potentialMoveableFile ->
                moveFileIfPossible(potentialMoveableFile, discMap)
            }
        }

    private fun MutableList<Long?>.moveFileIfPossible(potentialMoveableFile: File, discMap: DiscMap) {
        findFirstMatchingFreeSpace(potentialMoveableFile, discMap.freeSpaces)
            ?.also { freeSpace ->
                repeat(potentialMoveableFile.size) { fileIndex ->
                    this[freeSpace.startIndex + fileIndex] = potentialMoveableFile.id
                    this[potentialMoveableFile.startIndex + fileIndex] = null
                }
                freeSpace.size -= potentialMoveableFile.size
                freeSpace.startIndex += potentialMoveableFile.size
            }
    }

    private fun findFirstMatchingFreeSpace(potentialMoveableFile: File, freeSpaces: List<FreeSpace>) =
        freeSpaces.firstOrNull { freeSpace ->
            freeSpace.size >= potentialMoveableFile.size &&
                freeSpace.startIndex < potentialMoveableFile.startIndex
        }

    private fun calculateFilesystemChecksum(defragmentedBlocks: List<Long?>) =
        defragmentedBlocks.mapIndexed { index, fileId -> index * (fileId ?: 0) }.sum()

    private class DiscMap(rawDiscMap: String) {

        var blockMap: MutableList<Long?> = mutableListOf()
        var files: MutableList<File> = mutableListOf()
        var freeSpaces: MutableList<FreeSpace> = mutableListOf()

        init {
            var isFile = true
            var fileId = 0L
            var index = 0
            rawDiscMap.forEach { char ->
                if (isFile) {
                    files.add(File(index, char.digitToInt(), fileId))
                    repeat(char.digitToInt()) { blockMap.add(fileId) }
                    fileId++
                } else {
                    freeSpaces.add(FreeSpace(index, char.digitToInt()))
                    repeat(char.digitToInt()) { blockMap.add(null) }
                }
                isFile = !isFile
                index += char.digitToInt()
            }
        }
    }

    private data class File(val startIndex: Int, val size: Int, val id: Long)

    private data class FreeSpace(var startIndex: Int, var size: Int)
}
