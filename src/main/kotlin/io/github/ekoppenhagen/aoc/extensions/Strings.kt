package io.github.ekoppenhagen.aoc.extensions

import io.github.ekoppenhagen.aoc.extensions.helpers.StringHelpers

private val stringHelpers = StringHelpers()

fun String.toListOfDigits() =
    this.toCharArray()
        .partition(Char::isDigit)
        .first
        .map { it.digitToInt() }

fun String.toListOfDigitsWithSpelledOutNumbers() =
    stringHelpers.getAllNumbersIncludingSpelledOutOnes(this)
        .map { stringHelpers.toArabicNumeral(it) }

fun String.getAllNumbers(includeNegativeNumbers: Boolean = false) =
    (if (includeNegativeNumbers) Regex("(-?\\d+)") else Regex("(\\d+)"))
        .findAll(this)
        .mapNotNull { it.value.toLongOrNull() }
        .toList()

fun String.getAllNumbersWithIndex(includeNegativeNumbers: Boolean = false) =
    (if (includeNegativeNumbers) Regex("(-?\\d+)") else Regex("(\\d+)"))
        .findAll(this)
        .map { it.value.toLong() to it.range.first }
        .toList()
