package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay

// https://adventofcode.com/2023/day/2
class Day2 : AbstractAocDay(year = 2023, day = 2) {

    private val redCubes = 12
    private val greenCubes = 13
    private val blueCubes = 14

    override fun solve1(lines: List<String>) =
        getSumOfIdsOfPossibleGames(lines)

    private fun getSumOfIdsOfPossibleGames(lines: List<String>) =
        lines.sumOf { getIdOfPossibleGame(mapToGame(it)) }

    private fun mapToGame(line: String) =
        Game(
            id = line.substringBefore(":").substringAfter("Game ").toInt(),
            cubeShowcases = getCubeShowcases(line.substringAfter(":")),
        )

    private fun getCubeShowcases(showcases: String) =
        showcases.split(";").map { getCubeShowcase(it.split(",")) }

    private fun getCubeShowcase(cubes: List<String>): CubeShowcase {
        var redCubes = 0
        var blueCubes = 0
        var greenCubes = 0
        cubes.forEach {
            when {
                it.contains("red") -> redCubes = getCubeAmount(it)
                it.contains("blue") -> blueCubes = getCubeAmount(it)
                it.contains("green") -> greenCubes = getCubeAmount(it)
            }
        }
        return CubeShowcase(redCubes, blueCubes, greenCubes)
    }

    private fun getCubeAmount(colorAmount: String) =
        colorAmount.trim().split(" ").first().toInt()

    private fun getIdOfPossibleGame(game: Game) =
        if (isPossible(game)) game.id else 0

    private fun isPossible(game: Game) =
        game.cubeShowcases.all {
            it.red in 0..redCubes &&
                it.green in 0..greenCubes &&
                it.blue in 0..blueCubes
        }

    override fun solve2(lines: List<String>) =
        lines.sumOf { calculatePowerOfGame(mapToGame(it)) }

    private fun calculatePowerOfGame(game: Game) =
        game.cubeShowcases.maxBy(CubeShowcase::red).red *
            game.cubeShowcases.maxBy(CubeShowcase::blue).blue *
            game.cubeShowcases.maxBy(CubeShowcase::green).green

    private data class Game(
        val id: Int,
        val cubeShowcases: List<CubeShowcase>
    )

    private data class CubeShowcase(
        val red: Int,
        val blue: Int,
        val green: Int,
    )
}
