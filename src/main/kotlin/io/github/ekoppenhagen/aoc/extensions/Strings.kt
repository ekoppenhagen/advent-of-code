package io.github.ekoppenhagen.aoc.extensions

fun String.toListOfDigits() =
    this.toCharArray()
        .partition(Char::isDigit)
        .first
        .map { it.digitToInt() }

fun String.toListOfDigitsWithSpelledOutNumbers() =
    getAllNumbersIncludingSpelledOutOnes(this).map(::toArabicNumeral)

private fun getAllNumbersIncludingSpelledOutOnes(string: String): List<String> {
    val allIncludedNumbers = mutableListOf<String>()
    var indexWithDigit: Pair<Int, String>? = -1 to ""

    while (indexWithDigit != null) {
        indexWithDigit = string.findAnyOf(numbers, startIndex = indexWithDigit.first + 1, ignoreCase = true)
        indexWithDigit?.let { allIncludedNumbers.add(it.second) }
    }

    return allIncludedNumbers
}

private val numbers = listOf(
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
)


private fun toArabicNumeral(number: String) =
    if (number.length == 1) number.toInt() else arabicNumeralsOfSpelledOutNumbers.getValue(number)

@Suppress("MagicNumber")
private val arabicNumeralsOfSpelledOutNumbers = mapOf(
    "zero" to 0,
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)