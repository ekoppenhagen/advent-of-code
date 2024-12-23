package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Grid
import io.github.ekoppenhagen.aoc.common.Position

suspend fun main() {
    Day8().solve()
}

class Day8 : AbstractAocDay(
    exampleResultPart1 = 14,
    exampleResultPart2 = 34,
) {

    override suspend fun solvePart1(cityAntennaMap: List<String>) =
        findAllUniqueAntinodeLocationsInsideMap(Grid(cityAntennaMap)).size

    private fun findAllUniqueAntinodeLocationsInsideMap(cityAntennaMap: Grid) =
        findAllUniqueAntinodeLocations(cityAntennaMap).filter { cityAntennaMap.isInside(it) }

    private fun findAllUniqueAntinodeLocations(cityAntennaMap: Grid) =
        findAllAntennasForFrequencies(cityAntennaMap).map(::calculateAllAntinodeLocations).flatten().toSet()

    private fun findAllAntennasForFrequencies(cityAntennaMap: Grid) =
        mutableMapOf<Char, MutableList<Position>>().apply {
            cityAntennaMap.forEachIndexed { rowIndex, columnIndex, frequency ->
                if (frequency != '.') addAntennaOfFrequency(this, frequency, rowIndex, columnIndex)
            }
        }

    private fun addAntennaOfFrequency(
        antennaDictionary: MutableMap<Char, MutableList<Position>>,
        frequency: Char,
        rowIndex: Int,
        columnIndex: Int
    ) = antennaDictionary.getOrPut(frequency) { mutableListOf() }.add(Position(rowIndex, columnIndex))

    private fun calculateAllAntinodeLocations(frequencyAntennas: Map.Entry<Char, List<Position>>) =
        getAllAntennaCombinations(frequencyAntennas.value).map(::calculateAntinodesOfAntennas).flatten()

    private fun getAllAntennaCombinations(antennaPositions: List<Position>) =
        mutableListOf<Pair<Position, Position>>().apply {
            antennaPositions.forEachIndexed { antennaIndex, antennaLocation ->
                antennaPositions.drop(antennaIndex + 1).forEach { otherAntennaLocation ->
                    add(antennaLocation to otherAntennaLocation)
                }
            }
        }

    private fun calculateAntinodesOfAntennas(antennaPairs: Pair<Position, Position>) =
        mutableListOf<Position>().apply {
            val rowDifference = antennaPairs.first.row - antennaPairs.second.row
            val columnDifference = antennaPairs.first.column - antennaPairs.second.column
            add(Position(antennaPairs.first.row + rowDifference, antennaPairs.first.column + columnDifference))
            add(Position(antennaPairs.first.row - rowDifference, antennaPairs.first.column - columnDifference))
            add(Position(antennaPairs.second.row + rowDifference, antennaPairs.second.column + columnDifference))
            add(Position(antennaPairs.second.row - rowDifference, antennaPairs.second.column - columnDifference))
            remove(antennaPairs.first)
            remove(antennaPairs.second)
        }

    override suspend fun solvePart2(cityAntennaMap: List<String>) =
        findAllUniqueAntinodeLocationsWithResonantHarmonics(Grid(cityAntennaMap)).size

    private fun findAllUniqueAntinodeLocationsWithResonantHarmonics(cityAntennaMap: Grid) =
        findAllAntennasForFrequencies(cityAntennaMap)
            .map { calculateAllAntinodeLocationsWithResonantHarmonics(it, cityAntennaMap) }
            .flatten()
            .toSet()

    private fun calculateAllAntinodeLocationsWithResonantHarmonics(frequencyAntennas: Map.Entry<Char, List<Position>>, cityAntennaMap: Grid) =
        getAllAntennaCombinations(frequencyAntennas.value).map { calculateAntinodesOfAntennasWithResonantHarmonics(it, cityAntennaMap) }.flatten()

    private fun calculateAntinodesOfAntennasWithResonantHarmonics(antennaPairs: Pair<Position, Position>, cityAntennaMap: Grid) =
        mutableListOf<Position>().apply {
            val rowDifference = antennaPairs.first.row - antennaPairs.second.row
            val columnDifference = antennaPairs.first.column - antennaPairs.second.column
            addAll(findAllAntinodesWithResonantHarmonics(antennaPairs.first, rowDifference, columnDifference, cityAntennaMap))
        }

    private fun findAllAntinodesWithResonantHarmonics(
        origin: Position,
        rowDifference: Int,
        columnDifference: Int,
        cityAntennaMap: Grid,
    ) = mutableListOf<Position>().apply {
        var currentPosition = origin
        while (cityAntennaMap.isInside(currentPosition)) {
            add(currentPosition)
            currentPosition = Position(currentPosition.row + rowDifference, currentPosition.column + columnDifference)
        }
        currentPosition = origin
        while (cityAntennaMap.isInside(currentPosition)) {
            add(currentPosition)
            currentPosition = Position(currentPosition.row - rowDifference, currentPosition.column - columnDifference)
        }
    }
}
