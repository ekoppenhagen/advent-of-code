package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.common.Grid
import io.github.ekoppenhagen.aoc.common.Position
import io.github.ekoppenhagen.aoc.extensions.notContains

suspend fun main() {
    Day12().solve()
}

class Day12 : AbstractAocDay(
    exampleResultPart1 = 1930,
    exampleResultPart2 = 1206,
) {

    override suspend fun solvePart1(gardenPlotMap: List<String>) =
        calculateTotalPriceOfFencingWithPerimeter(Grid(gardenPlotMap))

    private fun calculateTotalPriceOfFencingWithPerimeter(gardenPlotMap: Grid) =
        findAllRegions(gardenPlotMap).sumOf(::calculateFencePriceOfRegionWithPerimeter)

    private fun findAllRegions(gardenPlotMap: Grid): List<Region> {
        val knownPlotPositions = mutableSetOf<Position>()
        val regions = mutableListOf<Region>()

        gardenPlotMap.forEachIndexed { rowIndex, columnIndex, plant ->
            if (knownPlotPositions.contains(Position(rowIndex, columnIndex))) return@forEachIndexed

            val plotPositions = gardenPlotMap.floodFill(rowIndex, columnIndex)
            knownPlotPositions.addAll(plotPositions)
            regions.add(toRegion(plant, plotPositions))
        }
        return regions
    }

    private fun toRegion(plant: Char, plotPositions: MutableList<Position>) =
        Region(
            plant = plant,
            plots = plotPositions.size,
            plotSides = getNumberOfPlotSides(plotPositions),
            regionSides = getNumberOfRegionSides(plotPositions),
        )

    private fun getNumberOfPlotSides(plotPositions: MutableList<Position>) =
        plotPositions.sumOf {
            var plotSides = 0
            if (plotPositions.notContains(it.rowUp())) plotSides++
            if (plotPositions.notContains(it.rowDown())) plotSides++
            if (plotPositions.notContains(it.columnRight())) plotSides++
            if (plotPositions.notContains(it.columnLeft())) plotSides++
            plotSides
        }

    private fun getNumberOfRegionSides(plotPositions: MutableList<Position>) =
        plotPositions.sumOf {
            numberOfOuterCorners(it, plotPositions) + numberOfInnerCorners(it, plotPositions)
        }

    private fun numberOfOuterCorners(
        position: Position,
        plotPositions: MutableList<Position>,
    ): Int {
        var outerCorners = 0
        if (plotPositions.notContains(position.rowUp()) &&
            plotPositions.notContains(position.columnLeft())
        ) outerCorners++
        if (plotPositions.notContains(position.rowUp()) &&
            plotPositions.notContains(position.columnRight())
        ) outerCorners++
        if (plotPositions.notContains(position.rowDown()) &&
            plotPositions.notContains(position.columnLeft())
        ) outerCorners++
        if (plotPositions.notContains(position.rowDown()) &&
            plotPositions.notContains(position.columnRight())
        ) outerCorners++
        return outerCorners
    }

    @Suppress("CyclomaticComplexMethod")
    private fun numberOfInnerCorners(
        position: Position,
        plotPositions: MutableList<Position>,
    ): Int {
        var innerCorners = 0
        if (plotPositions.contains(position.rowUp()) &&
            plotPositions.contains(position.columnLeft()) &&
            plotPositions.notContains(position.diagonalUpLeft())
        ) innerCorners++
        if (plotPositions.contains(position.rowUp()) &&
            plotPositions.contains(position.columnRight()) &&
            plotPositions.notContains(position.diagonalUpRight())
        ) innerCorners++
        if (plotPositions.contains(position.rowDown()) &&
            plotPositions.contains(position.columnLeft()) &&
            plotPositions.notContains(position.diagonalDownLeft())
        ) innerCorners++
        if (plotPositions.contains(position.rowDown()) &&
            plotPositions.contains(position.columnRight()) &&
            plotPositions.notContains(position.diagonalDownRight())
        ) innerCorners++
        return innerCorners
    }

    private fun calculateFencePriceOfRegionWithPerimeter(region: Region) =
        region.plots * region.plotSides

    private data class Region(
        val plant: Char,
        val plots: Int,
        val plotSides: Int,
        val regionSides: Int,
    )

    override suspend fun solvePart2(gardenPlotMap: List<String>) =
        calculateTotalPriceOfFencingWithRegionSides(Grid(gardenPlotMap))

    private fun calculateTotalPriceOfFencingWithRegionSides(gardenPlotMap: Grid) =
        findAllRegions(gardenPlotMap).sumOf(::calculateFencePriceOfRegionWithRegionSides)

    private fun calculateFencePriceOfRegionWithRegionSides(region: Region) =
        region.plots * region.regionSides
}
