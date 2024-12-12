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

fun String.getAllNumbers() =
    Regex("(?<numbers>[0-9]+)").findAll(this)
        .mapNotNull { it.value.toLongOrNull() }
        .toList()

fun String.getAllNumbersWithIndex() =
    Regex("(?<numbers>[0-9]+)").findAll(this)
        .map { it.value.toLong() to it.range.first }
        .toList()
