package io.github.ekoppenhagen.aoc.common

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend inline fun <T, R> Iterable<T>.mapParallel(crossinline transform: (T) -> R) =
    coroutineScope {
        map { value ->
            async { transform(value) }
        }.map { it.await() }
    }
