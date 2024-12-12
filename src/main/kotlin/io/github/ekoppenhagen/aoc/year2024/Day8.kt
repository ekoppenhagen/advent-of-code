package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Grid
import io.github.ekoppenhagen.aoc.common.Location

class Day8 : AbstractAocDay(day = 8) {

    override fun solvePart1(cityAntennaMap: List<String>) =
        findAllUniqueAntinodeLocationsInsideMap(cityAntennaMap).size

    private fun findAllUniqueAntinodeLocationsInsideMap(cityAntennaMap: List<String>) =
        findAllUniqueAntinodeLocations(cityAntennaMap).filter { isInsideMap(it, cityAntennaMap) }

    private fun findAllUniqueAntinodeLocations(cityAntennaMap: List<String>) =
        findAllAntennasForFrequencies(Grid(cityAntennaMap)).map(::calculateAllAntinodeLocations).flatten().toSet()

    private fun findAllAntennasForFrequencies(cityAntennaMap: Grid) =
        mutableMapOf<Char, MutableList<Location>>().apply {
            cityAntennaMap.forEachIndexed { rowIndex, columnIndex, frequency ->
                if (frequency != '.') addAntennaOfFrequency(this, frequency, rowIndex, columnIndex)
            }
        }

    private fun addAntennaOfFrequency(
        antennaDictionary: MutableMap<Char, MutableList<Location>>,
        frequency: Char,
        rowIndex: Int,
        columnIndex: Int
    ) = antennaDictionary.getOrPut(frequency) { mutableListOf() }.add(Location(rowIndex, columnIndex))

    private fun calculateAllAntinodeLocations(frequencyAntennas: Map.Entry<Char, List<Location>>) =
        getAllAntennaCombinations(frequencyAntennas.value).map(::calculateAntinodesOfAntennas).flatten()

    private fun getAllAntennaCombinations(antennaLocations: List<Location>) =
        mutableListOf<Pair<Location, Location>>().apply {
            antennaLocations.forEachIndexed { antennaIndex, antennaLocation ->
                antennaLocations.drop(antennaIndex + 1).forEach { otherAntennaLocation ->
                    add(antennaLocation to otherAntennaLocation)
                }
            }
        }

    private fun calculateAntinodesOfAntennas(antennaPairs: Pair<Location, Location>) =
        mutableListOf<Location>().apply {
            val rowDifference = antennaPairs.first.row - antennaPairs.second.row
            val columnDifference = antennaPairs.first.column - antennaPairs.second.column
            add(Location(antennaPairs.first.row + rowDifference, antennaPairs.first.column + columnDifference))
            add(Location(antennaPairs.first.row - rowDifference, antennaPairs.first.column - columnDifference))
            add(Location(antennaPairs.second.row + rowDifference, antennaPairs.second.column + columnDifference))
            add(Location(antennaPairs.second.row - rowDifference, antennaPairs.second.column - columnDifference))
            remove(antennaPairs.first)
            remove(antennaPairs.second)
        }

    fun isInsideMap(location: Location, cityAntennaMap: List<String>) =
        0 <= location.row && location.row < cityAntennaMap.size &&
            0 <= location.column && location.column < cityAntennaMap.first().length

    override fun solvePart2(cityAntennaMap: List<String>) =
        findAllUniqueAntinodeLocationsWithResonantHarmonics(cityAntennaMap).size

    private fun findAllUniqueAntinodeLocationsWithResonantHarmonics(cityAntennaMap: List<String>) =
        findAllAntennasForFrequencies(Grid(cityAntennaMap))
            .map { calculateAllAntinodeLocationsWithResonantHarmonics(it, cityAntennaMap) }
            .flatten()
            .toSet()

    private fun calculateAllAntinodeLocationsWithResonantHarmonics(frequencyAntennas: Map.Entry<Char, List<Location>>, cityAntennaMap: List<String>) =
        getAllAntennaCombinations(frequencyAntennas.value).map { calculateAntinodesOfAntennasWithResonantHarmonics(it, cityAntennaMap) }.flatten()

    private fun calculateAntinodesOfAntennasWithResonantHarmonics(antennaPairs: Pair<Location, Location>, cityAntennaMap: List<String>) =
        mutableListOf<Location>().apply {
            val rowDifference = antennaPairs.first.row - antennaPairs.second.row
            val columnDifference = antennaPairs.first.column - antennaPairs.second.column
            addAll(findAllAntinodesWithResonantHarmonics(antennaPairs.first, rowDifference, columnDifference, cityAntennaMap))
        }

    private fun findAllAntinodesWithResonantHarmonics(
        origin: Location,
        rowDifference: Int,
        columnDifference: Int,
        cityAntennaMap: List<String>
    ) = mutableListOf<Location>().apply {
        var currentPosition = origin
        while (isInsideMap(currentPosition, cityAntennaMap)) {
            add(currentPosition)
            currentPosition = Location(currentPosition.row + rowDifference, currentPosition.column + columnDifference)
        }
        currentPosition = origin
        while (isInsideMap(currentPosition, cityAntennaMap)) {
            add(currentPosition)
            currentPosition = Location(currentPosition.row - rowDifference, currentPosition.column - columnDifference)
        }
    }
}
