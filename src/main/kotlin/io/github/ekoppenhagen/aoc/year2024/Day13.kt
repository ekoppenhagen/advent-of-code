package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.getAllNumbers

suspend fun main() {
    Day13().solve()
}

class Day13 : AbstractAocDay(
    exampleResultPart1 = 480,
) {

    override suspend fun solvePart1(clawMachineConfigurations: List<String>) =
        calculateMinimumNumberOfTokensToWinAllPossiblePrices(clawMachineConfigurations).sum()

    private fun calculateMinimumNumberOfTokensToWinAllPossiblePrices(
        clawMachineConfigurations: List<String>,
        priceOffset: Long = 0,
        ignoreButtonPressNumber: Boolean = false,
    ) = extractButtonBehaviorsAndPriceLocations(clawMachineConfigurations, priceOffset)
        .map { getMinimumAmountOfTokensForPrice(it, ignoreButtonPressNumber) }

    private fun extractButtonBehaviorsAndPriceLocations(clawMachineConfigurations: List<String>, priceOffset: Long) =
        mutableListOf<ClawMachineConfiguration>().apply {
            for (configurationNumber in 0 until getTotalNumberOfClawMachineConfigurations(clawMachineConfigurations)) {
                this.add(
                    ClawMachineConfiguration(
                        buttonA = getPairOfNumbersFromConfiguration(clawMachineConfigurations, configurationNumber),
                        buttonB = getPairOfNumbersFromConfiguration(clawMachineConfigurations, configurationNumber, 1),
                        price = getPairOfNumbersFromConfiguration(clawMachineConfigurations, configurationNumber, 2)
                            .let { it.first + priceOffset to it.second + priceOffset },
                    )
                )
            }
        }

    private fun getTotalNumberOfClawMachineConfigurations(clawMachineConfigurations: List<String>): Int = (clawMachineConfigurations.size + 1) / 4

    private fun getPairOfNumbersFromConfiguration(clawMachineConfigurations: List<String>, configurationNumber: Int, offset: Int = 0) =
        clawMachineConfigurations[(configurationNumber * 4) + offset].getAllNumbers().let { it[0] to it[1] }

    private fun getMinimumAmountOfTokensForPrice(clawMachineConfiguration: ClawMachineConfiguration, ignoreButtonPressNumber: Boolean): Long {
        val determinantA = calculateDeterminantA(clawMachineConfiguration)
        val determinantA1 = calculateDeterminantA1(clawMachineConfiguration)
        val determinantA2 = calculateDeterminantA2(clawMachineConfiguration)

        return when {
            isPriceImpossible(determinantA) -> 0L
            // we don't check infinite possibilities
            else -> calculateRequiredButtonPresses(determinantA, determinantA1, determinantA2)
                .filterInvalid(ignoreButtonPressNumber, clawMachineConfiguration)
                .toTokens()
        }
    }

    private fun calculateDeterminantA(clawMachineConfiguration: ClawMachineConfiguration) =
        calculateDeterminant(
            clawMachineConfiguration.buttonA.first,
            clawMachineConfiguration.buttonA.second,
            clawMachineConfiguration.buttonB.first,
            clawMachineConfiguration.buttonB.second
        )

    private fun calculateDeterminantA1(clawMachineConfiguration: ClawMachineConfiguration) =
        calculateDeterminant(
            clawMachineConfiguration.price.first,
            clawMachineConfiguration.price.second,
            clawMachineConfiguration.buttonB.first,
            clawMachineConfiguration.buttonB.second
        )

    private fun calculateDeterminantA2(clawMachineConfiguration: ClawMachineConfiguration) =
        calculateDeterminant(
            clawMachineConfiguration.buttonA.first,
            clawMachineConfiguration.buttonA.second,
            clawMachineConfiguration.price.first,
            clawMachineConfiguration.price.second
        )

    // https://en.wikipedia.org/wiki/Determinant
    private fun calculateDeterminant(a: Long, b: Long, c: Long, d: Long) = a * d - b * c

    private fun isPriceImpossible(determinantA: Long) =
        determinantA == 0L

    private fun calculateRequiredButtonPresses(determinantA: Long, determinantA1: Long, determinantA2: Long) =
        // https://en.wikipedia.org/wiki/Cramer%27s_rule
        calculateButtonPresses(determinantA1, determinantA) to calculateButtonPresses(determinantA2, determinantA)

    private fun calculateButtonPresses(numerator: Long, denominator: Long) =
        numerator / denominator

    private fun Pair<Long, Long>.filterInvalid(ignoreButtonPressNumber: Boolean, clawMachineConfiguration: ClawMachineConfiguration) =
        when {
            !ignoreButtonPressNumber && exceedsButtonPressLimit(first) -> null
            !ignoreButtonPressNumber && exceedsButtonPressLimit(second) -> null
            !isValidEquation(first, second, clawMachineConfiguration) -> null
            else -> this
        }

    private fun exceedsButtonPressLimit(presses: Long) = presses < 0 || presses > 100

    private fun isValidEquation(
        buttonPressesA: Long,
        buttonPressesB: Long,
        configuration: ClawMachineConfiguration
    ) = buttonPressesA * configuration.buttonA.first + buttonPressesB * configuration.buttonB.first == configuration.price.first &&
        buttonPressesA * configuration.buttonA.second + buttonPressesB * configuration.buttonB.second == configuration.price.second

    private fun Pair<Long, Long>?.toTokens() =
        this?.let { first * PRESS_A_TOKEN_COST + second * PRESS_B_TOKEN_COST }
            ?: 0L

    override suspend fun solvePart2(clawMachineConfigurations: List<String>) =
        calculateMinimumNumberOfTokensToWinAllPossiblePrices(clawMachineConfigurations, 10_000_000_000_000, ignoreButtonPressNumber = true).sum()

    data class ClawMachineConfiguration(val buttonA: Pair<Long, Long>, val buttonB: Pair<Long, Long>, val price: Pair<Long, Long>)

    private companion object {

        private const val PRESS_A_TOKEN_COST = 3
        private const val PRESS_B_TOKEN_COST = 1
    }
}
