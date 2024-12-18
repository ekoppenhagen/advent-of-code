package io.github.ekoppenhagen.aoc.extensions

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend inline fun <T, R> Iterable<T>.mapParallel(crossinline transform: (T) -> R) =
    coroutineScope {
        map {
            async { transform(it) }
        }.map { it.await() }
    }
