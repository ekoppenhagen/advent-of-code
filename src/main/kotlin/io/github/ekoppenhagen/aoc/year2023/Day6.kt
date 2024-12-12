package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay

class Day6 : AbstractAocDay(
    exampleResultPart1 = 288,
    exampleResultPart2 = 71_503,
) {

    override fun solvePart1(lines: List<String>) =
        mapToRace(lines)
            .map(::calculateNumberOfWaysToBeatCurrentRecord)
            .reduce { product, factor -> product * factor }

    private fun mapToRace(lines: List<String>): List<Race> {
        val times = parseLines(lines[0])
        val distances = parseLines(lines[1])
        return times.mapIndexed { index, time -> Race(time, distances[index]) }
    }

    private fun parseLines(line: String) =
        line.substringAfter(":").trim().split(" ").filter(String::isNotBlank).map(String::toLong)

    private fun calculateNumberOfWaysToBeatCurrentRecord(race: Race): Long {
        val chargeTimesToBeatRecord = mutableListOf<Long>()
        repeat(race.time.toInt()) {
            val chargingTime = it + 1L
            val remainingTime = race.time - chargingTime
            if (remainingTime * chargingTime > race.distance) chargeTimesToBeatRecord.add(chargingTime)
        }
        return chargeTimesToBeatRecord.size.toLong()
    }

    private data class Race(
        val time: Long,
        val distance: Long,
    )

    override fun solvePart2(lines: List<String>) =
        calculateNumberOfWaysToBeatSingleRace(getRaceInformation(lines))

    private fun getRaceInformation(lines: List<String>) =
        Race(
            parseLine(lines[0]),
            parseLine(lines[1]),
        )

    private fun parseLine(line: String) =
        line.substringAfter(":").replace(" ", "").toLong()

    private fun calculateNumberOfWaysToBeatSingleRace(race: Race): Long {
        var numberOfWaysToBeatCurrentRecord = 0L

        for (chargeTimeAndSpeed in 1..race.time) {
            val remainingTime = race.time - chargeTimeAndSpeed
            when {
                remainingTime * chargeTimeAndSpeed > race.distance -> numberOfWaysToBeatCurrentRecord++
                numberOfWaysToBeatCurrentRecord != 0L -> return numberOfWaysToBeatCurrentRecord
            }
        }
        return numberOfWaysToBeatCurrentRecord
    }
}
