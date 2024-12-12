package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay

class Day2 : AbstractAocDay(
    exampleResultPart1 = 8,
    exampleResultPart2 = 2286,
) {

    override fun solvePart1(rawGames: List<String>) =
        getSumOfIdsOfPossibleGames(rawGames)

    private fun getSumOfIdsOfPossibleGames(rawGames: List<String>) =
        rawGames.mapIndexed { gameIdMinusOne, rawGame -> gameIdMinusOne + 1 to createMaxCubeMap(rawGame) }
            .filter { isBelowCubeLimit(it.second) }
            .sumOf { it.first }

    private fun createMaxCubeMap(rawGame: String) =
        mutableMapOf<String, Int>().apply {
            createShowcaseCubeMaps(rawGame).forEach {
                setMaximumCubeValue("red", it, this)
                setMaximumCubeValue("green", it, this)
                setMaximumCubeValue("blue", it, this)
            }
        }

    private fun setMaximumCubeValue(
        cubeColor: String,
        showCaseCubes: Map<String, Int>,
        maximumGameCubes: MutableMap<String, Int>,
    ) {
        if (maximumGameCubes.getOrDefault(cubeColor, 0) < showCaseCubes.getOrDefault(cubeColor, 0))
            maximumGameCubes.put(cubeColor, showCaseCubes.getOrDefault(cubeColor, 0))
    }

    private fun createShowcaseCubeMaps(rawGame: String) =
        getAllShowcases(rawGame).map { createMapOfCubesInShowcase(toShowcase(it)) }

    private fun isBelowCubeLimit(maxCubeMap: Map<String, Int>) =
        maxCubeMap.getOrDefault("red", 0) <= NUMBER_OF_RED_CUBES &&
            maxCubeMap.getOrDefault("green", 0) <= NUMBER_OF_GREEN_CUBES &&
            maxCubeMap.getOrDefault("blue", 0) <= NUMBER_OF_BLUE_CUBES

    private fun getAllShowcases(rawGame: String) =
        rawGame.substringAfter(": ").split("; ")

    private fun toShowcase(rawShowcase: String) =
        rawShowcase.split(", ")

    private fun createMapOfCubesInShowcase(showcase: List<String>) =
        showcase.associate { it.split(" ")[1] to it.split(" ")[0].toInt() }

    override fun solvePart2(rawGames: List<String>) =
        getSumOfPowerOfGames(rawGames)

    private fun getSumOfPowerOfGames(rawGames: List<String>) =
        rawGames.map { createMaxCubeMap(it) }
            .sumOf { it.values.reduce(Int::times) }

    private companion object {

        private const val NUMBER_OF_RED_CUBES = 12
        private const val NUMBER_OF_GREEN_CUBES = 13
        private const val NUMBER_OF_BLUE_CUBES = 14
    }
}
