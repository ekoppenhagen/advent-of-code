package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay

// https://adventofcode.com/2023/day/5
class Day5 : AbstractAocDay(year = 2023, day = 5) {

    override fun solvePart1(lines: List<String>) =
        getLowestLocationNumbers(lines).min()

    private fun getLowestLocationNumbers(lines: List<String>): List<Long> =
        getSeeds(lines.first()).map { getLowestLocationNumber(it, lines) }

    private fun getSeeds(seeds: String): List<Long> =
        seeds.substringAfter("seeds: ").split(" ").map(String::toLong)

    private fun getLowestLocationNumber(seed: Long, lines: List<String>) =
        getValueFromSection(seed, getRatioSection("seed-to-soil", lines))
            .let { getValueFromSection(it, getRatioSection("soil-to-fertilizer", lines)) }
            .let { getValueFromSection(it, getRatioSection("fertilizer-to-water", lines)) }
            .let { getValueFromSection(it, getRatioSection("water-to-light", lines)) }
            .let { getValueFromSection(it, getRatioSection("light-to-temperature", lines)) }
            .let { getValueFromSection(it, getRatioSection("temperature-to-humidity", lines)) }
            .let { getValueFromSection(it, getRatioSection("humidity-to-location", lines)) }

    private fun getValueFromSection(key: Long, ratioSection: List<Triple<Long, Long, Long>>): Long {
        val potentialRatio = ratioSection.filter { it.first <= key }.maxByOrNull(Triple<Long, Long, Long>::first)
        return if (potentialRatio != null && key <= potentialRatio.first + potentialRatio.third) potentialRatio.second + key - potentialRatio.first else key
    }

    private fun getRatioSection(ratio: String, lines: List<String>): List<Triple<Long, Long, Long>> {
        val sectionStart = lines.subList(lines.indexOf("$ratio map:") + 1, lines.size)
        return sectionStart
            .take(sectionStart.indexOf("").let { if (it != -1) it else sectionStart.size } + 1)
            .map {
                val information = it.split(" ")
                Triple(information[1].toLong(), information[0].toLong(), information[2].toLong())
            }
    }

    override fun solvePart2(lines: List<String>): Long {
        val seedRanges = getSeedRanges(lines.first())

        var minimalLocationValue = 0L
        while (true) {
            if (isInSeedRange(getSeedForLocation(minimalLocationValue, lines), seedRanges)) return minimalLocationValue
            minimalLocationValue++
        }
    }

    private fun getSeedRanges(seedRanges: String): List<Pair<Long, Long>> =
        seedRanges.substringAfter("seeds: ")
            .split(" ")
            .chunked(2)
            .map { it[0].toLong() to it[0].toLong() + it[1].toLong() - 1 }

    private fun isInSeedRange(seedForLocation: Long, seedRanges: List<Pair<Long, Long>>) =
        seedRanges.any { it.first <= seedForLocation && it.second >= seedForLocation }

    private fun getSeedForLocation(location: Long, lines: List<String>) =
        getRequiredValueForDesiredOutcome("humidity-to-location", lines, location)
            .let { getRequiredValueForDesiredOutcome("temperature-to-humidity", lines, it) }
            .let { getRequiredValueForDesiredOutcome("light-to-temperature", lines, it) }
            .let { getRequiredValueForDesiredOutcome("water-to-light", lines, it) }
            .let { getRequiredValueForDesiredOutcome("fertilizer-to-water", lines, it) }
            .let { getRequiredValueForDesiredOutcome("soil-to-fertilizer", lines, it) }
            .let { getRequiredValueForDesiredOutcome("seed-to-soil", lines, it) }

    private fun getRequiredValueForDesiredOutcome(section: String, lines: List<String>, outcome: Long): Long =
        getRangeAndKeys(section, lines)
            .firstOrNull { it.first <= outcome && it.second >= outcome }
            ?.let { it.third + outcome - it.first }
            ?: outcome

    private fun getRangeAndKeys(section: String, lines: List<String>): List<Triple<Long, Long, Long>> {
        val sectionStart = lines.subList(lines.indexOf("$section map:") + 1, lines.size)
        return sectionStart.take(sectionStart.indexOf("").let { if (it != -1) it else sectionStart.size } + 1)
            .map {
                val information = it.split(" ")
                Triple(information[0].toLong(), information[0].toLong() + information[2].toLong() - 1L, information[1].toLong())
            }
    }
}
