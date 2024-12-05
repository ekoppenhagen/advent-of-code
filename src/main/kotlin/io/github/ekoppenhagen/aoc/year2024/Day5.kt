package io.github.ekoppenhagen.aoc.year2024

import io.github.ekoppenhagen.aoc.AbstractAocDay

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

    private fun parseOrderingRule(string: String): Pair<Int, Int> =
        string.split("|").let { Pair(it[0].toInt(), it[1].toInt()) }

    private fun getOrderingRuleLines(pageOrderingRulesAndUpdates: List<String>) =
        pageOrderingRulesAndUpdates.subList(0, pageOrderingRulesAndUpdates.indexOf(""))

    private fun extractUpdates(pageOrderingRulesAndUpdates: List<String>) =
        getUpdateLines(pageOrderingRulesAndUpdates).map(::parseUpdate)

    private fun getUpdateLines(pageOrderingRulesAndUpdates: List<String>) =
        pageOrderingRulesAndUpdates.drop(pageOrderingRulesAndUpdates.indexOf("") + 1)

    private fun parseUpdate(update: String) =
        update.split(",").map(String::toInt)

    private fun findAllValidUpdateMiddlePages(pageOrderingRules: List<Pair<Int, Int>>, updates: List<List<Int>>) =
        findAllValidUpdates(pageOrderingRules, updates).map(::getMiddlePage)

    private fun findAllValidUpdates(pageOrderingRules: List<Pair<Int, Int>>, updates: List<List<Int>>) =
        updates.filter { isValidUpdate(it, pageOrderingRules) }

    private fun isValidUpdate(update: List<Int>, pageOrderingRules: List<Pair<Int, Int>>) =
        !update.any {
            isOrderingRuleViolated(
                it,
                update.subList(0, update.indexOf(it)),
                update.drop(update.indexOf(it) + 1),
                getMatchingOrderingRules(it, pageOrderingRules),
            )
        }

    private fun isOrderingRuleViolated(
        page: Int,
        pagesBefore: List<Int>,
        pagesAfter: List<Int>,
        matchingRules: List<Pair<Int, Int>>,
    ) = matchingRules.any {
        if (page == it.first) pagesBefore.contains(it.second)
        else pagesAfter.contains(it.first)
    }

    private fun getMatchingOrderingRules(page: Int, pageOrderingRules: List<Pair<Int, Int>>) =
        pageOrderingRules.filter { it.first == page || it.second == page }

    private fun getMiddlePage(validUpdate: List<Int>) =
        validUpdate[validUpdate.size / 2]

    override fun solvePart2(pageOrderingRulesAndUpdates: List<String>) =
        calculateSumOfAllInvalidUpdateMiddlePages(pageOrderingRulesAndUpdates)

    private fun calculateSumOfAllInvalidUpdateMiddlePages(pageOrderingRulesAndUpdates: List<String>) =
        findAllInvalidUpdateMiddlePages(
            extractPageOrderingRules(pageOrderingRulesAndUpdates),
            extractUpdates(pageOrderingRulesAndUpdates),
        ).sum()

    private fun findAllInvalidUpdateMiddlePages(pageOrderingRules: List<Pair<Int, Int>>, updates: List<List<Int>>) =
        sortByOrderingRules(findAllInvalidUpdates(pageOrderingRules, updates), pageOrderingRules).map(::getMiddlePage)

    private fun findAllInvalidUpdates(pageOrderingRules: List<Pair<Int, Int>>, updates: List<List<Int>>) =
        updates.filter { !isValidUpdate(it, pageOrderingRules) }

    private fun sortByOrderingRules(invalidUpdates: List<List<Int>>, pageOrderingRules: List<Pair<Int, Int>>) =
        mutableListOf<List<Int>>().apply {
            invalidUpdates.forEach { this.add(it.sortedWith(UpdatePageComparator(pageOrderingRules))) }
        }

    private class UpdatePageComparator(private val pageOrderingRules: List<Pair<Int, Int>>) : Comparator<Int> {

        override fun compare(firstUpdatePage: Int, secondUpdatePage: Int) =
            pageOrderingRules
                .find {
                    it.first == firstUpdatePage && it.second == secondUpdatePage ||
                        it.first == secondUpdatePage && it.second == firstUpdatePage
                }
                ?.let { if (it.first == firstUpdatePage) -1 else 1 }
                ?: 0
    }
}
