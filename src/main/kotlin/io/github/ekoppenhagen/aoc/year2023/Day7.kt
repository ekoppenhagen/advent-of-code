package io.github.ekoppenhagen.aoc.year2023

import io.github.ekoppenhagen.aoc.AbstractAocDay

class Day7 : AbstractAocDay(
    exampleResultPart1 = 6440,
    exampleResultPart2 = 5905,
) {

    private val playingCards = charArrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    private val playingCardsWithJokers = charArrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    override fun solvePart1(rawHands: List<String>) =
        getCamelCardsWinnings(rawHands)

    private fun getCamelCardsWinnings(rawHands: List<String>) =
        getWinningsOfHands(rawHands.map(::toHand)).sum()

    private fun getWinningsOfHands(hands: List<Hand>) =
        hands.sortedWith(HandRankingComparator(playingCards))
            .map(Hand::bid)
            .mapIndexed(::calculateWinningsOfHand)

    private fun toHand(line: String) =
        Hand(line.split(" ")[0], line.split(" ")[1].toInt(), false)

    private fun calculateWinningsOfHand(rankMinusOne: Int, bid: Int) =
        (rankMinusOne + 1) * bid

    override fun solvePart2(rawHands: List<String>) =
        getCamelCardsWithJokersWinnings(rawHands)

    private fun getCamelCardsWithJokersWinnings(rawHands: List<String>) =
        getWinningsOfHandsWithJokers(rawHands.map(::toHandWithJokers)).sum()

    private fun getWinningsOfHandsWithJokers(hands: List<Hand>) =
        hands.sortedWith(HandRankingComparator(playingCardsWithJokers))
            .map(Hand::bid)
            .mapIndexed(::calculateWinningsOfHand)

    private fun toHandWithJokers(line: String) =
        Hand(line.split(" ")[0], line.split(" ")[1].toInt(), true)

    private data class Hand(val cards: String, val bid: Int, val isWithJokers: Boolean) {

        val type = getType(cards, isWithJokers)

        private fun getType(cards: String, isWithJokers: Boolean) =
            if (isWithJokers) getTypeWithJokers(cards)
            else getTypeWithoutJokers(cards)

        private fun getTypeWithoutJokers(cards: String) =
            when (cards.toMutableList().distinct().size) {
                5 -> Type.HIGH_CARD
                4 -> Type.ONE_PAIR
                3 -> if (isTwoPair(cards)) Type.TWO_PAIR else Type.THREE_OF_A_KIND
                2 -> if (isFourOfaKind(cards)) Type.FOUR_OF_A_KIND else Type.FULL_HOUSE
                1 -> Type.FIVE_OF_A_KIND
                else -> Type.UNKNOWN
            }

        private fun isFourOfaKind(cards: String) =
            cards.count { it == cards[0] } == 1 ||
                cards.count { it == cards[0] } == 4

        private fun isTwoPair(cards: String): Boolean {
            cards.forEach { charToCount ->
                when {
                    cards.count { it == charToCount } == 2 -> return true
                }
            }
            return false
        }

        private fun getTypeWithJokers(cards: String): Type {
            val amountOfJokers = cards.count { it == 'J' }
            val cardsWithoutJokers = cards.replace("J", "")
            val mostOccurringCharacter: String = getMostOccurringCharacter(cardsWithoutJokers)
            val substitutedHand = cardsWithoutJokers.plus(mostOccurringCharacter.repeat(amountOfJokers))
            return getTypeWithoutJokers(substitutedHand)
        }

        private fun getMostOccurringCharacter(cardsWithoutJokers: String): String {
            var mostOccurringChar = "!"
            var mostOccurrences = 0
            cardsWithoutJokers.forEach { charToCount ->
                val occurrences = cards.count { it == charToCount }
                if (occurrences > mostOccurrences) {
                    mostOccurrences = occurrences
                    mostOccurringChar = "$charToCount"
                }
            }
            return mostOccurringChar
        }
    }

    private enum class Type(val ranking: Int) {
        FIVE_OF_A_KIND(6),
        FOUR_OF_A_KIND(5),
        FULL_HOUSE(4),
        THREE_OF_A_KIND(3),
        TWO_PAIR(2),
        ONE_PAIR(1),
        HIGH_CARD(0),
        UNKNOWN(-1),
    }

    private class HandRankingComparator(val playingCards: CharArray) : Comparator<Hand> {

        @Suppress("ReturnCount")
        override fun compare(hand1: Hand, hand2: Hand): Int {
            val rankingComparison = hand1.type.ranking - hand2.type.ranking
            if (rankingComparison != 0) return rankingComparison

            for ((index, card) in hand1.cards.withIndex()) {
                val cardRankingComparison = playingCards.indexOf(hand2.cards[index]) - playingCards.indexOf(card)
                if (cardRankingComparison != 0) return cardRankingComparison
            }

            return 0
        }
    }
}
