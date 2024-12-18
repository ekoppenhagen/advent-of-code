package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.getAllNumbers

suspend fun main() {
    Day5().solve()
}

class Day5 : AbstractAocDay(
    exampleResultPart1 = 35,
    exampleResultPart2 = 46,
) {

    override suspend fun solvePart1(rawAlmanac: List<String>) =
        Almanac(rawAlmanac).findLowestLocationNumber(getSeeds(rawAlmanac))

    private fun getSeeds(rawAlmanac: List<String>) = rawAlmanac.first().getAllNumbers()

    override suspend fun solvePart2(rawAlmanac: List<String>) =
        Almanac(rawAlmanac).findMatchingSeed(getSeedsWithRanges(rawAlmanac))

    private fun getSeedsWithRanges(rawAlmanac: List<String>) =
        rawAlmanac.first().getAllNumbers().chunked(2).map { it.first()..it.last() + it.first() }

    private class Almanac(rawAlmanac: List<String>) {

        fun findLowestLocationNumber(seeds: List<Long>) =
            seeds.minOf {
                sections.fold(it) { value, almanacSection -> almanacSection.getDestinationForValue(value) }
            }

        fun findMatchingSeed(seeds: List<LongRange>) =
            generateSequence(0L) { it + 1 }
                .first {
                    sectionsReversed.fold(it) { value, almanacSection -> almanacSection.getValueForDestination(value) }
                        .let { seed -> seeds.any { seed in it } }
                }

        private val sections = listOf(
            AlmanacSection("seed-to-soil", rawAlmanac),
            AlmanacSection("soil-to-fertilizer", rawAlmanac),
            AlmanacSection("fertilizer-to-water", rawAlmanac),
            AlmanacSection("water-to-light", rawAlmanac),
            AlmanacSection("light-to-temperature", rawAlmanac),
            AlmanacSection("temperature-to-humidity", rawAlmanac),
            AlmanacSection("humidity-to-location", rawAlmanac),
        )

        private val sectionsReversed = sections.reversed()

        private class AlmanacSection(sectionName: String, private val rawAlmanac: List<String>) {

            private val sectionContent = getAlmanacSectionRatios(sectionName)

            fun getDestinationForValue(value: Long) =
                sectionContent.firstOrNull { it.isValueInSection(value) }?.getDestinationForValue(value) ?: value

            fun getValueForDestination(destination: Long) =
                sectionContent.firstOrNull { it.isDestinationInSection(destination) }?.getValueForDestination(destination) ?: destination

            private fun getAlmanacSectionRatios(sectionName: String) =
                getSectionRatios(sectionName).map { almanacEntry ->
                    almanacEntry.getAllNumbers().let {
                        AlmanacSectionRatio(it[0].toLong(), it[1].toLong(), it[2].toLong())
                    }
                }

            private fun getSectionRatios(sectionName: String) =
                rawAlmanac
                    .drop(rawAlmanac.indexOf("$sectionName map:") + 1)
                    .takeWhile { it != ("") }
        }

        private data class AlmanacSectionRatio(
            private val destinationRangeStart: Long,
            private val sourceRangeStart: Long,
            private val rangeLength: Long,
        ) {

            fun isValueInSection(value: Long) = sourceRangeStart <= value && value < (sourceRangeStart + rangeLength)

            fun isDestinationInSection(destination: Long) = destinationRangeStart <= destination && destination < (destinationRangeStart + rangeLength)

            fun getDestinationForValue(value: Long) = destinationRangeStart + (value - sourceRangeStart)

            fun getValueForDestination(destination: Long) = sourceRangeStart + (destination - destinationRangeStart)
        }
    }
}
