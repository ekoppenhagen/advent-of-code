package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Location
import io.github.ekoppenhagen.aoc.extensions.toGrid

// https://adventofcode.com/2024/day/8
class Day8 : AbstractAocDay(year = 2024, day = 8) {

    override fun solvePart1(cityAntennaMap: List<String>) =
        findAllUniqueAntinodeLocationsInsideMap(cityAntennaMap).size

    private fun findAllUniqueAntinodeLocationsInsideMap(cityAntennaMap: List<String>) =
        findAllUniqueAntinodeLocations(cityAntennaMap).filter { isInsideMap(it, cityAntennaMap) }

    private fun findAllUniqueAntinodeLocations(cityAntennaMap: List<String>) =
        findAllAntennasForFrequencies(cityAntennaMap.toGrid()).map(::calculateAllAntinodeLocations).flatten().toSet()

    private fun findAllAntennasForFrequencies(cityAntennaMap: Array<Array<String>>) =
        mutableMapOf<String, MutableList<Location>>().apply {
            cityAntennaMap.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, frequency ->
                    if (frequency != ".") addAntennaOfFrequency(this, frequency, rowIndex, columnIndex)
                }
            }
        }

    private fun addAntennaOfFrequency(
        antennaDictionary: MutableMap<String, MutableList<Location>>,
        frequency: String,
        rowIndex: Int,
        columnIndex: Int
    ) = antennaDictionary.getOrPut(frequency) { mutableListOf() }.add(Location(rowIndex, columnIndex))

    private fun calculateAllAntinodeLocations(frequencyAntennas: Map.Entry<String, List<Location>>) =
        getAllAntennaCombinations(frequencyAntennas.value).map(::calculateAntinodesOfAntennas).flatten()

    private fun getAllAntennaCombinations(antennaLocations: List<Location>) =
        mutableListOf<Pair<Location, Location>>().apply {
            antennaLocations.forEachIndexed { antennaIndex, antennaLocation ->
                antennaLocations.drop(antennaIndex + 1).forEach { otherAntennaLocation ->
                    add(Pair(antennaLocation, otherAntennaLocation))
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
        findAllAntennasForFrequencies(cityAntennaMap.toGrid()).map { calculateAllAntinodeLocationsWithResonantHarmonics(it, cityAntennaMap) }.flatten().toSet()

    private fun calculateAllAntinodeLocationsWithResonantHarmonics(frequencyAntennas: Map.Entry<String, List<Location>>, cityAntennaMap: List<String>) =
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
