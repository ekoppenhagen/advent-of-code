package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay
import io.github.ekoppenhagen.aoc.extensions.getAllNumbers

// https://adventofcode.com/2024/day/5
class Day5 : AbstractAocDay(year = 2024, day = 5) {

    override fun solvePart1(pageOrderingRulesAndUpdates: List<String>) =
        calculateSumOfAllValidUpdateMiddlePages(pageOrderingRulesAndUpdates)

    private fun calculateSumOfAllValidUpdateMiddlePages(pageOrderingRulesAndUpdates: List<String>) =
        findAllValidUpdateMiddlePages(
            extractPageOrderingRules(pageOrderingRulesAndUpdates),
            extractUpdates(pageOrderingRulesAndUpdates),
        ).sum()

    private fun extractPageOrderingRules(pageOrderingRulesAndUpdates: List<String>) =
        getOrderingRuleLines(pageOrderingRulesAndUpdates).map(::parseOrderingRule)

    private fun parseOrderingRule(rawOrderingRule: String): Pair<Long, Long> =
        rawOrderingRule.getAllNumbers().let { Pair(it[0], it[1]) }

    private fun getOrderingRuleLines(pageOrderingRulesAndUpdates: List<String>) =
        pageOrderingRulesAndUpdates.take(pageOrderingRulesAndUpdates.indexOf("") + 1)

    private fun extractUpdates(pageOrderingRulesAndUpdates: List<String>) =
        getUpdateLines(pageOrderingRulesAndUpdates).map(::parseUpdate)

    private fun getUpdateLines(pageOrderingRulesAndUpdates: List<String>) =
        pageOrderingRulesAndUpdates.drop(pageOrderingRulesAndUpdates.indexOf("") + 1)

    private fun parseUpdate(update: String) =
        update.getAllNumbers()

    private fun findAllValidUpdateMiddlePages(pageOrderingRules: List<Pair<Long, Long>>, updates: List<List<Long>>) =
        findAllValidUpdates(pageOrderingRules, updates).map(::getMiddlePage)

    private fun findAllValidUpdates(pageOrderingRules: List<Pair<Long, Long>>, updates: List<List<Long>>) =
        updates.filter { isValidUpdate(it, pageOrderingRules) }

    private fun isValidUpdate(update: List<Long>, pageOrderingRules: List<Pair<Long, Long>>) =
        !update.any {
            isOrderingRuleViolated(
                it,
                update.take(update.indexOf(it) + 1),
                update.drop(update.indexOf(it) + 1),
                getMatchingOrderingRules(it, pageOrderingRules),
            )
        }

    private fun isOrderingRuleViolated(
        page: Long,
        pagesBefore: List<Long>,
        pagesAfter: List<Long>,
        matchingRules: List<Pair<Long, Long>>,
    ) = matchingRules.any {
        if (page == it.first) pagesBefore.contains(it.second)
        else pagesAfter.contains(it.first)
    }

    private fun getMatchingOrderingRules(page: Long, pageOrderingRules: List<Pair<Long, Long>>) =
        pageOrderingRules.filter { it.first == page || it.second == page }

    private fun getMiddlePage(validUpdate: List<Long>) =
        validUpdate[validUpdate.size / 2]

    override fun solvePart2(pageOrderingRulesAndUpdates: List<String>) =
        calculateSumOfAllInvalidUpdateMiddlePages(pageOrderingRulesAndUpdates)

    private fun calculateSumOfAllInvalidUpdateMiddlePages(pageOrderingRulesAndUpdates: List<String>) =
        findAllInvalidUpdateMiddlePages(
            extractPageOrderingRules(pageOrderingRulesAndUpdates),
            extractUpdates(pageOrderingRulesAndUpdates),
        ).sum()

    private fun findAllInvalidUpdateMiddlePages(pageOrderingRules: List<Pair<Long, Long>>, updates: List<List<Long>>) =
        sortByOrderingRules(findAllInvalidUpdates(pageOrderingRules, updates), pageOrderingRules).map(::getMiddlePage)

    private fun findAllInvalidUpdates(pageOrderingRules: List<Pair<Long, Long>>, updates: List<List<Long>>) =
        updates.filter { !isValidUpdate(it, pageOrderingRules) }

    private fun sortByOrderingRules(invalidUpdates: List<List<Long>>, pageOrderingRules: List<Pair<Long, Long>>) =
        mutableListOf<List<Long>>().apply {
            invalidUpdates.forEach { this.add(it.sortedWith(UpdatePageComparator(pageOrderingRules))) }
        }

    private class UpdatePageComparator(private val pageOrderingRules: List<Pair<Long, Long>>) : Comparator<Long> {

        override fun compare(firstUpdatePage: Long, secondUpdatePage: Long) =
            pageOrderingRules
                .find {
                    it.first == firstUpdatePage && it.second == secondUpdatePage ||
                        it.first == secondUpdatePage && it.second == firstUpdatePage
                }
                ?.let { if (it.first == firstUpdatePage) -1 else 1 }
                ?: 0
    }
}
